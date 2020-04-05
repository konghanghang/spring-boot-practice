package com.test.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * jwt工具类
 * @author konghang
 */
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 过期时间
    private static final long EXPIRE_TIME = 5 * 60 * 1000;
    private static final String SECRET = "XX#$%()(#";


    /**
     * 生成签名,以及设置过期时间
     * @param username 用户名
     * @return 加密的token
     */
    public static String createToken(String username) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    // .withClaim("salt", salt)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     * @param token
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verifyToken(String token, String username) {
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        try{
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    // .withClaim("salt", salt)
                    .build();
            verifier.verify(token);
            return true;
        } catch (SignatureVerificationException e) {
            logger.info("verify token error:{}", "签名校验失败");
        } catch (TokenExpiredException e) {
            logger.info("verify token error:{}", "token过期");
        } catch (InvalidClaimException e) {
            logger.info("verify token error:{}", "token过期(Claim),请重新登录");
        } catch (JWTVerificationException e) {
            logger.info("verify token error:{}", "token校验失败");
        } catch (Exception e){
            logger.info("verify token error:{}", "token校验失败");
        }
        return false;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
        }
        return null;
    }

}
