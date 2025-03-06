package com.shop.sport.Repositories;

import com.shop.sport.Entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface IOrderItem extends CrudRepository<OrderItem, Long> {
}
