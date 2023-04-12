package com.example.edu.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {

    private String errMsg;

    private Integer errCode;

    public ServiceException(String errMsg) {
        this.errCode = 400;
        this.errMsg = errMsg;
    }
}
