package com.sym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sym.entity.User;
import com.sym.mapper.UserMapper;
import com.sym.common.LoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username).eq(User::getDeleted,0);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误！");
        }
        //TODO 查询对应的权限信息,共有三个权限，admin普通管理员，superAdmin超管，common普通用户
        List<String> list = new ArrayList<>();
        list.remove("admin");
        list.remove("common");
        list.remove("superAdmin");
        if (user.getMarker()==1) {
            list.add("admin");
        }
        else if (user.getMarker()==2){
            list.add("superAdmin");
        }
        else {
            list.add("common");
        }
        //把数据封装成UserDetails返回
        return new LoginUser(user,list);
    }
}
