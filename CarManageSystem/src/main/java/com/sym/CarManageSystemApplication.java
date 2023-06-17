package com.sym;

import com.sym.utils.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import java.util.concurrent.TimeUnit;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@EnableCaching
public class CarManageSystemApplication {

    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(CarManageSystemApplication.class, args);
    }

    @PostConstruct
    public void init() {
        //SpringBoot启动的时候默认生成一次密钥
        SecretKey secretKey = secretKeyFor(SignatureAlgorithm.HS256);
        redisTemplate.opsForValue().set("secretKey", JwtUtil.encodeKey(secretKey), 3, TimeUnit.DAYS);
        System.out.println("------密钥生成成功!------");
    }

}
