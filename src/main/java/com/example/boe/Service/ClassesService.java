package com.example.boe.Service;

import com.example.boe.Form.ClassesDto;
import com.example.boe.result.ResponseData;

public interface ClassesService {
    ResponseData add(ClassesDto classesDto);

    ResponseData delete(int id);

    ResponseData update(ClassesDto classesDto);

    ResponseData getDetail(int id);
}
