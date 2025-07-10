package com.hb.cda.thymeleafproject.repository;

import com.hb.cda.thymeleafproject.entity.*;
import org.springframework.data.jpa.repository.*;

public interface ProductRepository extends JpaRepository<Product, String> {
}
