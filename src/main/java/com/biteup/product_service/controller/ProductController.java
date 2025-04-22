package com.biteup.product_service.controller;

import com.biteup.product_service.dto.ProductRequestDTO;
import com.biteup.product_service.dto.ProductResponseDTO;
import com.biteup.product_service.model.Product;
import com.biteup.product_service.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProductResponseDTO> createProduct(
    @RequestPart("product") String productJson, // <-- Accept as String
    @RequestPart("image") MultipartFile image
  ) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    ProductRequestDTO req = objectMapper.readValue(
      productJson,
      ProductRequestDTO.class
    );

    ProductResponseDTO response = productService.createProducts(req, image);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/api/{email}")
  public List<Product> getAllProductsByEmail(@PathVariable String email) {
    return productService.getAllProductsByEmail(email);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable String id) {
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }
}
