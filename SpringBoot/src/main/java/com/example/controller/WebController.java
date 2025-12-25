package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.CustomerExceptionTest;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * controller 测试接口类
 */
@RestController
public class WebController {

    @Resource
    CustomerExceptionTest customerExceptionTest;
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    //get请求接口的接口
    @GetMapping("/hello") //接口路径，全局唯一
    public String hello() {
        return "Hello World"; //接口返回值，返回值与函数定义的类型一致
    }

    @GetMapping("/hello1")
    public Result hello1() {
        Result result = new Result();
        int a = 1/0;
        return result.success("hello world!");
    }

    @GetMapping("/customer")
    public Result customer(String msg){
        String test = customerExceptionTest.test(msg);
        return Result.success(test);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if("ADMIN".equals(account.getRole())){
            dbAccount = adminService.login(account);
        } else if("USER".equals(account.getRole())){
            dbAccount = userService.login(account);
        }else{
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }
}