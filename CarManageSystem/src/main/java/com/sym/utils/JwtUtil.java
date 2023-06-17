package com.sym.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class JwtUtil {

    /**
     * 签名用的密钥
     */

    @Autowired
    private RedisTemplate<String, String> redis;

    public static RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void getRedisTemplate() {
        redisTemplate = this.redis;
    }

    public static String encodeKey(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static SecretKey decodeKey(String storedKey) {
        byte[] storedKeyBytes = Base64.getDecoder().decode(storedKey);
        return  new SecretKeySpec(storedKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法
     *
     *   exp  jwt过期时间
     * @param claims 保存在Payload（有效载荷）中的内容
     * @return token字符串
     */
    public static String createJWT(Map<String, Object> claims,SecretKey secretKey) {
        //指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成一个密钥
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date();
        exp.setTime(exp.getTime()+24*60*60*7*10);
        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload（有效载荷）中的内容
                .setClaims(claims)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置过期时间
                .setExpiration(exp)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256, secretKey);
        /*redisTemplate.opsForValue().set(builder.compact(), String.valueOf(1), 7, TimeUnit.DAYS);*/
        return builder.compact();
    }

    /**
     * 解析token，获取到Payload（有效载荷）中的内容，包括验证签名，判断是否过期
     *
     * @param token
     * @return
     */
    public static Claims parseJWT(String token, SecretKey secretKey) {
        //得到DefaultJwtParser
        //先验证是否有token
/*        Boolean flag = redisTemplate.hasKey(token);
        if (!flag) {
            return null;
        }*/
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(secretKey)
                //设置需要解析的token
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
