package com.sym.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sym.vo.UserPageVo;
import com.sym.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
public interface IUserService extends IService<User> {

    Result UserLogin(User user);

    User UserRegister(User user);

    Result userLogout();

    Page selectPage(Integer pageNum, Integer pageSize, User user);

    void updateCommonUser(User user);

    UserVo getInfoBtToken(HttpServletRequest request);

    UserPageVo getMoreInfo(HttpServletRequest request);
}
