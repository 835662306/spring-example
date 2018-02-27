package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/27 14:43
 * @Version 1.0
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/hello")
    public String index(){
        return "hello";
    }
}
