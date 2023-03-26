package com.example.boe.Service.Impl;

import com.example.boe.Entity.Classes;
import com.example.boe.Form.ClassesDto;
import com.example.boe.Repository.ClassesRepository;
import com.example.boe.Service.ClassesService;
import com.example.boe.Util.Util;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesRepository classesRepository;


    @Override
    public ResponseData add(ClassesDto classesDto) {
        //JPA查询是否有重复accountName
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

        return new ResponseData(ExceptionMsg.SUCCESS, classesRepository.findById(id).get());
    }
}
