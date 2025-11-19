package com.livemart.backend.dto.response;

import com.livemart.backend.entity.User.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String image;
    private UserRole role;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private Boolean emailVerified;
}
