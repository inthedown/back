package com.example.boe.Service;

import com.example.boe.Form.CourseDto;
import com.example.boe.Form.UserInfoDto;
import com.example.boe.result.ResponseData;

public interface CourseService {
    ResponseData getList(UserInfoDto userInfoDto);

    ResponseData getDetail(int id);

    ResponseData add(CourseDto courseDto);

    ResponseData delete(int id);

    ResponseData update(CourseDto courseDto);

    ResponseData getSessionList(int id);
}
