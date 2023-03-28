package com.example.boe.Controller;

import com.example.boe.result.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errMsg", e.getErrMsg());
        response.put("errCode", e.getErrCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
