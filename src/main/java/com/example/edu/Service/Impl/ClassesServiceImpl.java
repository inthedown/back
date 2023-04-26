package com.example.edu.Service.Impl;

import com.example.edu.Entity.Classes;
import com.example.edu.Entity.User;
import com.example.edu.Form.ClassesDto;
import com.example.edu.Form.ClassesParam;
import com.example.edu.Repository.ClassesRepository;
import com.example.edu.Service.ClassesService;
import com.example.edu.Util.Util;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesRepository classesRepository;


    @Override
    public ResponseData add(ClassesDto classesDto) {

        Classes classes = classesRepository.findByClassName(classesDto.getClassName());
        if (classes != null) {
            return new ResponseData(ExceptionMsg.FAILED, "班级名已存在");
        }
        classesRepository.save(classesDto.toEntity());
        return new ResponseData(ExceptionMsg.SUCCESS, "添加成功");
    }

    @Override
    public ResponseData delete(int id) {
        classesRepository.deleteById(id);
        return new ResponseData(ExceptionMsg.SUCCESS, "删除成功");
    }

    @Override
    public ResponseData update(ClassesDto classesDto) {
        //更新
        int id = classesDto.getId();
        Optional<Classes> entity= classesRepository.findById(id);
        if(!entity.isPresent()){
            return new ResponseData(ExceptionMsg.FAILED, "班级不存在");
        }
        Classes classes = entity.get();
        try {
            Util.map(classesDto, classes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        classesRepository.save(classes);
        return new ResponseData(ExceptionMsg.SUCCESS, "更新成功");
    }

    @Override
    public ResponseData getDetail(int id) {
        Classes classes = classesRepository.findById(id).get();
        Util.initial(classes);
        return new ResponseData(ExceptionMsg.SUCCESS, classes);
    }

    @Override
    public ResponseData getList(ClassesParam classesParam, User user) {
        if(user.getRoleId()==2){
            throw new RuntimeException("没有权限");
        }
        int current =0;
        int size = 10;
        if (classesParam != null) {
            current =  classesParam.getCurrent()-1;
            size =  classesParam.getSize();
        }
        Pageable pageable = PageRequest.of(current, size);
        Page<Classes> page = classesRepository.findByParam(classesParam,pageable);
        List<Classes> list = page.getContent();
        List<Map<String,Object>> list1 = new ArrayList<>();
        list.forEach(classes -> {
            Map<String,Object> map = new HashMap<>();
            map.put("id",classes.getId());
            map.put("className",classes.getClassName());
            map.put("studentNum",classes.getStudents().size());
            map.put("courseNum",classes.getCourses().size());
            list1.add(map);
        });

        return new ResponseData(ExceptionMsg.SUCCESS, list);
    }
}
