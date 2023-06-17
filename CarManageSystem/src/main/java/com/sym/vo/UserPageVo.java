package com.sym.vo;

import com.sym.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageVo implements Serializable {
    private static final long serialVersionUID = 1L;

    public UserPageVo(User user) {
        this.username = user.getUsername();
        this.id = user.getId();
        this.marker = user.getMarker();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.grade = user.getGrade();
    }

    public void setUserPageVo(User user,UserPageVo userPageVo) {
        userPageVo.setUsername(user.getUsername());
        userPageVo.setId(user.getId());
        userPageVo.setMarker(user.getMarker());
        userPageVo.setName(user.getName());
        userPageVo.setPhone(user.getPhone());
        userPageVo.setGender(user.getGender());
        userPageVo.setGrade(user.getGrade());
    }

    private String username;
    /**
     * 用户/管理员的id
     */
    private Integer id;

    /**
     * 0为用户，1为管理员
     */
    private Integer marker;
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

}
