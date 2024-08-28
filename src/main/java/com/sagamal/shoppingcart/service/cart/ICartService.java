package com.sagamal.shoppingcart.service.cart;

import com.sagamal.shoppingcart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ICartService{

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
}
