package com.sym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.*;
import com.sym.mapper.OrderMapper;
import com.sym.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sym.utils.JwtUtil;
import com.sym.vo.UserCarOderComVo;
import com.sym.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.sym.utils.JwtUtil.decodeKey;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private ComponentServiceImpl componentService;

    @Autowired
    private OrderComponentServiceImpl orderComponentService;

    public void ClearRedisCache() {
        Set keys = redisTemplate.keys("orderCache::PAGE*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
    public UserVo RuleCheck(HttpServletRequest request) {
        //取key
        Object secretKey = redisTemplate.opsForValue().get("secretKey");
        SecretKey realsecretKey = decodeKey((String) secretKey);
        //取token
        String token = request.getHeader("Authorization");
        Claims claims = JwtUtil.parseJWT(token,realsecretKey);
        //用户token中的id
        Integer tokenId = (Integer) claims.get("id");
        User byId = userService.getById(tokenId);
        Integer marker = byId.getMarker();
        UserVo userVo = new UserVo(tokenId,marker);
        return userVo;
    }

    @Override
    public IPage<UserCarOderComVo> selectByPage(IPage page, UserCarOderComVo vo, HttpServletRequest request) {

        //用户token头中的marker
        UserVo tokenUser = this.RuleCheck(request);
        Integer marker = tokenUser.getMarker();
        Integer id = tokenUser.getId();
        IPage<UserCarOderComVo> thePage;
        //判断权限
        if (marker!=0) {  //如果是管理直接随便查
            thePage = baseMapper.searchAllVoByPage(vo, page);
        }else {  //如果是用户那只能查自己的
            thePage = baseMapper.searchUserOrderByPage(page, id);
        }
        List<UserCarOderComVo> records = thePage.getRecords();
        records.stream().forEach(item -> {
            Integer orderId = item.getId();
            List<Component> components = baseMapper.searchComListByOrderId(orderId);
            item.setComponentList(components);
        });
        thePage.setRecords(records);
        return thePage;
    }

    @Override
    public Result addOrder(Order order,List<Integer> component, HttpServletRequest request) {

        this.ClearRedisCache();
        double count = 1d;
        double num = 0d;
        //查询token用户有的车
        UserVo userVo = this.RuleCheck(request);
        AtomicBoolean mid = new AtomicBoolean(false);
        Integer userVoId = userVo.getId();
        Integer marker = userVo.getMarker();

        //查一下用户的会员等级,计算折扣
        User byId = userService.getById(userVoId);
        String grade = byId.getGrade();
        if (Integer.parseInt(grade)==2)
            count = 0.8;
        else if (Integer.parseInt(grade)==1)
            count = 0.9;

        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Car::getUserId,userVoId);
        List<Car> carList = carService.list(queryWrapper);
        carList.stream().forEach(item->{
           if( item.getId().intValue() == order.getCarId().intValue() || marker==1 || marker==2)
               mid.set(true);
        });
        if (mid.get()) {
            List<OrderComponent> components = new ArrayList<>();
            //拿到车订单要的零件列表
            List<Component> needComponents = componentService.listByIds(component);
            List<Double> MoneyList = needComponents.stream().map(Component::getOutprice).collect(Collectors.toList());
            for (Double i : MoneyList)
            {
                num = num + i;
            }
            order.setPrice(num);
            this.save(order);
            Integer orderId = order.getId();
            component.stream().forEach(item -> {
                components.add(new OrderComponent(orderId,item));
            });
            orderComponentService.saveBatch(components);
            return Result.success();
        }
        return Result.build(201,"您没有权限改他人车的订单！");
    }

    @Override
    public Result userCancelOrder(Integer id, HttpServletRequest request) {
        this.ClearRedisCache();
        UserVo userVo = this.RuleCheck(request);
        Integer userId = userVo.getId();
        Integer marker = userVo.getMarker();

        //查询订单所属的用户id
        Order order = this.getById(id);
        Car car = carService.getById(order.getCarId());
        //如果用户非法，返回错误
        if (car.getUserId()!=userId && marker==0)
            return Result.fail(null ,ResultCodeEnum.ROLE_ERROR);
        //否则
        //删除订单表数据
        this.removeById(id);
        LambdaQueryWrapper<OrderComponent> queryWrapper = new LambdaQueryWrapper<>();

        //删除关系表数据
        queryWrapper.eq(OrderComponent::getOrderId,id);
        orderComponentService.remove(queryWrapper);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);

    }

    @Override
    public Result updateOrderInfo(Order order, List<Integer> comIds) {
        this.ClearRedisCache();
        //把订单表的部分信息更新了
        this.updateById(order);

        //如果对应的零件列表改了，把关系表的信息删掉
        LambdaQueryWrapper<OrderComponent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComponent::getOrderId,order.getId());
        List<OrderComponent> componentsList = orderComponentService.list(queryWrapper);

        //查出原先该订单使用的零件的id列表
        List<Integer> collect = componentsList.stream().map(OrderComponent::getComId).collect(Collectors.toList());

        //如果两个列表一样，则不更新关系表
        if(collect.equals(comIds))
            return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
        //否则先删除原先的关系表信息，然后插入新的零件信息
        orderComponentService.remove(queryWrapper);
        List<OrderComponent> list = new ArrayList<>();
        comIds.stream().forEach(i -> {
            list.add(new OrderComponent(order.getId(),i));
        });
        orderComponentService.saveBatch(list);
        //再根据新的conIds更新
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }
}
