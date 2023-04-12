package com.example.edu.Service;

import com.example.edu.Form.CourseDto;
import com.example.edu.Form.UserInfoDto;
import com.example.edu.result.ResponseData;

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
