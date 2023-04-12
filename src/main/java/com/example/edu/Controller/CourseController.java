package com.example.edu.Controller;

import com.example.edu.Form.CourseDto;
import com.example.edu.Form.UserInfoDto;
import com.example.edu.Service.CourseService;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("course")
public class CourseController extends BaseController {

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
    @RequestMapping("/delete")
    public ResponseData deleteCourse(@RequestParam("courseId") List<Integer> courseIds){
        return courseService.delete(courseIds);
    }
    //修改课程
    @RequestMapping("/update")
    public ResponseData changeCourse(@RequestBody CourseDto courseDto){
        return courseService.update(courseDto);
    }

    //获取session列表
    @RequestMapping("/getSessionList")
    public ResponseData getSessionList(int id){
        return courseService.getSessionList(id);
    }

    @RequestMapping("/importCou")
    public ResponseData importCou(@RequestParam("courseId") List<Integer> courseIds, @RequestParam("classId")int classId){
        return courseService.importCou(courseIds,classId);
    }
}
