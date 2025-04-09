package com.biteup.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biteup.product_service.dto.ProductRequestDTO;
import com.biteup.product_service.dto.ProductResponseDTO;
import com.biteup.product_service.model.Product;
import com.biteup.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProducts(
        @RequestBody ProductRequestDTO req
    ) {
        ProductResponseDTO res = productService.createProducts(req);
        if(res.getError() != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    
}
