package com.online.foodOrderingSystem.repository;

import com.online.foodOrderingSystem.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {
}
