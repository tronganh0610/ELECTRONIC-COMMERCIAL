package com.shop.sport.Controller;

import com.shop.sport.DTO.CartDTO;
import com.shop.sport.Entity.CartItem;
import com.shop.sport.Entity.Product;
import com.shop.sport.Entity.Role;
import com.shop.sport.Entity.User;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.CartService;
import com.shop.sport.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart-management")
public class CartController {

    Response response = Response.getInstance();


    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public ResponseEntity<Object> getAllProductInCart(
            @RequestParam("idUser") long idUser
    ) {
        try {
            List<CartDTO> listCart = cartService.getCartByIdUser(idUser);
                return response.generateResponse("get cart by id user successfully", HttpStatus.OK, listCart);
        } catch (Exception e) {
            return response.generateResponse("get cart by id user failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTOCart(@PathVariable("id") long id) {
        try {

          Boolean deleteCartItem =  cartService.deleteCartItem(id);
          if (deleteCartItem)
            return response.generateResponse("delete item in cart successfully", HttpStatus.OK, deleteCartItem);
            return response.generateResponse("delete item in cart failed", HttpStatus.BAD_REQUEST, null);

        }catch (Exception e) {
            return response.generateResponse("delete item in cart failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);


        }
    }

//    @PostMapping("/{id}")
//    public ResponseEntity<Object> updateCart(@PathVariable("id") long id) {
//        try {
//
//
//
//
//            if (deleteCartItem)
//                return response.generateResponse("delete item in cart successfully", HttpStatus.OK, deleteCartItem);
//            return response.generateResponse("delete item in cart failed", HttpStatus.BAD_REQUEST, null);
//
//        }catch (Exception e) {
//            return response.generateResponse("delete item in cart failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
//
//
//        }
//    }


    @PostMapping("/carts")
    public ResponseEntity<Object> addToCart(   @RequestBody Map<String, Long> body) {

        try {
            long idUser = body.get("idUser");
            long idProduct = body.get("idProduct");
            long quantity = body.get("quantity");
            System.out.println(idUser);
            Boolean addCart = cartService.addtoCart1(idUser,idProduct,quantity);
            if (addCart){
                return response.generateResponse("Add to Cart Successfully", HttpStatus.OK, addCart);

            }else {
                return response.generateResponse("Add to Cart fail", HttpStatus.BAD_REQUEST, addCart);

            }

        } catch (Exception e) {
            return response.generateResponse("Add to Cart fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }

}
