package com.example.demo.exception;

public class ProductNotEnoughException extends RuntimeException {
    public ProductNotEnoughException(String message) {
        super(message);
    }
}
