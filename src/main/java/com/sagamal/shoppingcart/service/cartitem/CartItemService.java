package com.sagamal.shoppingcart.service.cartitem;

import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Cart;
import com.sagamal.shoppingcart.model.CartItem;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.repository.cart.CartRepository;
import com.sagamal.shoppingcart.repository.cartitem.CartItemRepository;
import com.sagamal.shoppingcart.service.cart.ICartService;
import com.sagamal.shoppingcart.service.product.IProductService;
import com.sagamal.shoppingcart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //get the cart
        Cart cart = cartService.getCart(cartId);
        //get the product
        Product product = productService.getProductById(productId);
        //check if the product already exist in cart or not
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(new CartItem());
        //cartItemId will be null if it was not existing
        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);  //if product not in cart, create a new cartItem entry.
            cartItem.setUnitPrice(product.getPrice());
        }else{
            //if product exist in cart, we increase the previous quantity with the requested quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addCartItems(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItemToRemove = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Product not found in cart"));

        cart.r
    }
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

    }
}
