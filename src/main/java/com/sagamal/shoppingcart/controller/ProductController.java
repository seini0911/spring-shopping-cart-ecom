package com.sagamal.shoppingcart.controller;

import com.sagamal.shoppingcart.dto.ProductDto;
import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.request.AddProductRequest;
import com.sagamal.shoppingcart.request.ProductUpdateRequest;
import com.sagamal.shoppingcart.response.ApiResponse;
import com.sagamal.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productsDto = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products Found", productsDto));
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try{
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("Found", productDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PostMapping("/")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try{
            Product product1 = productService.addProduct(product);
            ProductDto productDto = productService.convertToProductDto(product1);
            return ResponseEntity.ok(new ApiResponse("Product added", productDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @RequestBody ProductUpdateRequest request,
            @PathVariable Long productId){
        try{
            Product product = productService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Product updated", product));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId){
        try{
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/by/brand/and/name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name){
        try{
            List<Product> products = productService.getProductsByBrandAndName(brand,name);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/by/category/and/brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(
            @RequestParam String category,
            @RequestParam String brand){
        try{
            List<Product> products = productService.getProductsByCategoryAndBrand(category,brand);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(
            @PathVariable String name){
        try{
            List<Product> products = productService.getProductsByName(name);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse> getProductsByBrand(
            @PathVariable String brand){
        try{
            List<Product> products = productService.getProductsByBrand(brand);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(
            @PathVariable String category){
        try{
            List<Product> products = productService.getProductsByCategory(category);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/count/brand/{brand}/name/{name}")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(
            @PathVariable String brand,
            @PathVariable String name){
        try{
            Long numberOfProducts = productService.countProductsByBrandAndName(brand,name);
            if(numberOfProducts <= 0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("Number of products found", numberOfProducts));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
