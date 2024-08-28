package com.sagamal.shoppingcart.repository.category;

import com.sagamal.shoppingcart.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findByName(String name);

    boolean existsByName(String name);
}
