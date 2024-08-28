package com.sagamal.shoppingcart.service.product;

import com.sagamal.shoppingcart.dto.ImageDto;
import com.sagamal.shoppingcart.dto.ProductDto;
import com.sagamal.shoppingcart.exceptions.ProductNotFoundException;
import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Image;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.model.ProductCategory;
import com.sagamal.shoppingcart.repository.category.ProductCategoryRepository;
import com.sagamal.shoppingcart.repository.image.ImageRepository;
import com.sagamal.shoppingcart.repository.product.ProductRepository;
import com.sagamal.shoppingcart.request.AddProductRequest;
import com.sagamal.shoppingcart.request.ProductUpdateRequest;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    @Override
    public Product addProduct(AddProductRequest product) {
        //if the category exist in the DB, set it to the product
        ProductCategory category = Optional.ofNullable(
                productCategoryRepository.findByName(
                        product.getCategory().getName()))
                .orElseGet(()->{
                    ProductCategory newCategory = new ProductCategory(product.getCategory().getName());
                    return productCategoryRepository.save(newCategory);
                });
        product.setCategory(category);
        //else save the category first and later on set it to the product
        return productRepository.save(createProduct(product, category));
    }
    private Product createProduct(AddProductRequest request, ProductCategory category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity(),
                category
        );
    }
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setQuantity(request.getQuantity());
        ProductCategory category  = productCategoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    }
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(
                        productRepository::delete,()->{
                            throw new ResourceNotFoundException("Product does not exist");
                        });
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }
    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
                .map(this::convertToProductDto).toList();
    }

    @Override
    public ProductDto convertToProductDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();

        productDto.setImages(imageDtos);
        return productDto;
    }
}
