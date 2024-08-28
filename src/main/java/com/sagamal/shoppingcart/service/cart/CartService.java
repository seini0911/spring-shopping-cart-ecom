package com.sagamal.shoppingcart.service.cart;


import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Cart;
import com.sagamal.shoppingcart.model.CartItem;
import com.sagamal.shoppingcart.repository.cart.CartRepository;
import com.sagamal.shoppingcart.repository.cartitem.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        //clear all the items in the cart
        cartItemRepository.deleteAllByCartId(id);
        //clear the cart items
        cart.getCartItems().clear();
        cartRepository.deleteById(id);

    }
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart  = getCart(id);
        return cart.getTotalAmount();
    }
}
