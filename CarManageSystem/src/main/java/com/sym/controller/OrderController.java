package com.sym.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.Order;
import com.sym.service.impl.OrderServiceImpl;
import com.sym.vo.UserCarOderComVo;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private CacheManager cacheManager;
    private OrderServiceImpl orderService;
    private RedisTemplate redisTemplate;

    public OrderController(CacheManager cacheManager, OrderServiceImpl orderService, RedisTemplate redisTemplate) {
        this.cacheManager = cacheManager;
        this.orderService = orderService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/page")
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    @Cacheable(value = "orderCache",key = "'PAGE'+#pageNum.toString()+#pageSize.toString()+" +
            "@serializeToString.serializeToStringByBase64(#vo)")
    public Result getOrderByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestBody UserCarOderComVo vo, HttpServletRequest request) {
        IPage<UserCarOderComVo> firstPage = new Page<>(pageNum,pageSize);
        IPage<UserCarOderComVo> page = orderService.selectByPage(firstPage,vo,request);
        return Result.success(page, ResultCodeEnum.SELECT_SUCCESS);
    }

    //新增订单，管理和用户都可以新建订单，但其实都是作为用户身份建立订单
    @PostMapping("/{component}")
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result addNewOrder(@RequestBody Order order, @PathVariable List<Integer> component, HttpServletRequest request) {
       return orderService.addOrder(order,component,request);
    }


    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result cancelOrder(@RequestParam Integer id,HttpServletRequest request) {
       return orderService.userCancelOrder(id,request);
    }

    //用户是没有资格更新订单的,只能取消订单
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public Result updateOrder(@RequestBody Order order,@RequestParam List<Integer> comIds) {
        return orderService.updateOrderInfo(order,comIds);
    }


}
