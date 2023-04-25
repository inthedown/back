package com.example.edu;

import com.example.edu.result.TokenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoeApplicationTests {

    @Test
    void contextLoads() {
        throw new TokenException("登录错误");
    }

}
