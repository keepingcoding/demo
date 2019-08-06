package com.example.demo.templates.thymeleaf.controller;

import com.example.demo.templates.thymeleaf.bean.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author zzs
 * @date 2019/8/2 16:55
 */
@Controller
public class ThymeleafController {

    @GetMapping(value = "/")
    @ResponseBody
    public String show() {
        return "hello world!!";
    }

    /**
     * 跳转到static目录
     */
    @GetMapping(value = "/index")
    public String index() {
        return "redirect:/index.html";
    }

    /**
     * 跳转到templates目录下
     */
    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    @GetMapping(value = "/thymeleaf")
    public String thymeleaf(Map<String, Object> params) {

        //普通文本
        params.put("name", "hello thymeleaf");
        //带样式的文本
        params.put("style", "<span style='color:red'>Jerry</span>");
        //返回对象
        Person person = new Person("zhangsan", 20, "12345678901", "Shanghai/China");
        params.put("obj", person);
        //返回集合日期等类型
        HashSet hashSet = new HashSet<String>() {
            {
                add("aaa");
                add("bbb");
                add("ccc");
            }
        };
        params.put("set", hashSet);
        List<String> stringList = Arrays.asList("111", "222", "333", "444", "555");
        params.put("list", stringList);
        params.put("date", new Date());

        //集合
        Person person1 = new Person("zhangsan", 20, "12345678901", "Shanghai/China");
        Person person2 = new Person("lisi", 21, "12345678901", "Beijing/China");
        Person person3 = new Person("wangwu", 22, "12345678901", "Guangzhou/China");
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        params.put("objList", personList);

        Map<String, Person> personMap = new HashMap<>(5);
        personMap.put("person1", person1);
        personMap.put("person2", person2);
        personMap.put("person3", person3);
        params.put("objMap", personMap);

        return "thymeleafPage";
    }

    @GetMapping(value = "/thymeleaf2")
    public String thymeleaf2(Model model) {

        model.addAttribute("name", "hello thymeleaf");

        return "thymeleafPage";
    }
}
