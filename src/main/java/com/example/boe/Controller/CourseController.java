package com.example.boe.Controller;

import com.example.boe.Form.CourseDto;
import com.example.boe.Form.UserInfoDto;
import com.example.boe.Service.CourseService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    //获取课程列表
    @RequestMapping("/getList")
    public ResponseData getCourseList(@RequestBody UserInfoDto userInfoDto){
        return courseService.getList(userInfoDto);
    }
    //获取课程详情
    @RequestMapping("/getDetail")
    public ResponseData getCourseDetail(int id){
        return courseService.getDetail(id);
    }
    //添加课程
    @RequestMapping("/add")
    public ResponseData addCourse(@RequestBody CourseDto courseDto){
        return courseService.add(courseDto);
    }
    //删除课程
    @RequestMapping("/delete/{couId}")
    public ResponseData deleteCourse(@PathVariable("couId") int id){
        return courseService.delete(id);
    }
    //修改课程
    @RequestMapping("/update")
    public ResponseData changeCourse(@RequestBody CourseDto courseDto){
        return courseService.update(courseDto);
    }
}
