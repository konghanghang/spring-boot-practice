package com.tools.controller;

import cn.hutool.core.lang.UUID;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tools.image.CaptchaUtils;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final String CAPTCHA_COOKIE_NAME = "pic_name_final";

    private Cache<String, String> cache = Caffeine.newBuilder()
        //设置过期时间；向缓存中写入的数据会在3分钟之后过期
        .expireAfterWrite(3, TimeUnit.MINUTES)
        //设置最大值；最大可以放1000条数据,当数据量超过最大值之后，则进行覆盖
        .maximumSize(1000)
        .build();

    /**
     * 请求生成生成图片
     * @param response
     */
    @RequestMapping("/genCaptcha")
    public void genCaptcha(HttpServletResponse response) {
        String imageCode = CaptchaUtils.getImageCode();
        String uuid = UUID.fastUUID().toString();
        cache.put(uuid, imageCode);
        Cookie cookie = new Cookie(CAPTCHA_COOKIE_NAME, uuid);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try(ServletOutputStream sos = response.getOutputStream()) {
            BufferedImage image = new BufferedImage(108, 40, BufferedImage.TYPE_INT_RGB);
            CaptchaUtils.drawGraphic(imageCode, image);

            ImageIO.write(image, "jpeg", sos);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 验证码校验
     */
    @RequestMapping("/verify")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            log.info("cookie中找不到图片key");
            throw new IllegalArgumentException();
        }
        String imageKey = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CAPTCHA_COOKIE_NAME)) {
                imageKey =  cookie.getValue();
                break;
            }
        }
        String imageValue = cache.getIfPresent(imageKey);
        if (StringUtils.isBlank(imageValue)) {
            log.info("图片已过期");
            throw new IllegalArgumentException();
        }
        Cookie cookie = new Cookie(CAPTCHA_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
