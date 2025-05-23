package com.biteup.product_service.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//database model
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "product")
public class Product {

  @Id
  private String id;

  private String name;
  private String description;
  private BigDecimal price;
  private String restaurantEmail;
  private String image;
  private String category;
  private String signedUrl;
}
