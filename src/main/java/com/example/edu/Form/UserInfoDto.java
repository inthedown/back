package com.example.edu.Form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfoDto {

        private final int id;
        private final String role;
        private final String name;
        private final String username;
        private final String password;
        private final String token;
        private final Timestamp createTime;

}
