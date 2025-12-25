package com.example.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler){
        //1.从请求头获取token
        String token = request.getHeader("token");
        //如果没有获取到，再从参数里获取一次
        if(StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        //2.认证token
        if(StrUtil.isBlank(token)){
            throw new CustomerException("401" , "您无权限操作（没有获取到token）" );
        }
        Account account = null;
        try{
            //获取token的载荷
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            //根据token解析出来的userId去对应的表中查询用户信息
            if("ADMIN".equals(role)){
                account = adminService.selectById(userId);
            } else if("USER".equals(role)){
                account = userService.selectById(userId);
                System.out.println("1");
            }
        }catch (Exception e){
            throw new CustomerException("401", "您无权限操作（token解析失败）");
        }
        if(account == null){
            throw new CustomerException("401" , "您无权限操作（数据库中未查到该用户）");
        }
        try{
            //3.验证签名
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        }catch (Exception e){
            throw new CustomerException("401" , "您无权限操作（token验证失败）");
        }
        return true;
    }
}
