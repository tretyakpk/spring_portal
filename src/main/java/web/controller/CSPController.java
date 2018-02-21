package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.CSP;
import web.model.Link;
import web.repository.CSPRepository;
import web.repository.LinkRepository;

import javax.validation.Valid;
import java.util.Date;

// TODO ADD SECURITY!!!
@Controller
@RequestMapping(value = "/csp")
public class CSPController {

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private LinkRepository linkRepository;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("csps", CSPRepository.findAll());
        return "csp/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model){

        CSP csp = CSPRepository.findOne(id);
        if(csp == null)
            return "redirect:/csp/list";

        model.addAttribute("csp", csp);
        model.addAttribute("links", linkRepository.findAllByCspOrderByIdDesc(csp));

        return "csp/view";
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
    public String editUser(Model model, @PathVariable("id") Integer id){
        model.addAttribute("csp", CSPRepository.findOne(id));
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

    @GetMapping("/{cspId}/link/delete/{linkId}")
    public String deleteLink(@PathVariable("cspId") Integer cspId, @PathVariable("linkId") Integer linkId, RedirectAttributes redirectAttributes){

        linkRepository.delete(linkId);
        redirectAttributes.addFlashAttribute("message", "Link deleted successfully!");
        return "redirect:/csp/view/" + cspId;

    }
    
    @GetMapping("/{cspId}/link/change/{linkId}")
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
}