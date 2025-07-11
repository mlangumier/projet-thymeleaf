package com.hb.cda.thymeleafproject.controller;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.*;
import com.hb.cda.thymeleafproject.service.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@Controller
public class ProductController {
  private final ProductRepository repository;
  private CartService cartService;

  public ProductController(ProductRepository repository, CartService cartService) {
    this.repository = repository;
    this.cartService = cartService;
  }

  @GetMapping("/")
  public String displayIndex(
      Model model,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "size", defaultValue = "3") int size
  ) {
    Page<Product> paginatedProducts = repository.findAll(PageRequest.of(
        page - 1,
        size,
        Sort.by(Sort.Direction.ASC, "id")
    ));
    model.addAttribute("paginatedProducts", paginatedProducts);
    model.addAttribute("currentPage", page);

    // Creates a list of page numbers to display for better UX
    int totalPages = paginatedProducts.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
      model.addAttribute("pageNumbers", pageNumbers);
    }

    return "index";
  }

  @PostMapping("/cart/add/{id}")
  public String addProductToCart(@PathVariable("id") String id, Model model) {
    Product product = repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Product not found: invalid product ID"));

    cartService.addToCart(product);

    return "redirect:/";
  }
}
