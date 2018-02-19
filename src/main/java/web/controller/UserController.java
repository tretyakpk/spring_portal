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
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ModelAndView list(){
        return new ModelAndView("user/list").addObject("users", userRepository.findAll());
    }

    @GetMapping(value = "/add")
    public String newUser(Model model){
        model.addAttribute(new User());
        return "user/form";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "user/form";
        else {
            user.setCreatedAt(new Date());
            user.setRole("USER");
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping(value = "/edit/{id}")
    public String editUser(Model model, @PathVariable("id") String id){
        model.addAttribute(userRepository.findById(id));
        return "user/form";
    }

    @PostMapping(value = "edit/{id}")
    public String saveEdited(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "user/form";
        else {
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id){
        User user = userRepository.findById(id);
        return new ModelAndView("user/view").addObject(user);
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
