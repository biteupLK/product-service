package com.biteup.product_service.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequestDTO {

  //reqesting data transfer object
  private String id;

  private String name;
  private String description;
  private BigDecimal price;
  private String restaurantEmail;
  private String category;
  private String image;
}
