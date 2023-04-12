package com.example.edu.Form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {

    @NotNull
    private String userName;

    @NotNull
    private String password;
}
