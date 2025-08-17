package com.online.foodOrderingSystem.repository;

import com.online.foodOrderingSystem.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByOrderId(Long orderId);
    List<Feedback> findByOrderRestaurantId(Long restaurantId);
}
