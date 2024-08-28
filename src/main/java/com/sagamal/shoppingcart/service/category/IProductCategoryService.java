package com.sagamal.shoppingcart.service.category;

import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.model.ProductCategory;

import java.util.List;

public interface IProductCategoryService {

    ProductCategory getCategoryById(Long id);

    ProductCategory getCategoryByName(String name);

    List<ProductCategory> getAllCategories();

    ProductCategory addCategory(ProductCategory category);

    ProductCategory updateCategory(ProductCategory category, Long id);

    void deleteCategoryById(Long id);
}
