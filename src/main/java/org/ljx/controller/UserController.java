package org.ljx.controller;

import org.ljx.entity.User;
import org.ljx.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ljx on 2018/5/2.
 */
@Controller
@EnableAutoConfiguration
@ComponentScan(basePackages={"org.ljx.service"})//添加的注解
public class UserController {
    //依赖注入
    @Autowired
    UserService userService;

    /**
     * @RestController代表这个类是用Restful风格来访问的，如果是普通的WEB页面访问跳转时，我们通常会使用@Controller
    value = "/users/{username}" 代表访问的URL是"http://host:PORT/users/实际的用户名"
    method = RequestMethod.GET 代表这个HTTP请求必须是以GET方式访问
    consumes="application/json" 代表数据传输格式是json
     @PathVariable将某个动态参数放到URL请求路径中
     @RequestParam指定了请求参数名称
     */
    @ResponseBody
    @RequestMapping(value = "qp/{name}",method = RequestMethod.GET)
    public List<User> queryProduct(@PathVariable String name, HttpServletResponse httpServletResponse) {
        System.out.println(name);
        List<User> p = userService.queryUserByName(name);
        return p;
    }

    @GetMapping("/login")
    public String login(){
        return "index";
    }
}
