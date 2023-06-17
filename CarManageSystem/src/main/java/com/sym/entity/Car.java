package com.sym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@TableName("car")
@Repository
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车主id
     */
    @TableField(value = "userId")
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
     * 逻辑删除（0为存在，1为删除）
     */
    private Integer deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + id +
            ", userId=" + userId +
            ", type=" + type +
            ", description=" + description +
            ", code=" + code +
            ", color=" + color +
            ", deleted=" + deleted +
        "}";
    }
}
