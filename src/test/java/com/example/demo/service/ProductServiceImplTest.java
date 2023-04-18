package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    private ProductServiceImpl productService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void checkReturnEmptyProductList() {
        when(productRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        assertEquals(0, productService.getAll().size());

        verify(productRepository).findAll();
    }

    @Test
    public void checkBasketValueAccuracy() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Apple", 1, "A", 3));
        productList.add(new Product("Orange", 2, 5, "D"));

        when(productRepository.getProductsByProductCodeIsIn(any())).thenReturn(productList);

        Double basketCost = productService.calculateBasketCost(productList);

        assertEquals(13.00, basketCost);

        verify(productRepository, times(1)).getProductsByProductCodeIsIn(any());
    }

    @Test
    public void checkEmptyBasket() {
        List<Product> productList = new ArrayList<>();

        when(productRepository.getProductsByProductCodeIsIn(any())).thenReturn(productList);

        Double basketValue = productService.calculateBasketCost(productList);

        assertEquals(0.00, basketValue);

        verify(productRepository, times(1)).getProductsByProductCodeIsIn(any());
    }
}