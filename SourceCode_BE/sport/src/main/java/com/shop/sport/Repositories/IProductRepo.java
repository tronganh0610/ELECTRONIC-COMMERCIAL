package com.shop.sport.Repositories;


import com.shop.sport.Entity.Product;
import com.shop.sport.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface IProductRepo extends CrudRepository<Product, Long> {

    @Query(value = "SELECT u FROM Product u WHERE u.status = true  or u.status= null")
    List<Product> findAllProduct();


    @Query(value = "CALL check_delete_product(:idProduct);", nativeQuery = true)
    Long check_delete_product(@Param("idProduct") long id);
    @Query(value = "CALL best_sell_product();", nativeQuery = true)
    List<Product> bestSellProduct();



    @Query(value = "CALL search_product(:text);", nativeQuery = true)
    List<Product> searchProduct(@Param("text") String text);
    @Query(value = "CALL getAllProductByCategory(:categoryName);", nativeQuery = true)
    List<Product> getAllProductByCategory(@Param("categoryName") String categoryName);
}
