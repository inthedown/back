package com.example.edu.Service;

import com.example.edu.Entity.User;
import com.example.edu.result.ResponseData;

public interface VisualService {
    ResponseData getStatus(User user);

    ResponseData getActiveMap(User user);

    ResponseData getMap(User user);

    ResponseData getDonutMap(int courseId, User user);
}
