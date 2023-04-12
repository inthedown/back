package com.example.edu.Form;

import lombok.Data;

@Data
public class AddFeedBackDto {
    private Integer sId;
    private String content;
    private Integer userFromId;
    private Integer userToId;

}
