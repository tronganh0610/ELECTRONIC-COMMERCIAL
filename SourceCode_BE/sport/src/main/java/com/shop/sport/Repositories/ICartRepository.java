package com.shop.sport.Repositories;

import com.shop.sport.DTO.CartDTO;
import com.shop.sport.Entity.Cart;
import com.shop.sport.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface ICartRepository extends CrudRepository<Cart, Long> {


//    @Query(value = "CALL addtoCart(:id_User, :id_Product,:quantity)", nativeQuery = true)
//    String addtoCart(@Param("id_User") long id_User,
//                       @Param("id_Product") long id_Product,
//                       @Param("quantity") long quantity);

    @Procedure(name = "addtoCart")
    void addtoCart(@Param("id_User") long id_User,
                       @Param("id_Product") long id_Product,
                       @Param("quantity") long quantity);

    @Query(value = "CALL getCart_by_iduser(:id_User)", nativeQuery = true)
    List<CartDTO> getCartByIdUser(@Param("id_User") long idProduct);
}
