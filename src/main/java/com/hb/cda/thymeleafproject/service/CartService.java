package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that manages operations regarding the list of {@link Product} in the <code>Cart</code>.
 * This service work in <code>session scope</code>, allowing each {@link User} to have their own
 * cart.
 */
@Service
@SessionScope
public class CartService {
  private final Set<Product> cartItems = new HashSet<>();
  private final ProductRepository productRepository;

  public CartService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

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
  public boolean addToCart(Product product) {
    if (cartItems.contains(product) || product.getStock() < 1) {
      return false;
    } else {
      cartItems.add(product);
      return true;
    }
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
   *
   * @return whether the transaction has been successful or not
   */
  @Transactional
  public boolean validateCart() {
    if (cartItems.isEmpty()) return false;

    List<Product> products = cartItems
        .stream()
        .map(product -> productRepository
            .findById(product.getId())
            .orElseThrow(() -> new IllegalArgumentException("> Product not found: " + product.getId())))
        .toList();

    for (Product product : products) {
      if (product.getStock() < 1) {
        return false;
      }
    }

    for (Product product : products) {
      product.setStock(product.getStock() - 1);
      productRepository.save(product);
    }

    cartItems.clear();
    return true;
  }
}
