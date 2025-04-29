package com.biteup.product_service.service;

import com.biteup.product_service.dto.ProductRequestDTO;
import com.biteup.product_service.dto.ProductResponseDTO;
import com.biteup.product_service.model.Product;
import com.biteup.product_service.repository.ProductRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ImageUploaderService imageUploaderService;
  private final Storage storage;
  private final String bucketName;
  private final OkHttpClient httpClient = new OkHttpClient();

  public ProductService(
    ProductRepository productRepository,
    ImageUploaderService imageUploaderService,
    Storage storage,
    @Value("${gcp.bucket-name}") String bucketName
  ) {
    this.productRepository = productRepository;
    this.imageUploaderService = imageUploaderService;
    this.storage = storage;
    this.bucketName = bucketName;
  }

  public ProductResponseDTO createProducts(
    ProductRequestDTO req,
    MultipartFile imageFile
  ) {
    try {
      String imageUrl = imageUploaderService.uploadToGcs(bucketName, imageFile);

      Product product = new Product();
      product.setName(req.getName());
      product.setDescription(req.getDescription());
      product.setPrice(req.getPrice());
      product.setRestaurantEmail(req.getRestaurantEmail());
      product.setCategory(req.getCategory());
      product.setImage(imageUrl);

      Product saved = productRepository.save(product);
      log.info("Product created successfully: {}", saved);

      return new ProductResponseDTO("Product Saved Success", null);
    } catch (IOException e) {
      log.error("Image upload failed", e);
      return new ProductResponseDTO(null, "Image upload failed");
    }
  }

  public List<Product> getAllProducts() {
    List<Product> products = productRepository.findAll();
    for (Product product : products) {
      try {
        String objectName = product.getImage();
        String signedUrl = generateShortenedSignedUrl(objectName);
        product.setSignedUrl(signedUrl);
      } catch (Exception e) {
        log.error(
          "Error generating signed URL for image: {}",
          product.getImage(),
          e
        );
      }
    }
    return products;
  }

  public List<Product> getAllProductsByEmail(String email) {
    List<Product> products = productRepository.findByRestaurantEmail(email);
    for (Product product : products) {
      try {
        String objectName = product.getImage();
        String signedUrl = generateShortenedSignedUrl(objectName);
        product.setSignedUrl(signedUrl);
      } catch (Exception e) {
        log.error(
          "Error generating signed URL for image: {}",
          product.getImage(),
          e
        );
      }
    }
    return products;
  }

  public String generateShortenedSignedUrl(String objectName) throws Exception {
    Blob blob = storage.get(bucketName, objectName);
    if (blob == null) {
      throw new RuntimeException("File not found in GCS: " + objectName);
    }

    URL signedUrl = blob.signUrl(7, TimeUnit.DAYS);
    String longUrl = signedUrl.toString();

    // Step 2: Call TinyURL to shorten it
    String tinyUrlApi = "https://tinyurl.com/api-create.php?url=" + longUrl;
    Request request = new Request.Builder().url(tinyUrlApi).build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to shorten URL: " + response);
      }
      return response.body().string(); // This is the shortened URL
    }
  }

  public Product getProductById(String id) {
    Product product = productRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found for id: " + id)
      );

    try {
      String objectName = product.getImage();
      if (objectName != null && !objectName.isEmpty()) {
        String signedUrl = generateShortenedSignedUrl(objectName);
        product.setSignedUrl(signedUrl);
      }
    } catch (Exception e) {
      log.error(
        "Error generating signed URL for image: {}",
        product.getImage(),
        e
      );
    }

    return product;
  }

  public List<Product> getProductByCategory(String category) {
    List<Product> products = productRepository.findByCategory(category);
    for (Product product : products) {
      try {
        String objectName = product.getImage();
        String signedUrl = generateShortenedSignedUrl(objectName);
        product.setSignedUrl(signedUrl);
      } catch (Exception e) {
        log.error(
          "Error generating signed URL for image: {}",
          product.getImage(),
          e
        );
      }
    }
    return products;
  }
}
