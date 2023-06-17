package com.sym.vo;

import com.sym.entity.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCarOderComVo implements Serializable {

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
    private Integer marker;


    /**
     * 车辆型号
     */
    private String type;

    /**
     * 车辆情况
     */
    private String situation;

    //所需零件
    private List<Component> componentList;

    /**
     * 订单的id
     * */
    private Integer id;
    /**
     * 订单创建日期
     */
    private Date time;

    /**
     * 订单状态（0未完成，1已完成）
     */
    private Integer status;

    /**
     * 订单总价
     */
    private Double price;


}
