package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getAll();

    Double calculateBasketCost(List<Product> products);
}
