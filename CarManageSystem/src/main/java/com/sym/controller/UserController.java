package com.sym.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.User;
import com.sym.service.impl.UserServiceImpl;

import com.sym.utils.JwtUtil;
import com.sym.vo.UserPageVo;
import com.sym.vo.UserVo;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    CacheManager cacheManager;
    private UserServiceImpl userService;
    @PostMapping("/userlogin")
    @ApiOperation("用户登录")
    public Result Login(@RequestBody User user) {
      return  userService.UserLogin(user);
    }

    @GetMapping("/exitlogin")
    public Result logout() {
        return userService.userLogout();
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    @ApiOperation("测试接口")
    public Result test() {
        return Result.success();
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result reg(@RequestBody User user) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set keys = redisTemplate.keys("userCache::PAGE*");
        redisTemplate.delete(keys);
        User finalUser = userService.UserRegister(user);
        userService.save(finalUser);
        return Result.success();
    }


    //TODO 前端记得对管理隐藏marker输入框，并且marker默认为0，就是管理员只能查用户，只有超管可以看到输入框并输入
    // (cache的key生成策略问题已解决)
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    @Cacheable(value = "userCache", key = "'PAGE'+#pageNum.toString()+#pageSize.toString()+" +
            "@serializeToString.serializeToStringByBase64(#user)")
    @PostMapping("/page")
    @ApiOperation("分页查询用户")
    @CrossOrigin
    public Result slecetPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestBody User user) {
        Page page = userService.selectPage(pageNum, pageSize, user);
        return Result.success(page,ResultCodeEnum.SELECT_SUCCESS);
    }

    // 删除用户，只有超管有权限，超管可以删除管理或普通用户
    //TODO 前端到时候记得只面向超管开放按钮的权限
    @PreAuthorize("hasAuthority('superAdmin')")
    @DeleteMapping
    @ApiOperation("删除用户")
    public Result deleteUser(@RequestParam Integer id){
        userService.removeById(id);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set keys = redisTemplate.keys("userCache::PAGE*");
        redisTemplate.delete(keys);
        return Result.success(ResultCodeEnum.SUCCESS);
    }

    //管理员修改普通用户信息
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    @PutMapping
    @ApiOperation("更新用户信息")
    public Result updateUser(@RequestBody User user){
        userService.updateCommonUser(user);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }


    //超管设置或取消管理员
    @PreAuthorize("hasAuthority('superAdmin')")
    @PutMapping("/setAdmin")
    @ApiOperation("超管设置或取消管理员")
    public Result setUserAdmin(@RequestBody User user) {
        userService.updateById(user);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set keys = redisTemplate.keys("userCache::PAGE*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    //解析token的接口,这里只返回marker跟id
    @GetMapping("/decodeToken")
    public Result getTokenInfo(HttpServletRequest request){
        UserVo uservo = userService.getInfoBtToken(request);
        return Result.success(uservo,ResultCodeEnum.SUCCESS);
    }

    @GetMapping
    public Result getMoreInfo(HttpServletRequest request) {
       UserPageVo userPageVo = userService.getMoreInfo(request);
       return Result.success(userPageVo,ResultCodeEnum.SELECT_SUCCESS);
    }


}
