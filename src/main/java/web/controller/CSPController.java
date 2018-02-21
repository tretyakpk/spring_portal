package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.CSP;
import web.model.CustomUserDetails;
import web.model.Link;
import web.model.User;
import web.repository.CSPRepository;
import web.repository.LinkRepository;
import web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

// TODO ADD SECURITY!!!
@Controller
@RequestMapping(value = "")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("")
    public String list(Model model, HttpServletRequest request) {


        System.out.println(request.isUserInRole("ADMIN"));

        User user = getCurrentUser();
        Set<CSP> csps = user.getCsps();

        model.addAttribute("csps", csps);
        return "csp/list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/csp/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model){

        CSP csp = CSPRepository.findOne(id);
        if(csp == null)
            return "redirect:/csp/list";

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
            return "redirect:/csp";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/csp";
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

    private User getCurrentUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findOne(userDetails.getId());
    }
}