package com.sym;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.LoginUser;
import com.sym.entity.User;
import com.sym.service.impl.UserServiceImpl;
import com.sym.utils.JwtUtil;
import com.sym.utils.SerializeToString;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CarManageSystemApplicationTests {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
    }
    public static String encodeKey(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static SecretKey decodeKey(String storedKey) {
        byte[] storedKeyBytes = Base64.getDecoder().decode(storedKey);
        return  new SecretKeySpec(storedKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
    @Test
    public void testJwt() {
        /*SecretKey secretKey = secretKeyFor(SignatureAlgorithm.HS256);
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("aa","Nihao");
        String jwt = JwtUtil.createJWT(hashMap, secretKey);
        String base64Key = encodeKey(secretKey);
        SecretKey secretKey1 = decodeKey(base64Key);
        Claims claims = JwtUtil.parseJWT(jwt, secretKey1);
        System.out.println(claims);*/
        Object secretKey = redisTemplate.opsForValue().get("secretKey");
        System.out.println(secretKey);
/*        SecretKey secretKey1 = decodeKey((String) secretKey);
        Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjg2MjMwOTM0LCJleHAiOjE2ODYyMzE1Mzl9.88usSQ1Q8mz1xfsl7XtTkBmxZhRCmatR1lPgGgXHDyI", secretKey1);
        System.out.println((Integer) claims.get("id"));*/
    }

    @Test
    public void testUserGet() {
        LoginUser user1 = (LoginUser) redisTemplate.opsForValue().get("User1");
        System.out.println(user1.getUser());
    }

    @Test
    public void testPage() {
        User user  = new User();
        user.setMarker(0);
        Page page = userService.selectPage(1, 5, user);
        System.out.println(page.getRecords());
    }
    @Test
    public void testSerializeToStringByHashCode() {
        User user = new User();
        user.setMarker(0);
        user.setGender("女");

        User user1 = new User();
        user1.setMarker(0);
        user1.setGender("女");

        System.out.println(SerializeToString.serializeToStringByBase64(user1));
        System.out.println(SerializeToString.serializeToStringByBase64(user));
        String s = SerializeToString.serializeToStringByBase64(user1);
        String s1 = SerializeToString.serializeToStringByBase64(user);
        assertEquals(s,s1);
    }
    @Test
    public void RedisTest() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set<String> keys = redisTemplate.keys("userCache::PAGE*");
        System.out.println(keys);
        redisTemplate.delete(keys);
        /*redisTemplate.delete("k1");*/
        /*if (keys != null) {
            redisTemplate.delete(keys);
        }*/
    }
}
