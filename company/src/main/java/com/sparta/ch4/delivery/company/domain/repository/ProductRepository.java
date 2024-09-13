package com.sparta.ch4.delivery.company.domain.repository;

import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.infrastructure.repository.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom { }