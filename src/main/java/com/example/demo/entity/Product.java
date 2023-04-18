package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String productCode;
    private String name;
    private double pricePerUnit;
    private int quantity;
    private double pricePerSet;


    public Product() {
    }

    public Product(String name, double pricePerUnit, String productCode, int quantity) {
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public Product(String name, int setQuantity, double pricePerSet, String productCode) {
        this.name = name;
        this.quantity = setQuantity;
        this.pricePerSet = pricePerSet;
        this.productCode = productCode;
    }
}
