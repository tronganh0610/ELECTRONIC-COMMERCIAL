package com.shop.sport.Controller;

import com.shop.sport.Entity.Category;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.CategoryService;
import com.shop.sport.Service.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    Response response = Response.getInstance();
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("/getAllCategory")
    public ResponseEntity<Object> getAllCategory() {

        try {
            List<Category> category = categoryService.getAllCategory();

            return response.generateResponse("Get All category Successfully", HttpStatus.OK, category);
        } catch (Exception e) {
            return response.generateResponse("Get All category fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }

    @GetMapping("/update/{id}")
    public ResponseEntity<Object> updateCategory(
            @PathVariable long id,
            @RequestParam(value="name", required = false) String name,
            @RequestParam(value="image", required = false) MultipartFile file
    ) {

        try {
            Category category = categoryService.searchCategoryById(id).get();
            if (category != null){

                if (!name.isEmpty()){
                    category.setCategoryName(name);
                }

                if (!file.isEmpty()) {
                    fileUpload.deleteFile(category.getPublicId());

                    Map<String, String> upload = fileUpload.uploadFile(file);

                    category.setImageUrl(upload.get("url"));
                    category.setPublicId(upload.get("public_id"));
                }

                return response.generateResponse("update category Successfully", HttpStatus.OK, category);
            }

            return response.generateResponse("update category failed", HttpStatus.BAD_REQUEST, category);
        } catch (Exception e) {
            return response.generateResponse("update category failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> searchCategoryById(
            @PathVariable("id") long id
    ) {

        try {
            Category category = categoryService.searchCategoryById(id).get();
            if (category != null){
                return response.generateResponse("find category Successfully", HttpStatus.OK, category);
            }

            return response.generateResponse("find category failed", HttpStatus.BAD_REQUEST, category);
        } catch (Exception e) {
            return response.generateResponse("find category failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }



    @PostMapping("/add")
    public ResponseEntity<Object> createCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "image" , required = true) MultipartFile multipartFile

    ) {
        try {
            Category category = new Category();
            category.setCategoryName(categoryName);

            Map<String, String> upload =  fileUpload.uploadFile(multipartFile);
            category.setImageUrl(upload.get("url"));
            category.setPublicId(upload.get("public_id"));


            categoryService.createCategory(category);
            return response.generateResponse("createCategory Successfully", HttpStatus.OK, category);
        } catch (Exception e) {
            return response.generateResponse("createCategory fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


}
