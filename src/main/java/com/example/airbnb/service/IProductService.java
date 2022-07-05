package com.example.airbnb.service;

import com.example.airbnb.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IService<Product>{
    Page<Product> findAllByNameContaining(String name, Pageable pageable);
}
