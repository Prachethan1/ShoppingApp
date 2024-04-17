package com.shoppingapp.shoppingapp.service;

import com.shoppingapp.shoppingapp.entity.Product;
import com.shoppingapp.shoppingapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product getProduct(int pid) {

   Product product =  productRepository.findById(pid).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));

        if (product.getAvailable() == 0) {
            product.setAvailable(100);
        }
        if (product.getPrice() == 0) {
            product.setPrice(100);
        }

        return product;
    }

    public Product add(Product product) {

        return productRepository.save(product);
    }
}
