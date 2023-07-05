package com.sym.advice;

import com.sym.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AroundAdvice {
    @Pointcut("execution(* com.sym.controller.*.*(..))")
    private void pt(){}

    //对controller层所有的接口进行异常的抓取
//    @Around("pt()")
    public Object AllAroundMethod(ProceedingJoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();//获取形参
        Object proceed;
        try {
            proceed = joinPoint.proceed(args);
        }
        catch (RuntimeException runtimeException){
            //catch两次，第一次抓运行异常返回Result
            /*proceed = Result.fail(ResultCodeEnum.APP_ERROR);*/
            runtimeException.printStackTrace();
            throw new ServiceException("服务异常");
        }
        catch (Throwable e) {
            //catch两次，第二次抓全部异常返回Result
            /*proceed = Result.fail(ResultCodeEnum.SYSTEM_ERROR);*/
            e.printStackTrace();
            throw new Exception("系统异常！");
        }
        return proceed;
    }
}
