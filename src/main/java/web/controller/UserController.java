package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.entity.User;
import web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("user/list");
        modelAndView.addObject("users", userRepository.findAll());
        return modelAndView;
    }

    @GetMapping(value = "/add")
    public String newUser(Model model){
        User user = new User();
        model.addAttribute(user);
        return "user/form";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            return "user/form";
        } else {
            user.setCreatedAt(new Date());
            user.setRole("USER");
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping(value = "/edit/{id}")
    public String editUser(Model model, @PathVariable("id") String id){
        User user = userRepository.findById(id);
        model.addAttribute(user);
        return "user/form";
    }

    @PostMapping(value = "edit/{id}")
    public String saveEdited(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) {
            return "user/form";
        } else {
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id){
        User user = userRepository.findById(id);
        ModelAndView modelAndView = new ModelAndView("user/view");
        return modelAndView.addObject(user);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes){
        try {
            userRepository.delete(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
            return "redirect:/user";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/user";
        }
    }
}
