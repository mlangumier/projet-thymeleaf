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

  /**
   * Gets the list of items in the cart
   *
   * @return a list of items
   */
  public List<Product> getCartItems() {
    return new ArrayList<>(cartItems);
  }

  /**
   * Adds a product to the cart
   *
   * @param product product we want to add to the cart
   */
  public void addToCart(Product product) {
    // TODO: return error if product already exists in Cart
    cartItems.add(product);
  }

  /**
   * Removes a product from the cart
   *
   * @param product product we want to remove from the cart
   */
  public void removeFromCart(Product product) {
    cartItems.removeIf(cartItem -> cartItem.getId().equals(product.getId()));
  }

  /**
   * Calculates the total sum of the items currently in the cart
   *
   * @return The sum of the items in the cart
   */
  public Double getTotalPrice() {
    return cartItems.stream().mapToDouble(Product::getPrice).sum();
  }

  /**
   * Validates the "buy" action from the user, emptying the cart
   */
  public void validateCart() {
    cartItems.clear();
  }
}
