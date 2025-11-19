package com.livemart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {
    private String id;
    private String userId;
    private String userName;
    private String productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
