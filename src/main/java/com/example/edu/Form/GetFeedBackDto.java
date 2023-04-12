package com.example.edu.Form;

import lombok.Data;

@Data
public class GetFeedBackDto {
    private Integer courseId;
    private Integer sessionId;
    private Integer userId;
    private String type;
}
