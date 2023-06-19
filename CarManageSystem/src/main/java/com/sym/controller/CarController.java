package com.sym.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.Car;
import com.sym.entity.User;
import com.sym.service.impl.CarServiceImpl;
import com.sym.service.impl.UserServiceImpl;
import com.sym.utils.JwtUtil;
import com.sym.vo.CarAndUserVo;
import com.sym.vo.UserVo;
import io.jsonwebtoken.Claims;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

import static com.sym.utils.JwtUtil.decodeKey;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/cars")
public class CarController {


    private RedisTemplate redisTemplate;
    private CacheManager cacheManager;
    private CarServiceImpl carService;
    private UserServiceImpl userService;

    public CarController(RedisTemplate redisTemplate, CacheManager cacheManager, CarServiceImpl carService, UserServiceImpl userService) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;
        this.carService = carService;
        this.userService = userService;
    }

    //用户新增自己的车辆(所有人都可以用这个接口，这个接口是面向已登录用户的，所以也能新建自己的车)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result addCars(@RequestBody Car car,HttpServletRequest request) {
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
        car.setUserId(userVo.getId());

        Set keys = redisTemplate.keys("carCache::PAGE*");
        redisTemplate.delete(keys);
        carService.save(car);
        return Result.success(ResultCodeEnum.SUCCESS);
    }

    //用户删除自己的车辆 (这里内部只有用户和超管可以改，对管理开放只是管理也可以是用户)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result deleteCars(@RequestParam Integer carId, HttpServletRequest request) {
        return carService.deleteUserCar(carId,request);
    }

    //用户更新自己车辆的信息 (这里内部只有用户和超管可以改，对管理开放只是管理也可以是用户)
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result updateCars(@RequestBody Car car,HttpServletRequest request){
        return carService.updateInfo(car,request);
    }

    /**
     * 以上为用户部分操作
     * -----------------
     * 以下的部分为管理员操作
     */

    @PostMapping("/page")
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    @Cacheable(value = "carCache" , key = "'PAGE'+#pageNum.toString()+#pageSize.toString()+" +
            "@serializeToString.serializeToStringByBase64(#vo)")
    public Result selectByPage(@RequestBody CarAndUserVo vo,@RequestParam Integer pageNum,@RequestParam Integer pageSize,HttpServletRequest request) {
        IPage<CarAndUserVo> firstPage = new Page(pageNum,pageSize);
        IPage iPage = carService.CarUserPage(vo,firstPage,request);
        return Result.success(iPage,ResultCodeEnum.SELECT_SUCCESS);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result selectUserOwnCar(HttpServletRequest request) {
        List<Car> carList = carService.getOwnCar(request);
        return Result.success(carList,ResultCodeEnum.SELECT_SUCCESS);
    }

    @GetMapping("/ownpage")
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result getOwnPageCar(HttpServletRequest request,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
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
        IPage<Car> page = new Page<>(pageNum,pageSize);
        IPage<Car> finalPage = carService.page(page, queryWrapper);
        return Result.success(finalPage,ResultCodeEnum.SELECT_SUCCESS);
    }
}
