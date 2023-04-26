package com.example.edu.Service;

import com.example.edu.Entity.User;
import com.example.edu.Form.UserResourceLogForm;
import com.example.edu.result.ResponseData;

public interface ResourceLogService {
    void add(UserResourceLogForm logForm, User user);


    ResponseData count(Integer userId, Integer courseId);
}
