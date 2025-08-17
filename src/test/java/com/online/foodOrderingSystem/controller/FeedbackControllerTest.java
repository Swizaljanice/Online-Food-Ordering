package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.Feedback;
import com.online.foodOrderingSystem.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    @Test
    void leaveFeedback_ReturnsSavedFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackService.leaveFeedback(any(Feedback.class))).thenReturn(feedback);

        ResponseEntity<Feedback> response = feedbackController.leaveFeedback(feedback);

        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getFeedbackForRestaurant_ReturnsList() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackService.getFeedbackForRestaurant(10L)).thenReturn(List.of(feedback));

        ResponseEntity<List<Feedback>> response = feedbackController.getFeedbackForRestaurant(10L);

        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }
}
