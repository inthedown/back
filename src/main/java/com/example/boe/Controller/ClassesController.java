package com.example.boe.Controller;

import com.example.boe.Form.ClassesDto;
import com.example.boe.Form.ClassesParam;
import com.example.boe.Service.ClassesService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("classes")
public class ClassesController extends BaseController{
    @Autowired
    private ClassesService classesService;
    //CRUD
    @RequestMapping("/add")
    public ResponseData add(@RequestBody ClassesDto classesDto){
        return classesService.add(classesDto);
    }
    @RequestMapping("/delete")
    public ResponseData delete(int id){
        return classesService.delete(id);
    }
    @RequestMapping("/update")
    public ResponseData update(@RequestBody ClassesDto classesDto){
        return classesService.update(classesDto);
    }
    @RequestMapping("/getDetail")
    public ResponseData getDetail(int id){
        return classesService.getDetail(id);
    }
    @RequestMapping("/getList")
    public ResponseData getList(@RequestBody ClassesParam classesParam){
        return classesService.getList(classesParam,getUser());
    }
}
