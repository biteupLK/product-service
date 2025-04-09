package com.biteup.product_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.biteup.product_service.dto.ProductRequestDTO;
import com.biteup.product_service.dto.ProductResponseDTO;
import com.biteup.product_service.model.Product;
import com.biteup.product_service.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO createProducts(
            ProductRequestDTO req) {
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());

        Product saved = productRepository.save(product);
        log.info("Product Create Successfully");
        if (saved.getId() == null)
            return new ProductResponseDTO(null, "System Error");

        return new ProductResponseDTO("Product Saved Success", null);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
