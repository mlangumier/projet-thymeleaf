package com.hb.cda.thymeleafproject.controller;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.*;
import com.hb.cda.thymeleafproject.service.*;
import jakarta.persistence.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
  private final ProductRepository productRepository;
  private final CartService cartService;

  public CartController(ProductRepository productRepository, CartService cartService) {
    this.productRepository = productRepository;
    this.cartService = cartService;
  }

  /**
   * Displays the <code>cart</code> page with the list of {@link Product} currently in the cart and
   * the total sum of their price.
   *
   * @param model Data model for the view
   * @return the <code>cart</code> view
   */
  @GetMapping
  public String displayCart(Model model) {
    model.addAttribute("cartItems", cartService.getCartItems());
    model.addAttribute("totalPrice", cartService.getTotalPrice());

    return "cart";
  }

  /**
   * Adds the {@link Product} to the <code>cart</code> after checking if the <code>ID</code> is
   * valid
   *
   * @param id    Identifier of the <code>product</code> to add to the cart
   * @param model Data model for the view
   * @return a redirection to the <code>index</code> view
   */
  @PostMapping("/add/{id}")
  public String addProductToCart(@PathVariable("id") String id, Model model) {
    Product product = findProductOrThrow(id);
    cartService.addToCart(product);
    //TODO: check if redirect or return void?

    return "redirect:/";
  }

  /**
   * Deletes a {@link Product} from the <code>cart</code>
   *
   * @param id
   * @return
   */
  @PostMapping("/remove/{id}")
  public String removeCartItem(@PathVariable("id") String id) {
    Product product = findProductOrThrow(id);
    cartService.removeFromCart(product);

    return "redirect:/cart";
  }

  //TODO: JavaDoc
  @PostMapping("/validate")
  public String validateCart() {
    try {
      //TODO: Check if all items still available (stock > 0)

      //TODO: Update product.stock on all items

      cartService.validateCart();

      return "redirect:/cart";
    } catch (Exception e) {
      //TODO: handle errors -> couldn't update items's stock ; items not longer available
      throw new RuntimeException(e);
    }
  }

  /**
   * Finds the {@link Product} corresponding to the given <code>ID</code> and either returns it or
   * throws an error
   *
   * @param id Identifier of the product we're looking for
   * @return either the found <code>product</code> or a <code>404 - Not Found</code> error
   */
  private Product findProductOrThrow(String id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("> Could not find Product of ID: " + id));
  }
}
