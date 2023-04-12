package com.example.edu.Controller;

import com.example.edu.Form.ClassesDto;
import com.example.edu.Form.ClassesParam;
import com.example.edu.Service.ClassesService;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("classes")
public class ClassesController extends BaseController{
    @Autowired
    private ClassesService classesService;
    //CRUD
    @PostMapping("/add")
    public ResponseData add(@RequestBody ClassesDto classesDto){
        return classesService.add(classesDto);
    }
    @RequestMapping("/delete")
    public ResponseData delete(int id){
        return classesService.delete(id);
    }
    @PostMapping("/update")
    public ResponseData update(@RequestBody ClassesDto classesDto){
        return classesService.update(classesDto);
    }
    @PostMapping("/getDetail")
    public ResponseData getDetail( int id){
        return classesService.getDetail(id);
    }
    @GetMapping("/getList")
    public ResponseData getList(@RequestBody @Nullable ClassesParam classesParam){
        return classesService.getList(classesParam,getUser());
    }
}
