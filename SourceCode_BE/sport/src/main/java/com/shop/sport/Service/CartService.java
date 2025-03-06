package com.shop.sport.Service;

import com.shop.sport.DTO.CartDTO;
import com.shop.sport.Entity.CartItem;
import com.shop.sport.Entity.Product;
import com.shop.sport.Repositories.ICartItemRepository;
import com.shop.sport.Repositories.ICartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private ICartRepository iCartRepository;

    @Autowired
    private ICartItemRepository iCartItemRepository;

    public Boolean addtoCart1(long idUser, long idProduct, long quatity) {
        try {
        iCartRepository.addtoCart(idUser,idProduct,quatity);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<CartDTO> getCartByIdUser(long idUser) {
        return iCartRepository.getCartByIdUser(idUser);
    }

    public Boolean deleteCartItem(long id){

        try {
            CartItem cartItem = iCartItemRepository.findById(id).get();
            System.out.println(cartItem.getQuantity());
            if(cartItem== null)

                return false;
            iCartItemRepository.deleteById(id);
            return  true;
        }catch (Exception e) {
            return false;
        }

    }
}
