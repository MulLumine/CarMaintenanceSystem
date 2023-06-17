package com.sym.handler;

import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.exception.ServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;

@ControllerAdvice
public class ExceptionHandlerUtils {
    @ExceptionHandler(SignatureException.class)
    public Result tokenEX(SignatureException signatureException){
        signatureException.printStackTrace();
        return Result.fail(ResultCodeEnum.UNLOGIN);
    }
    @ExceptionHandler(ServiceException.class)
    public Result ServiceEX(ServiceException exception){
        exception.printStackTrace();
        return  Result.fail(ResultCodeEnum.APP_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public Result GlobalEX(Exception exception){
        exception.printStackTrace();
        return  Result.fail(ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result UserOrNameERROR(InternalAuthenticationServiceException e){
        return Result.fail(ResultCodeEnum.LOGIN_ERROR);
    }
}
