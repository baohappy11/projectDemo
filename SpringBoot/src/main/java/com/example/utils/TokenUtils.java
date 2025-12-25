package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    static AdminService staticAdminService;
    static UserService staticUserService;

    //springboot工程启动后会加载这段代码
    @PostConstruct
    public void init(){
        staticAdminService = adminService;
        staticUserService = userService;
    }
    /**
     * 生成token
     */
    public static String createToken(String data, String sign){
        return JWT.create().withAudience(data)//将userId-role保存到token里面，作为载荷（负载）
                .withExpiresAt(DateUtil.offsetDay(new Date() , 1))//1天后token过期
                .sign(Algorithm.HMAC256(sign));//以password作为token的秘钥，HMAC256算法加密
    }

    /**
     * 获取当前登录用户信息
     */
    public static Account getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            request.getParameter("token");
        }
        //拿到token的载荷
        String audience = JWT.decode(token).getAudience().get(0);
        String[] split = audience.split("-");
        String userId = split[0];
        String role = split[1];
        //根据token解析出来的userId去对应的表查询用户信息
        if("ADMIN".equals(role)){
            return staticAdminService.selectById(userId);
        }else if("USER".equals(role)){
            return staticUserService.selectById(userId);
        }
        return null;
    }
}
