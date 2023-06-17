package com.sym.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.entity.Component;
import com.sym.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sym.vo.UserCarOderComVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    //查用户自己的订单（用户版本）
    IPage<UserCarOderComVo> searchUserOrderByPage(@Param("page") IPage page,@Param("id") Integer id);

    //先查基本的订单(管理员版)
    IPage<UserCarOderComVo> searchAllVoByPage(@Param("vo") UserCarOderComVo vo,@Param("page") IPage page);

    //然后根据上面查到的page里面的list集合里的订单id列表遍历，查每个订单id对应的零件集合
    List<Component> searchComListByOrderId(@Param("id") Integer id);
}
