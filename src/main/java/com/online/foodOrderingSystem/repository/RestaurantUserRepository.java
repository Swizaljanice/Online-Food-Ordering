package com.online.foodOrderingSystem.repository;

import com.online.foodOrderingSystem.entity.RestaurantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {
    Optional<RestaurantUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
