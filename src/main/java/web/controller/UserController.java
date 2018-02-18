package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/add")
    public String add(Model userModel){

        userModel.addAttribute("user", new User());

        return "user";
    }

}
