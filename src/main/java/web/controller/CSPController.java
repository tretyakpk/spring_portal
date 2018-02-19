package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.entity.User;
import web.repository.CSPRepository;

@Controller
@RequestMapping(value = "/csp")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @GetMapping("")
    public ModelAndView list() {
        return new ModelAndView("csp/list").addObject("csp", CSPRepository.findAll());
    }

    @GetMapping(value = "/add")
    public String newUser(Model model) {
        model.addAttribute(new User());
        return "user/form";
    }

}