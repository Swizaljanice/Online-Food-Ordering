package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.entity.Feedback;
import com.online.foodOrderingSystem.repository.FeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public Feedback leaveFeedback(Feedback feedback) {
        log.info("Saving feedback for restaurantId={}, userId={}",
                feedback.getOrder() != null ? feedback.getOrder().getRestaurantId() : null,
                feedback.getUser() != null ? feedback.getUser().getId() : null);

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Feedback saved successfully with id={}", saved.getId());

        return saved;
    }

    public List<Feedback> getFeedbackForRestaurant(Long restaurantId) {
        log.info("Fetching feedback for restaurantId={}", restaurantId);

        List<Feedback> feedbackList = feedbackRepository.findByOrderRestaurantId(restaurantId);
        log.info("Retrieved {} feedback entries for restaurantId={}", feedbackList.size(), restaurantId);

        return feedbackList;
    }
}
