package com.example.edu.handler;

import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import com.example.edu.result.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class BaseControllerAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ServiceException.class})
    public Object handleException(ServiceException e) throws Exception {
        log.error(e.getErrMsg());
        return new ResponseData(ExceptionMsg.FAILED,e.getErrMsg());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e) throws Exception {
        log.error("", e);
        return new ResponseData(ExceptionMsg.FAILED,"内部错误");
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({TokenException.class})
    public Object handleException(TokenException e) throws Exception {
        log.error(e.getMessage());
        return new ResponseData(ExceptionMsg.ERROR, e.getMessage());
    }

}
