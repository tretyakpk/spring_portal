package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.model.CSP;
import web.model.Link;
import web.repository.CSPRepository;
import web.repository.LinkRepository;

import java.util.HashSet;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/link")
public class LinkRestController {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private CSPRepository CSPRepository;

    @RequestMapping(value = "/add/{csp_id}", method = RequestMethod.POST)
    @ResponseBody
    public String addLinks(@RequestBody HashSet<Link> links, @PathVariable("csp_id") Integer csp_id){


        CSP csp = CSPRepository.findOne(csp_id);
//        if(csp == null)
//            return new ResponseEntity<HashSet<Link>>(HttpStatus.BAD_REQUEST);

        for (Link link : links) {
            if(linkRepository.getByUrl(link.getUrl()) == null){
                link.setCsp(csp);
                linkRepository.save(link);
            }
        }

//        return new ResponseEntity<HashSet<Link>>(links, HttpStatus.OK);
        return "OK";
    }
}
