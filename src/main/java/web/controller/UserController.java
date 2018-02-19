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
    public String newUset(Model model){
        User user = new User();
        model.addAttribute(user);
        return "user/form";
    }

    @GetMapping(value = "/edit/{id}")
    public String newUset(Model model, @PathVariable("id") String id){
        User user = userRepository.findById(id);
        model.addAttribute(user);
        return "user/form";
    }

    @PostMapping(value = {"/add", "edit/{id}"})
    public String save(@PathVariable("id") String id, @Valid User user, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request){

        if(result.hasErrors())
            return "user/form";
        else if(request.getRequestURL().toString().equals("/user/add")) {
            user.setCreatedAt(new Date());
            user.setRole("USER");
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            return "redirect:/user/list";
        } else {
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User edited successfully!");
            return "redirect:/user/list";
        }
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id){
        User user = userRepository.findById(id);
        ModelAndView modelAndView = new ModelAndView("user/view");
        return modelAndView.addObject(user);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id, RedirectAttributes redirectAttributes){
        try {
            userRepository.delete(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
            return "redirect:/user/list";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/user/list";
        }
    }
}
