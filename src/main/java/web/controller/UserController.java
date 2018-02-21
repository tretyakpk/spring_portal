package web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.enreachment.CryptoData;
import web.model.CSP;
import web.model.CustomUserDetails;
import web.model.Log;
import web.model.User;
import web.repository.CSPRepository;
import web.repository.LogRepository;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private Environment env;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("")
    public String list(Model model){

//        if(!request.getParameterMap().containsKey("m") && env.getProperty("carrier").equals("wind"))
//            return "redirect:" + env.getProperty("wind.enreachment");
//        log();

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
            user.setRoles(roleRepository.findByRole("USER"));
            user.setActive(1);
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

    @PostMapping(value = "/edit/{id}")
    public String saveEdited(@Valid User userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Integer id){
        if(bindingResult.hasErrors()) return "user/editform";
        else {

            User user = userRepository.findOne(id);
            user.setName(userForm.getName());
            user.setPassword(userForm.getPassword());

            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "User " + user.getName() + " edited successfully!");
            return "redirect:/user/view/" + user.getId();
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model){

        User user                 = userRepository.findOne(id);
        List<CSP> notUserCsps     = CSPRepository.findAll();

        Set<CSP> userCsps = user.getCsps();
        notUserCsps.removeAll(userCsps);

        model.addAttribute("userCsps", userCsps);
        model.addAttribute("notUserCsps", notUserCsps);
        model.addAttribute("user", user);
        model.addAttribute("logs", logRepository.findAllByUserOrderByIdDesc(user));

        return "user/view";
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

    @GetMapping("/{userId}/add/csp/{cspId}")
    public String addCspToUser(@PathVariable("userId") Integer userId, @PathVariable("cspId") Integer cspId, RedirectAttributes redirectAttributes) {
        User user    = userRepository.findOne(userId);
        CSP newCsp   = CSPRepository.findOne(cspId);

        Set<CSP> csps = user.getCsps();
        csps.add(newCsp);
        user.setCsps(csps);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Added content service provider: " + newCsp.getName() + " to the user: " + user.getName());
        return "redirect:/user/view/" + userId;
    }

    @GetMapping("/{userId}/remove/csp/{cspId}")
    public String removeCspFromUser(@PathVariable("userId") Integer userId, @PathVariable("cspId") Integer cspId, RedirectAttributes redirectAttributes) {
        User user    = userRepository.findOne(userId);
        CSP oldCsp   = CSPRepository.findOne(cspId);

        Set<CSP> csps = user.getCsps();
        csps.remove(oldCsp);
        user.setCsps(csps);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Content service provider: " + oldCsp.getName() + " removed from the user: " + user.getName());
        return "redirect:/user/view/" + userId;
    }

    private User getCurrentUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findOne(userDetails.getId());
    }

    private void log(){

        HashMap<String, Object> forLog     = new HashMap<>();
        HashMap<String, String> mapHeaders = new HashMap<>();
        HashMap<String, String> mapGet     = new HashMap<>();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            mapHeaders.put( name, request.getHeader(name));
        }

        Enumeration<String> get = request.getParameterNames();
        while (get.hasMoreElements()) {
            String name = get.nextElement();
            mapGet.put( name, request.getParameter(name));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            forLog.put("url", request.getRequestURL());
            forLog.put("headers", mapHeaders);
            forLog.put("get",     mapGet);

            String json = objectMapper.writeValueAsString(forLog);

            Log log = new Log();
            log.setCreated_at(new Date());
            log.setParameters(json);
            log.setMsisdn(CryptoData.getMsisdn(request, env));
            log.setUser(getCurrentUser());
            System.out.println(log.getId());
            logRepository.save(log);
        }
        catch (JsonProcessingException e) { System.out.println(e.getMessage());}
    }
}
