package com.leyou.page.web;

import com.leyou.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/page/pagehello")
public class HelloControler {

    @GetMapping("toHello")
    public String toHello(Model model) {
        model.addAttribute("msg" , "hello, world");

        User user1 = new User();
        user1.setName("aabaa");
        user1.setAge( 10 );
        model.addAttribute("user", user1);

        User user2 = new User();
        user2.setName("bbbbb");
        user2.setAge( 11 );

        List<User> usersList = Arrays.asList(user1, user2);

        model.addAttribute("usersList",usersList);

        return "hello";
    }

    @GetMapping("toHello1")
    public String toHello1(Model model) {
        return "hello";
    }

}
