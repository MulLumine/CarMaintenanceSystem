package com.sym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.LoginUser;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.User;
import com.sym.mapper.UserMapper;
import com.sym.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sym.utils.JwtUtil;
import com.sym.vo.UserPageVo;
import com.sym.vo.UserVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sym.utils.JwtUtil.decodeKey;
import static io.jsonwebtoken.security.Keys.secretKeyFor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result UserLogin(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        /*LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username)
                .eq(User::getPassword,password);
        User TheLoginUser = baseMapper.selectOne(queryWrapper);
        if (TheLoginUser==null)
            return Result.fail(null,ResultCodeEnum.LOGIN_ERROR);*/
        //AuthenticationManager 进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证未通过，给出提示
        if(Objects.isNull(authenticate)) {
            throw new RuntimeException("登陆失败");
        }
        else {
            //认证通过，使用userId生成一个jwt，存入result
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            Integer id = loginUser.getUser().getId();
            Map idmap = new HashMap<>();
            idmap.put("id", id);
            /*SecretKey secretKey = secretKeyFor(SignatureAlgorithm.HS256);*/
/*            redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());*/
            SecretKey secretKey = JwtUtil.decodeKey((String) redisTemplate.opsForValue().get("secretKey"));
            String jwt = JwtUtil.createJWT(idmap, secretKey);
            //将密钥存入redis
            /*redisTemplate.opsForValue().set("secretKey", JwtUtil.encodeKey(secretKey), 3, TimeUnit.DAYS);*/
            //把完整的用户信息存入redis
            redisTemplate.opsForValue().set("User"+id, loginUser, 3, TimeUnit.DAYS);
            HashMap tokenMap = new HashMap<>();
            tokenMap.put("Authorization", jwt);
            return Result.success(tokenMap, ResultCodeEnum.LOGIN_SUCCESS);
        }
    }

    @Override
    public User UserRegister(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        user.setMarker(0);  //设置maker为0，也就是普通用户
        return user;
    }

    @Override //分页查询
    public Page selectPage(Integer pageNum, Integer pageSize, User user) {
        //1 创建一个分页对象，传递相关参数
        Page<User> PageParam = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(!user.getName().equals(""),User::getName,user.getName())
                .like(!user.getPhone().equals(""),User::getPhone,user.getPhone())
                .eq(!user.getGender().equals(""),User::getGender,user.getGender())
                .like(!user.getGrade().equals(""),User::getGrade,user.getGrade())
                .eq(user.getMarker()!=null,User::getMarker,user.getMarker());
        Page<User> page = this.page(PageParam, queryWrapper);
        List<User> users = page.getRecords();
        List<UserPageVo> userPagelist = new ArrayList<>();
        users.stream().forEach(item -> {
            UserPageVo userPageVo = new UserPageVo(item);
            userPagelist.add(userPageVo);
        });
        Page<UserPageVo> pageVoPage = new Page<>();
        pageVoPage.setRecords(userPagelist)
                .setCurrent(page.getCurrent())
                .setPages(page.getPages())
                .setSize(page.getSize())
                .setTotal(page.getTotal());
        return pageVoPage;
    }

    @Override
    public void updateCommonUser(User user) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set keys = redisTemplate.keys("userCache::PAGE*");
        redisTemplate.delete(keys);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMarker,0).eq(User::getId,user.getId());
        this.update(user,queryWrapper);
    }

    @Override
    public UserVo getInfoBtToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Object secretKey = redisTemplate.opsForValue().get("secretKey");
        SecretKey key = JwtUtil.decodeKey((String) secretKey);
        Claims claims = JwtUtil.parseJWT(token, key);
        //获取token中存的用户id
        Integer id = (Integer) claims.get("id");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        User one = this.getOne(queryWrapper);
        return new UserVo(one.getId(),one.getMarker());
    }

    @Override
    public UserPageVo getMoreInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Object secretKey = redisTemplate.opsForValue().get("secretKey");
        SecretKey key = JwtUtil.decodeKey((String) secretKey);
        Claims claims = JwtUtil.parseJWT(token, key);
        //获取token中存的用户id
        Integer id = (Integer) claims.get("id");
        User byId = this.getById(id);
        UserPageVo userPageVo = new UserPageVo(byId);
        return userPageVo;
    }

    @Override
    public Result userLogout() {
        //获取SecurityContextHolder的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        //删除redis中的值
        redisTemplate.delete("User"+id);
        Set keys = redisTemplate.keys("*Cache*");
        redisTemplate.delete(keys);
        return Result.success();
    }
}
