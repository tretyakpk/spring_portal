package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.enreachment.CryptoData;
import web.model.CustomUserDetails;
import web.model.Log;
import web.model.User;
import web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("")
    public String list(Model model){

        if(!request.getParameterMap().containsKey("m") && env.getProperty("carrier").equals("wind"))
            return "redirect:" + env.getProperty("wind.enreachment");

        String msisdn = CryptoData.getMsisdn(request, env);
        System.out.println(msisdn);

        Enumeration<String> headers = request.getHeaderNames();
        HashMap<String, String> headersMap = new HashMap<>();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            headersMap.put( name, request.getHeader(name));
        }

        // works
        System.out.println(request.getRequestURL());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        System.out.println(userDetails.getId());

        User user = userRepository.findOne(userDetails.getId());



        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping(value = "/add")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "user/addform";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
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
    public String saveEdited(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "user/editform";
        else {
            User user1 = userRepository.findOne(user.getId());

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

    private static void log(){

    }
}
