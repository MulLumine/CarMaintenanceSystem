package com.sym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.Car;
import com.sym.entity.User;
import com.sym.mapper.CarMapper;
import com.sym.service.ICarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sym.utils.JwtUtil;
import com.sym.vo.CarAndUserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;

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
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Autowired
    private RedisTemplate redisTemplate;



    @Autowired
    private UserServiceImpl userService;

    @Override
    public List<Car> getOwnCar(HttpServletRequest request) {
        //取key
        Object secretKey = redisTemplate.opsForValue().get("secretKey");
        SecretKey realsecretKey = decodeKey((String) secretKey);
        //取token
        String token = request.getHeader("Authorization");
        Claims claims = JwtUtil.parseJWT(token,realsecretKey);
        //用户token中的id
        Integer tokenId = (Integer) claims.get("id");
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Car::getUserId,tokenId);
        List<Car> carList = this.list(queryWrapper);
        return carList;
    }
    @Override
    public Result deleteUserCar(Integer carId, HttpServletRequest request) {
        //拿到车辆对应的真实用户id
        Car userCar = this.getById(carId);
        Integer realUserId = userCar.getUserId();

        Set keys = redisTemplate.keys("carCache::PAGE*");
        redisTemplate.delete(keys);

        //查一下token头里的用户id对应的权限

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
        if (tokenId!=realUserId && marker!=2)
            return Result.fail(null,ResultCodeEnum.ROLE_ERROR);
        else
        {
            baseMapper.deleteById(carId);
            return Result.success(ResultCodeEnum.DELETE_SUCCESS);
        }
    }

    @Override
    public IPage CarUserPage(CarAndUserVo vo,IPage ipage,HttpServletRequest request) {

        Set keys = redisTemplate.keys("carCache::PAGE*");
        redisTemplate.delete(keys);
        //查一下token头里的用户id对应的权限

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
        IPage<CarAndUserVo> page;
        if (marker!=0) {
            page = baseMapper.selectPageVo(vo, ipage);
        }else
        {
            page = baseMapper.selectUserPageVo(tokenId, ipage);
        }
        return page;
    }

    @Override
    public Result updateInfo(Car car, HttpServletRequest request) {
        //拿到车辆对应的真实用户id
        Car userCar = this.getById(car.getId());
        Integer realUserId = userCar.getUserId();

        Set keys = redisTemplate.keys("carCache::PAGE*");
        redisTemplate.delete(keys);
        //查一下token头里的用户id对应的权限

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

        //如果不是车主又不是超级管理员
        if (tokenId!=realUserId && marker!=2)
            return Result.fail(null,ResultCodeEnum.ROLE_ERROR);
        else {
            baseMapper.updateById(car);
            return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
        }

    }
}
