package web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.enrichment.CryptoData;
import web.model.*;
import web.repository.CSPRepository;
import web.repository.LinkRepository;
import web.repository.LogRepository;
import web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private Environment env;

    @Autowired
    private HttpServletRequest request;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("")
    public String list(Model model) {

        User user = getCurrentUser();
        List<CSP> csps = user.getCsps();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println( authentication.getDetails());

        model.addAttribute("csps", csps);
        return "csp/list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/csp/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model){

        User user = getCurrentUser();

        CSP csp = CSPRepository.findOne(id);
        if(csp == null)
            return "redirect:/";

        model.addAttribute("csps", user.getCsps());
        model.addAttribute("csp", csp);
        model.addAttribute("links", linkRepository.findAllByCspOrderByIdDesc(csp));

        return "csp/view";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/csp/add")
    public String newUser(Model model) {
        model.addAttribute("csp", new CSP());
        return "csp/addform";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/csp/add")
    public String saveNew(@Valid CSP csp, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "csp/addform";
        else {
            csp.setCreatedAt(new Date());
            CSPRepository.save(csp);

            User user = getCurrentUser();
            List<CSP> userCsps = user.getCsps();
            userCsps.add(csp);
            user.setCsps(userCsps);
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("message", "Content service provider " + csp.getName() + " added successfully!");
            return "redirect:/csp/view/" + csp.getId();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/csp/edit/{id}")
    public String editUser(Model model, @PathVariable("id") Integer id){
        model.addAttribute("csp", CSPRepository.findOne(id));
        return "csp/editform";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/csp/edit/{id}")
    public String saveEdited(@Valid CSP cspForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Integer id){
        if(bindingResult.hasErrors()) return "csp/editform";
        else {
            CSP csp = CSPRepository.findOne(id);
            csp.setName(cspForm.getName());
            CSPRepository.save(csp);
            redirectAttributes.addFlashAttribute("message", "Content Service Provider " + csp.getName() + " edited successfully!");
            return "redirect:/csp/view/" + csp.getId();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/csp/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {

            CSPRepository.delete(id);
            redirectAttributes.addFlashAttribute("message", "Content Service Provider deleted successfully!");
            return "redirect:/";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/csp/{cspId}/link/delete/{linkId}")
    public String deleteLink(@PathVariable("cspId") Integer cspId, @PathVariable("linkId") Integer linkId, RedirectAttributes redirectAttributes){

        linkRepository.delete(linkId);
        redirectAttributes.addFlashAttribute("message", "Link deleted successfully!");
        return "redirect:/csp/view/" + cspId;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/csp/{cspId}/link/change/{linkId}")
    public String changelink(@PathVariable("cspId") Integer cspId, @PathVariable("linkId") Integer linkId, RedirectAttributes redirectAttributes){
        
        Link link = linkRepository.findOne(linkId);
        if(link.getActive() == 1)
            link.setActive(0);
        else
            link.setActive(1);

        linkRepository.save(link);
        redirectAttributes.addFlashAttribute("message", "Status changed successfully!");
        return "redirect:/csp/view/" + cspId;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/user/logs")
    public String logs(Model model) {

        User user = getCurrentUser();
        List<Log> logs = logRepository.findTop50ByUserOrderByIdDesc(user);
        List<CSP> csps = user.getCsps();

        model.addAttribute("logs", logs);
        model.addAttribute("csps", csps);
        model.addAttribute("user", user);

        return "user/viewlogs";
    }

    @RequestMapping(value = "/click/save", method = RequestMethod.POST)
    @ResponseBody
    public String logClickToLinkPost(@RequestBody String body){

        HashMap<String, Object> forLog     = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            forLog.put("click", body);
            String json = objectMapper.writeValueAsString(forLog);

            Log log = new Log();
            log.setCreated_at(new Date());
            log.setParameters(json);
            log.setType("click");
            log.setUser(getCurrentUser());
            logRepository.save(log);
        }
        catch (JsonProcessingException e) { System.out.println(e.getMessage());}

        return "OK";
    }

    @GetMapping(value = "/enduser")
    public String endUserWind(){

        User user = getCurrentUser();

        if(!env.getProperty("environment").equals("local") && !user.getName().equals("admin")) {
            if (!request.getParameterMap().containsKey("m") && env.getProperty("carrier").equals("wind"))
                return "redirect:" + env.getProperty("wind.enrichment");
            log();
        }

        return "redirect:/";
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
            forLog.put("get", mapGet);

            String json = objectMapper.writeValueAsString(forLog);

            Log log = new Log();
            log.setCreated_at(new Date());
            log.setType("login");
            log.setParameters(json);
            log.setMsisdn(CryptoData.getMsisdn(request, env));
            log.setUser(getCurrentUser());
            logRepository.save(log);
        }
        catch (JsonProcessingException e) { System.out.println(e.getMessage());}
    }

    private User getCurrentUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findOne(userDetails.getId());
    }

}