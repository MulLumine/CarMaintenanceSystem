package com.sym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.common.Result;
import com.sym.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sym.vo.UserCarOderComVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
public interface IOrderService extends IService<Order> {

    IPage<UserCarOderComVo> selectByPage(IPage page, UserCarOderComVo vo, HttpServletRequest request);

    Result addOrder(Order order, List<Integer> component, HttpServletRequest request);

    Result userCancelOrder(Integer id, HttpServletRequest request);


    Result updateOrderInfo(Order order, List<Integer> comIds);
}
