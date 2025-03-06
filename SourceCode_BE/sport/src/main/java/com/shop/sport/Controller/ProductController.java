package com.shop.sport.Controller;

import com.shop.sport.Entity.Product;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.CategoryService;
import com.shop.sport.Service.FileUpload;
import com.shop.sport.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    Response response = Response.getInstance();

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/allProduct")
    public ResponseEntity<Object> getAllProduct() {
        try {
            List<Product> list = productService.getAllProduct();

            return response.generateResponse("Get All product Successfully", HttpStatus.OK, list);

        } catch (Exception e) {
            return response.generateResponse("Get All product fail" + e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }


    @GetMapping("/best_sell")
    public ResponseEntity<Object> bestSellProduct() {
        try {
            List<Product> list = productService.bestSell();

            return response.generateResponse("best_sell product Successfully", HttpStatus.OK, list);

        } catch (Exception e) {
            return response.generateResponse("best_sell product fail" + e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }


    @GetMapping("/byCategory")
    public ResponseEntity<Object> getProductbyCategory(
            @RequestParam("categoryName") String category
    ) {
        try {
            List<Product> list = productService.getAllProductByCategory(category);

            return response.generateResponse("getProductbyCategory Successfully", HttpStatus.OK, list);

        } catch (Exception e) {
            return response.generateResponse("getProductbyCategory product fail in ProductController" + e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

    @GetMapping("/byText")
    public ResponseEntity<Object> searchProductsearchProduct(
            @RequestParam("text") String text
    ) {
        try {
            List<Product> list = productService.searchProduct(text);

            return response.generateResponse("getProductbyCategory Successfully", HttpStatus.OK, list);

        } catch (Exception e) {
            return response.generateResponse("getProductbyCategory product fail in ProductController" + e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> createProduct(
            @RequestParam("stockQuantity") int stockQuantity,
            @RequestParam("price") float price,
            @RequestParam("description") String description,
            @RequestParam("productName") String productName,
            @RequestParam("category_id") long id,
            @RequestParam(value = "image") MultipartFile multipartFile

    ) throws IOException {
        String public_id = "";
        try {
            System.out.println("---------------------");
            System.out.println(stockQuantity);
            System.out.println(price);
            System.out.println(description);
            System.out.println(productName);
            System.out.println(id);
            System.out.println(multipartFile);
            System.out.println("---------------------");
            Product product = new Product();
            product.setProductName(productName);
            product.setStatus(true);
            product.setStockQuantity(stockQuantity);
            product.setPrice(price);
            product.setDescription(description);
            Map<String, String> upload = fileUpload.uploadFile(multipartFile);
            product.setImageUrl(upload.get("url"));
            public_id = upload.get("public_id");
            product.setPublicId(public_id);

            product.setCategory(categoryService.searchCategoryById(id).get());

            product = productService.createProduct(product);

            return response.generateResponse("create product Successfully", HttpStatus.OK, product);

        } catch (Exception e) {
            fileUpload.deleteFile(public_id);
            System.out.println(e.getMessage());
            return response.generateResponse("create product failed ProductController" + e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductByid(
            @PathVariable long id
    ) {

        try {
            Product product = productService.getProductById(id).get();
            if (product == null)
                return response.generateResponse("product not found", HttpStatus.BAD_REQUEST, null);

            return response.generateResponse(" product successfully", HttpStatus.OK, product);

        } catch (Exception e) {
            return response.generateResponse(" product failed", HttpStatus.OK, null);

        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProduct(
            @PathVariable long id
    ) {

        try {

            Product product = productService.getProductById(id).get();

            if (product == null)
                return response.generateResponse("product not found", HttpStatus.BAD_REQUEST, null);

            long isDel =productService.checkDelete(id);
            if (isDel==1) {
                product.setStatus(false);
//            fileUpload.deleteFile(product.getPublicId());
                productService.createProduct(product);
                return response.generateResponse("delete product successfully", HttpStatus.OK, isDel);
            }
            return response.generateResponse("product exist in cart or order !!! delete failer", HttpStatus.OK, null);

        } catch (Exception e) {
            return response.generateResponse("delete product failed"+e.getMessage(), HttpStatus.OK, null);

        }
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object> deleteProductReal(
//            @PathVariable long id
//    ) {
//
//        try {
//            Boolean isDel = productService.DeleteProduct(id);
//            if (isDel)
//                return response.generateResponse("Delete Product sucess", HttpStatus.OK, isDel);
//            return response.generateResponse("delete product failed", HttpStatus.BAD_REQUEST, null);
//
//        } catch (Exception e) {
//            return response.generateResponse("delete product failed", HttpStatus.BAD_REQUEST, null);
//
//        }
//    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable long id,
            @RequestParam(value = "stockQuantity", required = false) String stockQuantity,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "productName", required = false) String productName,
//            @RequestParam(value = "category_id", required = false) String category_id,
            @RequestParam(value = "image", required = false) MultipartFile multipartFile

    ) {

        try {

            Product product = productService.getProductById(id).get();
            if (!stockQuantity.isEmpty()) {
                product.setStockQuantity(Integer.parseInt(stockQuantity));
            }
            if (!price.isEmpty()) {
                product.setPrice(Float.parseFloat(price));
            }
            if (!description.isEmpty()) {
                product.setDescription(description);
            }
            if (!productName.isEmpty()) {
                product.setProductName(productName);
            }
            if (multipartFile!=null) {

                fileUpload.deleteFile(product.getPublicId());

                Map<String, String> upload = fileUpload.uploadFile(multipartFile);

                product.setImageUrl(upload.get("url"));
                product.setPublicId(upload.get("public_id"));
            }

            System.out.println(product.getCategory().getId() +" nânnnaa");


            product = productService.createProduct(product);
            return response.generateResponse("update product Successfully", HttpStatus.OK, product);

        } catch (Exception e) {
            return response.generateResponse("update product failed ProductController" + e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }

    }


}
