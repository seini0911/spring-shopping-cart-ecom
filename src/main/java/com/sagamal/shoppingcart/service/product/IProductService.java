package com.sagamal.shoppingcart.service.product;

import com.sagamal.shoppingcart.dto.ProductDto;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.request.AddProductRequest;
import com.sagamal.shoppingcart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
    public ProductDto convertToProductDto(Product product);
    public List<ProductDto> getConvertedProducts(List<Product> products);
}
