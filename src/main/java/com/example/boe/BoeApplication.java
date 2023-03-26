package com.example.boe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class BoeApplication   {

    //	打包war需要这个启动类，发布到服务器上
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
//        System.out.print("视频上传项目启动类2.==="+"\n");
//        return application.sources(BoeApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(BoeApplication.class, args);
    }

}
