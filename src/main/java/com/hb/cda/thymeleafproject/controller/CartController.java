package com.hb.cda.thymeleafproject.controller;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.*;
import com.hb.cda.thymeleafproject.service.*;
import jakarta.persistence.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


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
   * @param id                 Identifier of the <code>product</code> to add to the cart
   * @param redirectAttributes Object that allow us to send data to the next redirect to be
   *                           displayed in the view
   * @return a redirection to the <code>index</code> view
   */
  @PostMapping("/add/{id}")
  public String addProductToCart(
      @PathVariable("id") String id,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size,
      RedirectAttributes redirectAttributes
      ) {
    Product product = findProductOrThrow(id);

    boolean success = cartService.addToCart(product);

    if (!success) {
      redirectAttributes.addFlashAttribute("error", "Product already is the cart");
    } else {
      redirectAttributes.addFlashAttribute(
          "message",
          "'" + product.getName() + "' added to the cart"
      );
    }

    System.err.println("> Params: page=" + page + ", size=" + size);
    int currentPage = page.orElse(1);
    int currentSize = size.orElse(4);

    return "redirect:/?page=" + currentPage + "&size=" + currentSize;
  }

  /**
   * Deletes a {@link Product} from the <code>cart</code>.
   *
   * @param id Identifier of the <code>product</code> we want to remove
   * @return a redirection to the <code>cart</code> view
   */
  @PostMapping("/remove/{id}")
  public String removeCartItem(@PathVariable("id") String id) {
    Product product = findProductOrThrow(id);
    cartService.removeFromCart(product);

    return "redirect:/cart";
  }

  /**
   * Validates the <code>cart</code> by simulating a "buy" action: verifies that the items are still
   * available, reduces their stock, then empties the cart.
   *
   * @param redirectAttributes Object that allow us to send data to the next redirect to be
   *                           displayed in the view
   * @return a redirection to the new empty cart
   */
  @PostMapping("/validate")
  public String validateCart(RedirectAttributes redirectAttributes) {
    boolean transactionSuccess = cartService.validateCart();

    if (!transactionSuccess) {
      redirectAttributes.addAttribute(
          "error",
          "Failed to validate cart: one or more items are out of stock"
      );
      return "cart";
    } else {
      redirectAttributes.addFlashAttribute(
          "message",
          "Transaction successful! Your cart has been validated"
      );
      return "redirect:/";
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
