package com.example.boe.Service;

import com.example.boe.Form.CourseDto;
import com.example.boe.Form.UserInfoDto;
import com.example.boe.result.ResponseData;

import java.util.List;

public interface CourseService {
    ResponseData getList(UserInfoDto userInfoDto);

    ResponseData getDetail(int id);

    ResponseData add(CourseDto courseDto);

    ResponseData delete(List<Integer> ids);

    ResponseData update(CourseDto courseDto);

    ResponseData getSessionList(int id);

    ResponseData importCou(List<Integer> courseIds, int classId);
}
