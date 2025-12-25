package com.example.service;

import com.example.exception.CustomerException;
import org.springframework.stereotype.Service;

@Service
public class CustomerExceptionTest {

    public String test(String msg) {
        if ("success".equals(msg)) {
            return "success";
        } else {
            throw new CustomerException("测试错误");
        }
    }

}
