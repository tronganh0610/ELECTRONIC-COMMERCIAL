package com.shop.sport.Repositories;

import com.shop.sport.Entity.PaymentMethod;
import org.springframework.data.repository.CrudRepository;

public interface IPaymentMethodRepo extends CrudRepository<PaymentMethod, Long> {
}
