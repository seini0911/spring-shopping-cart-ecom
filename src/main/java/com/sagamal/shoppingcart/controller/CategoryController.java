package com.sagamal.shoppingcart.controller;

import com.sagamal.shoppingcart.exceptions.ResourceAlreadyExistException;
import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.ProductCategory;
import com.sagamal.shoppingcart.response.ApiResponse;
import com.sagamal.shoppingcart.service.category.IProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final IProductCategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<ProductCategory> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("All categories found", categories));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody ProductCategory category){
        try{
            ProductCategory category1= categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Category saved successfully", category1));
        }catch (ResourceAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }

    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId){
        try{
            ProductCategory category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found", category));

        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));

        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try{
            ProductCategory category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable Long categoryId){
        try{
             categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category deleted",null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
