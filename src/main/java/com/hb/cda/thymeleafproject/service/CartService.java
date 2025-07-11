package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.entity.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

import java.util.*;

/**
 * Service that manages operations regarding the list of {@link Product} in the <code>Cart</code>.
 * This service work in <code>session scope</code>, allowing each {@link User} to have their own
 * cart.
 */
@Service
@SessionScope
public class CartService {
  private final Set<Product> cartItems = new HashSet<>();

  // Get Cart items
  public List<Product> getCartItems() {
    return new ArrayList<>(cartItems);
  }

  // Add product to cart (unique: check if already exists in cart -> HashMap?)
  public void addToCart(Product product) {
    cartItems.add(product);
  }

  // Remove product from cart
  public void removeFromCart(Product product) {
    cartItems.removeIf(cartItem -> cartItem.getId().equals(product.getId()));
  }

  // Calculate cart total (nbr of products & total price)
  public Double getTotalPrice() {
    return cartItems.stream().mapToDouble(Product::getPrice).sum();
  }

  // Validate (buy) cart: empty cart & take product stocks into account
  public void validateCart() {
    // Check if all items still available (stock > 0)

    //
  }
}
