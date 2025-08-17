package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.entity.Feedback;
import com.online.foodOrderingSystem.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void leaveFeedback_SavesAndReturnsFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.leaveFeedback(feedback);

        assertEquals(1L, result.getId());
    }

    @Test
    void getFeedbackForRestaurant_ReturnsList() {
        Feedback fb = new Feedback();
        fb.setId(1L);
        List<Feedback> list = List.of(fb);

        when(feedbackRepository.findByOrderRestaurantId(10L)).thenReturn(list);

        List<Feedback> result = feedbackService.getFeedbackForRestaurant(10L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}
