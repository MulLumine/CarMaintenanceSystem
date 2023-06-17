package com.sym.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarAndUserVo implements Serializable {

    private Integer id;

    /**
     * 车主id
     */
    private Integer userId;

    /**
     * 车辆型号
     */
    private String type;

    /**
     * 车辆描述
     */
    private String description;

    /**
     * 车牌号
     */
    private String code;

    /**
     * 车辆颜色
     */
    private String color;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户的电话号码
     */
    private String phone;

    /**
     * 用户会员等级：0普通用户，1普通会员（90%），2高级会员(80%)
     */
    private String grade;
}
