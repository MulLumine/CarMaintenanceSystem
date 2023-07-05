package com.sym.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ResultCodeEnum {
    SUCCESS("操作成功！",200),
    ERROR("操作失败！",202),
    LOGIN_SUCCESS("登录成功！",200),
    LOGIN_ERROR("用户名或密码错误！",210),
    UNLOGIN("您尚未登录！",210),
    APP_ERROR("服务异常！",211),
    SYSTEM_ERROR("系统异常！",221),
    UPDATE_SUCCESS("更新成功！",200),
    DELETE_SUCCESS("删除成功！",200),
    SELECT_SUCCESS("查询成功！",200),
    ADD_SUCCESS("添加成功！",200),
    ROLE_ERROR("您没有权限！",210);

    private String message;
    private Integer code;

    ResultCodeEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
