package com.example.boe.result;

public class TokenException extends RuntimeException{

    public TokenException() {
        this("登录错误");
    }


    public TokenException(String message) {
        super(message);
    }
}
