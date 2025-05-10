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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  //create product route
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

  //get all product route
  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  //get product by email route
  @GetMapping("/api/{email}")
  public List<Product> getAllProductsByEmail(@PathVariable String email) {
    return productService.getAllProductsByEmail(email);
  }

  //get product by id route
  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable String id) {
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

// product update route

@PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProductResponseDTO>updateProduct(
  @PathVariable String id,
  @RequestPart(value = "product", required = false) String productjson,
  @RequestPart(value = "image", required = false) MultipartFile image
)throws JsonProcessingException{
  ProductRequestDTO req=null;
  if(productjson!=null){
    ObjectMapper objectMapper=new ObjectMapper();
    req=objectMapper.readValue(productjson,ProductRequestDTO.class);
  }
  ProductResponseDTO response=productService.updateProduct(id,req,image);
  return ResponseEntity.ok(response);
}

// product delete route

 @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable String id) {
      ProductResponseDTO  response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }


  //filtered Categories API
  @GetMapping("/Burgers")
  public List<Product> getProductByBurgers() {
    String category = "Burgers";

    return productService.getProductByCategory(category);
  }

  //filter by pizza route
  @GetMapping("/Pizza")
  public List<Product> getProductByPizza() {
    String category = "Pizza";

    return productService.getProductByCategory(category);
  }

  //filter by Beverages route
  @GetMapping("/Beverages")
  public List<Product> getProductByBeverages() {
    String category = "Beverages";

    return productService.getProductByCategory(category);
  }

  //filter by Vegetarian route
  @GetMapping("/Vegetarian")
  public List<Product> getProductByVegetarian() {
    String category = "Vegetarian";

    return productService.getProductByCategory(category);
  }

  //filter by Others route
  @GetMapping("/Others")
  public List<Product> getProductByOthers() {
    String category = "Others";

    return productService.getProductByCategory(category);
  }
}
