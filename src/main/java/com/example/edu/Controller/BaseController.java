package com.example.edu.Controller;

import com.example.edu.Entity.User;
import com.example.edu.Util.BaseContextHandler;
import com.example.edu.result.ServiceException;

public class BaseController{
    public User getUser() {
        return BaseContextHandler.getUser();
    }

    public void ifAdmin() {
        if(getUser().getRoleId().intValue() != 1) {
            throw new ServiceException("无权限");
        }
    }
}
