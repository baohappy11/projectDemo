//GlobalExceptionHandler.java
package com.example.exception;

import com.example.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获器。
 */
@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody //将result对象转换成json格式
    public Result error (Exception e){
        logger.error("后端日志显示系统异常：" , e);
        return Result.error("前端返回的系统异常");
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody //将result对象转换成json格式
    public Result error (CustomerException e){
        logger.error("后端日志显示自定义异常：" , e);
        return Result.error(e.getCode(),e.getMsg());
    }

}