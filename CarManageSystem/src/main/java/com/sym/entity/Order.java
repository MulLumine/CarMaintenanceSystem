package com.sym.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@TableName("theorder")
@Repository
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车辆id
     */
    @TableField(value = "carId")
    private Integer carId;

    /**
     * 车辆情况
     */
    private String situation;

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

    /**
     * 逻辑删除（0为存在，1为删除）
     */
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }
    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", carId=" + carId +
            ", situation=" + situation +
            ", time=" + time +
            ", status=" + status +
            ", price=" + price +
            ", deleted=" + deleted +
            ", version=" + version +
        "}";
    }
}
