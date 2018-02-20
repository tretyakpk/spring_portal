package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.CSP;
import web.model.Link;
import web.repository.CSPRepository;
import web.repository.LinkRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(value = "/link")
public class LinkController {

    @Autowired
    private LinkRepository linkRepository;


//    TODO add seciryty
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            linkRepository.delete(id);
            redirectAttributes.addFlashAttribute("message", "Link deleted successfully!");
            return "redirect:/csp";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Something went wrong!");
            return "redirect:/csp";
        }
    }

    @GetMapping("/change/{id}")
    public String change(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        Link link = linkRepository.findOne(id);

        if(link.getActive() == 1)
            link.setActive(0);
        else
            link.setActive(1);

        return "redirect:/csp";
    }
}