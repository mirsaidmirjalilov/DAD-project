package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    Page<Restaurant> findAll(Specification<Restaurant> specification, Pageable pageable);
}
