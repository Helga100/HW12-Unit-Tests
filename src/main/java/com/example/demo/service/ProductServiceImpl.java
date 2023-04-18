package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotEnoughException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService, InitializingBean {

    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        productRepository.saveAll(List.of(
                new Product("Apple", 1, "A", 20),
                new Product("Apple", 4, 3, "B"),
                new Product("Orange", 2, "C", 15),
                new Product("Orange", 3, 5, "D")
        ));
    }


    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Double calculateBasketCost(List<Product> products) {
        List<String> productCodes = products.stream()
                .map(Product::getProductCode)
                .distinct()
                .collect(Collectors.toList());
        List<Product> productsFromDB = productRepository.getProductsByProductCodeIsIn(productCodes);
        return productsFromDB.stream()
                .peek(product -> checkProductAvailable(products, product))
                .map(product -> costOfOnePurchase(product, products))
                .reduce(0.0, Double::sum);
    }

    private Double costOfOnePurchase(Product productFromDB, List<Product> productsFromClient) {
        Product productFromClient = getProductByCode(productsFromClient, productFromDB.getProductCode());
        Double finalPrice = productFromDB.getPricePerUnit() == 0.0 ? productFromDB.getPricePerSet()
                : productFromDB.getPricePerUnit();
        return productFromClient.getQuantity() * finalPrice;
    }

    private void checkProductAvailable(List<Product> clientListProduct, Product productFromDB) {
        Product clientProductByCode = getProductByCode(clientListProduct, productFromDB.getProductCode());
        checkProductAvailable(clientProductByCode, productFromDB);
    }

    private void checkProductAvailable(Product productFromClient, Product productFromDB) {
        if (productFromDB.getQuantity() < productFromClient.getQuantity()) {
            throw new ProductNotEnoughException("Not enough quantity");
        }
    }

    private Product getProductByCode(List<Product> products, String code) {
        return products.stream()
                .filter(product -> product.getProductCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Such product is not found"));
    }
}
