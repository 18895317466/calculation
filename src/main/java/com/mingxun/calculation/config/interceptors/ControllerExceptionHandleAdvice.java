//package com.mingxun.warehouse.management.config.interceptors;
//
//import com.alibaba.fastjson.JSONException;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.CredentialsException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.UnknownAccountException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import wang.daoziwo.cloud.interfaces.Json;
//
//import javax.servlet.http.HttpServletResponse;
//import java.sql.SQLException;
//
///**
// * @author wangpeng
// * @date 2018/10/12 14:34:39
// */
//@RestControllerAdvice
//public class ControllerExceptionHandleAdvice {
//
//    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandleAdvice.class);
//
//    @ExceptionHandler
//    public Json handler(HttpServletResponse res, Exception e) {
//        logger.error(e.getMessage() + "===" + e.getLocalizedMessage());
//        if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
//            res.setStatus(HttpStatus.OK.value());
//        }
//        if (e instanceof NullPointerException) {
//            return Json.ERROR().setMessage("发生空指针异常");
//        } else if (e instanceof IllegalArgumentException) {
//            return Json.ERROR().setMessage("请求参数类型不匹配");
//        } else if (e instanceof SQLException) {
//            return Json.ERROR().setMessage("数据库访问异常");
//        } else if (e instanceof MethodArgumentTypeMismatchException) {
//            return Json.ERROR().setMessage(e.getMessage());
//        }else if (e instanceof JSONException) {
//            return Json.ERROR().setMessage("数据提交错误，请检查:" + e.getMessage());
//        } else if (e instanceof UnknownAccountException) {
//            return Json.ERROR().setMessage("此账号不存在");
//        } else if (e instanceof IncorrectCredentialsException) {
//            return Json.ERROR().setMessage("账号密码错误");
//        }else if (e instanceof AuthenticationException){
//            return Json.ERROR().setMessage("用户未登录");
//        }else {
//            return Json.ERROR().setMessage("系统异常");
//        }
//    }
//}
