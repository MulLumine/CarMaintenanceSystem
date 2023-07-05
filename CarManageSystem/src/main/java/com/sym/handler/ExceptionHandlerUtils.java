package com.sym.handler;

import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.exception.ServiceException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
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



    //登录异常
    @ExceptionHandler(BadCredentialsException.class)
    public Result UserOrNameERROR(BadCredentialsException e){
        e.printStackTrace();
        return Result.fail(ResultCodeEnum.LOGIN_ERROR);
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result NoUserError(InternalAuthenticationServiceException e){
        e.printStackTrace();
        return Result.fail(ResultCodeEnum.LOGIN_ERROR);
    }

    //权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result AccessERROR(AccessDeniedException e){
        e.printStackTrace();
        return Result.fail(ResultCodeEnum.ROLE_ERROR);
    }
}
