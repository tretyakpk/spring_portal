package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.*;
import web.repository.*;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/page")
public class StaticPageController {

    @Autowired
    private LinkShowRepository linkShowRepository;

    @GetMapping(value = "/view")
    public String view(Model model){

        model.addAttribute("link", new LinkShow());
        model.addAttribute("links", linkShowRepository.findAll());

        return "static/view";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String add(@Valid LinkShow linkShow, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) return "redirect:/links/view";
        else {
            linkShowRepository.save(linkShow);
            redirectAttributes.addFlashAttribute("message", "Link added successfully!");
            return "redirect:/page/view";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/static/delete/{linkId}")
    public String delete(@PathVariable("linkId") Integer linkId, RedirectAttributes redirectAttributes){

        linkShowRepository.delete(linkId);
        redirectAttributes.addFlashAttribute("message", "Link successfully deleted!");

        return "redirect:/page/view";
    }
}