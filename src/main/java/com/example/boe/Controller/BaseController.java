package com.example.boe.Controller;

import com.example.boe.Entity.User;
import com.example.boe.Util.BaseContextHandler;
import com.example.boe.result.ServiceException;

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
