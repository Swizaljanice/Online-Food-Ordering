package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.Feedback;
import com.online.foodOrderingSystem.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> leaveFeedback(@RequestBody Feedback feedback) {
        log.info("Received request to leave feedback: {}", feedback);

        Feedback saved = feedbackService.leaveFeedback(feedback);
        log.info("Feedback saved with ID: {}", saved.getId());

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/restaurants/{restaurantId}/feedback")
    public ResponseEntity<List<Feedback>> getFeedbackForRestaurant(@PathVariable Long restaurantId) {
        log.info("Fetching feedback for restaurantId={}", restaurantId);

        List<Feedback> feedbackList = feedbackService.getFeedbackForRestaurant(restaurantId);
        log.info("Found {} feedback entries for restaurantId={}", feedbackList.size(), restaurantId);

        return ResponseEntity.ok(feedbackList);
    }
}
