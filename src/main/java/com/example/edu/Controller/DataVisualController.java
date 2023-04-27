package com.example.edu.Controller;

import com.example.edu.Service.VisualService;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("visual")
public class DataVisualController extends BaseController{
    @Autowired
    private VisualService visualService;

    @GetMapping("/getCourseStatus")
    public ResponseData upload(){
        return visualService.getStatus(getUser());
    }

    @GetMapping("/getStuActiveMap")
    public ResponseData getActiveMap(){
        return visualService.getActiveMap(getUser());
    }

    @GetMapping("/getMap")
    public ResponseData getMap(){
        return visualService.getMap(getUser());
    }
}
