package com.sagamal.shoppingcart.service.category;

import com.sagamal.shoppingcart.exceptions.ResourceAlreadyExistException;
import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.model.ProductCategory;
import com.sagamal.shoppingcart.repository.category.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService implements IProductCategoryService{

    private static final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);
    private final ProductCategoryRepository categoryRepository;


    @Override
    public ProductCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public ProductCategory getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public ProductCategory addCategory(ProductCategory category) {
        return Optional.of(category)
                .filter(categoryOne -> !categoryRepository.existsByName(categoryOne.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new ResourceAlreadyExistException("Category "+ category.getName()+" already exist"));
    }

    @Override
    public ProductCategory updateCategory(ProductCategory category, Long id) {
        return Optional.ofNullable(this.getCategoryById(id))
                .map(oldCategory ->{
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));

    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
                ()-> {
                    log.info("Product with id ${id} does not exist in the database");
                    throw new ResourceNotFoundException("Category not found");
                });
    }
}
