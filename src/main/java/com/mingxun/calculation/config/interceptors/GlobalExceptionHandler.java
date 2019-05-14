//package com.mingxun.warehouse.management.config.interceptors;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.CredentialsException;
//import org.apache.shiro.authc.DisabledAccountException;
//import org.apache.shiro.session.InvalidSessionException;
//import org.apache.shiro.session.UnknownSessionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
// */
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 用户未登录
//     */
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public String unAuth(AuthenticationException e) {
//        log.error("用户未登陆：", e);
//        return "/login.html";
//    }
//
//    /**
//     * 账号被冻结
//     */
//    @ExceptionHandler(DisabledAccountException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public String accountLocked(DisabledAccountException e, Model model) {
//        model.addAttribute("tips", "账号被冻结");
//        return "/login.html";
//    }
//
//    /**
//     * 账号密码错误
//     */
//    @ExceptionHandler(CredentialsException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public String credentials(CredentialsException e, Model model) {
//        model.addAttribute("tips", "账号密码错误");
//        return "/login.html";
//    }
//
//    /**
//     * session失效的异常拦截
//     */
//    @ExceptionHandler(InvalidSessionException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String sessionTimeout(InvalidSessionException e, Model model, HttpServletRequest request, HttpServletResponse response) {
//        model.addAttribute("tips", "session超时");
//        assertAjax(request, response);
//        return "/login.html";
//    }
//
//    /**
//     * session异常
//     *
//     * @author stylefeng
//     * @Date 2017/6/7 21:02
//     */
//    @ExceptionHandler(UnknownSessionException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String sessionTimeout(UnknownSessionException e, Model model, HttpServletRequest request, HttpServletResponse response) {
//        model.addAttribute("tips", "session超时");
//        assertAjax(request, response);
//        return "/login.html";
//    }
//
//    private void assertAjax(HttpServletRequest request, HttpServletResponse response) {
//        if (request.getHeader("x-requested-with") != null
//                && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
//            //如果是ajax请求响应头会有，x-requested-with
//            response.setHeader("sessionstatus", "timeout");//在响应头设置session状态
//        }
//    }
//
//}
