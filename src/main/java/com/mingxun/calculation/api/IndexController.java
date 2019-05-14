package com.mingxun.calculation.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ouyang on 2019/5/10.
 */
@Controller
public class IndexController {

    /*登录页面*/
    @RequestMapping("/index")
    public String to_login(Model model){
        return  "index";
    }

    /*登录页面*/
    @RequestMapping("/4glist")
    public String glist(Model model){
        return  "/page/4glist";
    }
}
