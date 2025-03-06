package com.shop.sport.Repositories;

import com.shop.sport.Entity.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface ICartItemRepository extends CrudRepository<CartItem, Long> {
}
