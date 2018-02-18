package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.entity.User;
import web.repository.UserRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/add")
    public ModelAndView addUserViev(){
        ModelAndView modelAndView = new ModelAndView("user/useradd");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addUser(@Valid User user, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();

        user.setCreatedAt(new Date());
        user.setRole("USER");

        if(result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()){
                System.out.println(error.toString());
                System.out.println(error.getField());
                System.out.println(error.getRejectedValue());
            }
            modelAndView.setViewName("user/add");
            modelAndView.addObject("errors", result);
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        userRepository.save(user);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/userview");
        return modelAndView;
    }

}
