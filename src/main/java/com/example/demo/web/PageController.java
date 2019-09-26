package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zzs
 * @date 2019/9/3 19:57
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "/page/index.html";
    }
}
