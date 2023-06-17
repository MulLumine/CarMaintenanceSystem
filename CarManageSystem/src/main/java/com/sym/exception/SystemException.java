package com.sym.exception;

import com.sym.common.ResultCodeEnum;

public class SystemException {
    private Integer code;
    private String msg;

    public SystemException(String msg) {
        this.msg = msg;
    }

    public SystemException(Integer code) {
        this.code = code;
    }
    public SystemException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }
}
