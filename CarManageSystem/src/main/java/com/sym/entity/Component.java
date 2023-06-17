package com.sym.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
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
@TableName("component")
@Repository
public class Component implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 零件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 零件库存
     */
    private Integer number;

    /**
     * 零件进货价格
     */
    private Double inprice;

    /**
     * 零件出售价格
     */
    private Double outprice;

    /**
     * 零件功能描述
     */
    private String description;

    /**
     * 逻辑删除（0为存在，1为删除）
     */
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public Integer getMaxInprice() {
        return maxInprice;
    }

    public void setMaxInprice(Integer maxInprice) {
        this.maxInprice = maxInprice;
    }

    public Integer getMaxOutprice() {
        return maxOutprice;
    }

    public void setMaxOutprice(Integer maxOutprice) {
        this.maxOutprice = maxOutprice;
    }

    @TableField(exist = false)
    private Integer maxInprice;

    @TableField(exist = false)
    private Integer maxOutprice;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    public Double getInprice() {
        return inprice;
    }

    public void setInprice(Double inprice) {
        this.inprice = inprice;
    }
    public Double getOutprice() {
        return outprice;
    }

    public void setOutprice(Double outprice) {
        this.outprice = outprice;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Component{" +
            "id=" + id +
            ", number=" + number +
            ", inprice=" + inprice +
            ", outprice=" + outprice +
            ", description=" + description +
            ", deleted=" + deleted +
            ", version=" + version +
        "}";
    }
}
