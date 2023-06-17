package com.sym.exception;

import com.sym.common.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ServiceException extends RuntimeException{
   /* private Integer code;
    private String msg;

    public ServiceException(String msg) {
        super.getMessage();
    }

    public ServiceException(Integer code) {
        this.code = code;
    }
    public ServiceException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }*/

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }
}
