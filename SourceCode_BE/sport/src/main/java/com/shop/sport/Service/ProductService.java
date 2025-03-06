package com.shop.sport.Service;

import com.shop.sport.Entity.Product;
import com.shop.sport.Repositories.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private IProductRepo iProductRepo;

    public List<Product> getAllProduct() {
        return iProductRepo.findAllProduct();
    }

    public Long checkDelete(long id) {
        Long result = iProductRepo.check_delete_product(id);
        return result;
    }

    public List<Product> bestSell() {
        return iProductRepo.bestSellProduct();
    }

    public List<Product> getAllProductByCategory(String categoryName) {
        return iProductRepo.getAllProductByCategory(categoryName);
    }

    public List<Product> searchProduct(String text) {
        return iProductRepo.searchProduct(text);
    }

    public Optional<Product> getProductById(long id) {
        return iProductRepo.findById(id);
    }

    public Boolean DeleteProduct(long id) {
        try {
            iProductRepo.deleteById(id);
            return true;
        }catch (Exception e) {
            return false;
        }

    }

    public Product createProduct(Product product) {
//        if(user.getRole().equals("ADMIN"))
//            user.setRole(RoleEnum.ADMIN);
//        else user.setRole(RoleEnum.EMPLOYEE);
//        String passWord = passwordEncoder.encode(user.getPassword());
//        user.setPassword(passWord);
        return iProductRepo.save(product);
    }
}
