package com.example.edu.Service;

import com.example.edu.Form.UserResourceLogForm;
import com.example.edu.result.ResponseData;

public interface ResourceLogService {
    void add(UserResourceLogForm logForm);


    ResponseData count(Integer userId, Integer courseId);
}
