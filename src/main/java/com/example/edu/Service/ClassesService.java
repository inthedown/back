package com.example.edu.Service;

import com.example.edu.Entity.User;
import com.example.edu.Form.ClassesDto;
import com.example.edu.Form.ClassesParam;
import com.example.edu.result.ResponseData;

public interface ClassesService {
    ResponseData add(ClassesDto classesDto);

    ResponseData delete(int id);

    ResponseData update(ClassesDto classesDto);

    ResponseData getDetail(int id);

    ResponseData getList(ClassesParam classesParam, User user);
}
