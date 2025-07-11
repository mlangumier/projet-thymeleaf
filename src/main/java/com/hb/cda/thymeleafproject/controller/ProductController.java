package com.hb.cda.thymeleafproject.controller;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@Controller
public class ProductController {
  private final ProductRepository repository;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
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
}
