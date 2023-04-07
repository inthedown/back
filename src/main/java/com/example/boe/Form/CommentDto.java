package com.example.boe.Form;

import lombok.Data;

@Data
public class CommentDto {
    private Integer id;
    private Integer userToId;
    private String userToName;
    private String time;
    private Integer userFromId;
    private String userFromName;
    private Integer sessionId;
    private String content;
}
