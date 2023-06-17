package com.sym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@TableName("order_component")
@Repository
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    public OrderComponent(Integer orderId, Integer comId) {
        this.orderId = orderId;
        this.comId = comId;
    }

    /**
     * 关系表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    @TableField(value = "orderId")
    private Integer orderId;

    /**
     * 零件id
     */
    @TableField(value = "comId")
    private Integer comId;

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
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "OrderComponent{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", comId=" + comId +
            ", deleted=" + deleted +
        "}";
    }
}
