package com.example.edu.handler;

import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {"com.example.edu.Controller1"},annotations = {RestController.class})
public class ExceptionHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        final String returnTypeName = returnType.getParameterType().getName();
        if(!request.getURI().toString().endsWith(".do") && !request.getURI().toString().endsWith("open/addSite") && MediaType.APPLICATION_JSON.equals(selectedContentType)) {
            if("com.example.edu.result.ResponseData".equals(returnTypeName)) {
                return body;
            } else {
                if("void".equals(returnTypeName)) {
                    return new ResponseData(ExceptionMsg.SUCCESS);
                }
                return new ResponseData(ExceptionMsg.SUCCESS,body);
            }
        }
        return body;
    }
}
