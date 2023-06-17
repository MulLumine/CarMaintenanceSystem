package com.sym.filter;

import com.sym.common.LoginUser;
import com.sym.entity.User;
import com.sym.exception.ServiceException;
import com.sym.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
//Token过滤器
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Resource
    RedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        Integer id;
        try {
            Object secretKey = redisTemplate.opsForValue().get("secretKey");
            SecretKey key = JwtUtil.decodeKey((String) secretKey);
            Claims claims = JwtUtil.parseJWT(token, key);
            //获取token中存的用户id
            id = (Integer) claims.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("token非法");
        }

        //从redis中获取用户信息(redis里的id)
        String redisKey = "User" + id;//拿取存储的对象
        LoginUser user = (LoginUser) redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户未登录 ");
        }
        //存入SecurityContextHolder
        //TODO 要获取权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}


