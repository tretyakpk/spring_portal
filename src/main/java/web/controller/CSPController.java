package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.entity.CSP;
import web.entity.User;
import web.repository.CSPRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/csp")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @GetMapping("")
    public ModelAndView list() {
        return new ModelAndView("csp/list").addObject("csp", CSPRepository.findAll());
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id){
        return new ModelAndView("csp/view").addObject("csp", CSPRepository.findById(id));
    }


    @GetMapping(value = "/add")
    public String newUser(Model model) {
        model.addAttribute("csp", new CSP());
        return "csp/form";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid CSP csp, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "csp/form";
        else {
            CSPRepository.save(csp);
            redirectAttributes.addFlashAttribute("message", "Content service provider added successfully!");
            return "redirect:/csp/view/" + csp.getId();
        }
    }



}