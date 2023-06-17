package com.sym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("user")
@Repository
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户/管理员的id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0为用户，1为管理员
     */
    private Integer marker;

    /**
     * 登录的用户名
     */
    private String username;

    /**
     * 登录的密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户的电话号码
     */
    private String phone;

    /**
     * 用户的性别
     */
    private String gender;

    /**
     * 用户会员等级：0普通用户，1普通会员（90%），2高级会员(80%)
     */
    private String grade;

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
    public Integer getMarker() {
        return marker;
    }

    public void setMarker(Integer marker) {
        this.marker = marker;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", marker=" + marker +
            ", username=" + username +
            ", password=" + password +
            ", name=" + name +
            ", phone=" + phone +
            ", gender=" + gender +
            ", grade=" + grade +
            ", deleted=" + deleted +
        "}";
    }
}
