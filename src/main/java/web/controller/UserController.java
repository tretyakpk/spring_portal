package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.Users;
import web.repository.UsersRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UsersRepository userRepository;

    @GetMapping("")
    public ModelAndView list(){
        return new ModelAndView("user/list").addObject("users", userRepository.findAll());
    }

    @GetMapping(value = "/add")
    public String newUser(Model model){
        model.addAttribute("user", new Users());
        return "user/addform";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid Users user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "user/addform";
        else {
            user.setCreatedAt(new Date());
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User " + user.getName() + " added successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping(value = "/edit/{id}")
    public String editUser(Model model, @PathVariable("id") Integer id){
        model.addAttribute(userRepository.findOne(id));
        return "user/editform";
    }

    @PostMapping(value = "/edit")
    public String saveEdited(@Valid Users user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "user/editform";
        else {
            Users user1 = userRepository.findOne(user.getId());

            user1.setName(user.getName());
            user1.setPassword(user.getPassword());

            userRepository.save(user1);
            redirectAttributes.addFlashAttribute("message", "User " + user1.getName() + " edited successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") Integer id){
        return new ModelAndView("user/view").addObject("user", userRepository.findOne(id));
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
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
