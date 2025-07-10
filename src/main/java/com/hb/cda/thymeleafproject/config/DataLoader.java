package com.hb.cda.thymeleafproject.config;

import com.hb.cda.thymeleafproject.entity.*;
import com.hb.cda.thymeleafproject.repository.*;
import org.springframework.boot.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Component
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final PasswordEncoder encoder;

  public DataLoader(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) throws Exception {

    System.out.println("> Checking database for data...");

    if (userRepository.findAll().isEmpty()) {
      System.out.println("> No users found. Generating users...");
      userRepository.save(new User("user", encoder.encode("password"), "ROLE_USER"));
      userRepository.save(new User("matt", encoder.encode("matt@password"), "ROLE_USER"));
      userRepository.save(new User("sam", encoder.encode("sam@password"), "ROLE_USER"));
    } else {
      System.out.println("> Some users already exist. Data generation not needed.");
    }

    if (productRepository.findAll().isEmpty()) {
      System.out.println("> No products found. Generating products...");
      productRepository.save(new Product("Padded Backpack", 35.65, "It's a pretty cool backpack, very comfortable", 5));
      productRepository.save(new Product("Polarized Sunglasses", 18.20, "Stylish sunglasses for summer", 10));
      productRepository.save(new Product("WIFI Surveillance Camera", 18.55, "So you can check on your dog when you're at work", 2));
      productRepository.save(new Product("Noise Cancelling Headphones", 22.87, "Listen to music and forget everything around you", 4));
      productRepository.save(new Product("Car Cooler Box", 32.32, "Keep your food and drinks fresh during your holidays", 3));
      productRepository.save(new Product("Ceiling Fan", 42.85, "Cool your room with this wonderful ceiling fan", 2));
      productRepository.save(new Product("Reading Light", 12.23, "For everyone who love reading, but hate the sun", 15));
      productRepository.save(new Product("Laptop Cooler", 28.89, "Prevents your laptop from overheating", 3));
      productRepository.save(new Product("Car Phone Holder", 22.22, "Keep your phone in front of you at all time", 5));
      productRepository.save(new Product("Vacuum Cleaner", 165.50, "Best vacuum cleaner for every surface", 1));
    } else {
      System.out.println("> Some products already exist. Data generation not needed.");
    }

    System.out.println("> Finished populating database.");
  }
}
