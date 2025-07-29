package com.byllameister.modelstore.repositories;

import com.byllameister.modelstore.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {

}
