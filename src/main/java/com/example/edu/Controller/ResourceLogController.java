package com.example.edu.Controller;

import com.example.edu.Form.UserResourceLogForm;
import com.example.edu.Service.ResourceLogService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("resourceLog")
public class ResourceLogController extends BaseController{
    @Autowired
    private ResourceLogService resourceLogService;

    @PostMapping( "/add")
    public ResponseData add(@RequestBody UserResourceLogForm logForm){
        resourceLogService.add(logForm,getUser());
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @PostMapping( "/count")
    public ResponseData count(@RequestParam("userId")Integer userId,@RequestParam("courseId") Integer courseId){
        return  resourceLogService.count(userId,courseId);
    }

}
