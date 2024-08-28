package com.sagamal.shoppingcart.request;

import com.sagamal.shoppingcart.model.Image;
import com.sagamal.shoppingcart.model.ProductCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int quantity;
    private ProductCategory category;
    private List<Image> images;
}
