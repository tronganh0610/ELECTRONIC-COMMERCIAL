package com.shop.sport.Repositories;

import com.shop.sport.Entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryRepository extends CrudRepository<Category, Long> {

}
