package com.example.boe.Controller;

import com.example.boe.Form.ClassesDto;
import com.example.boe.Form.ClassesParam;
import com.example.boe.Service.ClassesService;
import com.example.boe.result.ResponseData;
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
