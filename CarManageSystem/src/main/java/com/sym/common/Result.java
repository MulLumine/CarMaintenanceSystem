package com.sym.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public  class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    private Result(){}

    public Result(Integer code, String msg) {
        this.code = code;
        this.message =msg;
    }

    //封装返回的数据
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        //封装数据
        if(body != null){
            result.setData(body);
        }
        //状态码
        result.setCode(resultCodeEnum.getCode());
        //相关返回数据
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //成功
    //成功但里面没有数据
    public static<T> Result<T> success(){
        return build(null, ResultCodeEnum.SUCCESS);
    }
    public static<T> Result<T> success(T data,ResultCodeEnum resultCodeEnum){
        return build(data,resultCodeEnum);
    }
    //成功里面有数据
    public static <T> Result<T> success(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }
    public static <T> Result<T> success(ResultCodeEnum resultCodeEnum){
        return build(null, resultCodeEnum);
    }
    //失败
    //失败里面没数据
    public static<T> Result<T> fail(){
        return build(null, ResultCodeEnum.ERROR);
    }
    public static<T> Result<T> fail(ResultCodeEnum dataError) { return build(null,dataError);}
    //失败里面有数据
    public static<T> Result<T> fail(T data){
        return build(data, ResultCodeEnum.ERROR);
    }

    public static<T> Result<T> fail(T data, ResultCodeEnum dataError) {
        return build(data,dataError);
    }

    public static<T> Result<T> build(Integer code,String msg) {
        return new Result<>(code,msg);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
