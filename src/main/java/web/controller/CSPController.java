package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.entity.CSP;
import web.entity.Link;
import web.repository.CSPRepository;
import web.repository.LinkRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/csp")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private LinkRepository linkRepository;

    @GetMapping("")
    public ModelAndView list() {
        return new ModelAndView("csp/list").addObject("csps", CSPRepository.findAll());
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView("csp/view");
        modelAndView.addObject("csp", CSPRepository.findById(id));
        modelAndView.addObject("link", new Link());
        return modelAndView;
    }


    @GetMapping(value = "/add")
    public String newUser(Model model) {
        model.addAttribute("csp", new CSP());
        return "csp/addform";
    }

    @PostMapping(value = "/add")
    public String saveNew(@Valid CSP csp, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "csp/addform";
        else {
            csp.setCreatedAt(new Date());
            CSPRepository.save(csp);
            redirectAttributes.addFlashAttribute("message", "Content service provider " + csp.getName() + " added successfully!");
            return "redirect:/csp/view/" + csp.getId();
        }
    }

    @GetMapping(value = "/edit/{id}")
    public String editUser(Model model, @PathVariable("id") String id){
        model.addAttribute("csp", CSPRepository.findById(id));
        return "csp/editform";
    }

    @PostMapping(value = "/edit")
    public String saveEdited(@Valid CSP csp, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "csp/editform";
        else {
            CSP csp1 = CSPRepository.findOne(csp.getId());
            csp1.setName(csp.getName());
            CSPRepository.save(csp1);
            redirectAttributes.addFlashAttribute("message", "Content Service Provider " + csp.getName() + " edited successfully!");
            return "redirect:/csp/view/" + csp.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes){
        try {
            CSPRepository.delete(id);
            redirectAttributes.addFlashAttribute("message", "Content Service Provider deleted successfully!");
            return "redirect:/csp";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/csp";
        }
    }

    @PostMapping(value = "/add/link/{id}")
    public String addLinks(@PathVariable("id") String id){

        CSP csp = CSPRepository.findById(id);



        return "redirect:/csp/view/" + id;
    }
}