package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.model.CSP;
import web.model.Link;
import web.model.User;
import web.repository.CSPRepository;
import web.repository.LinkRepository;
import web.repository.UserRepository;
import java.util.HashSet;

@RestController
@RequestMapping(value = "/link")
public class LinkRestController {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private CSPRepository CSPRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add/{csp_id}", method = RequestMethod.POST)
    public ResponseEntity<HashSet<Link>> addLinks(@RequestBody HashSet<Link> links, @PathVariable("csp_id") Integer csp_id){

        System.out.println(csp_id);

        for (Link link : links) {
            System.out.println(link.toString());
        }

        System.out.println(linkRepository.getByUrl("wqrett2wfe"));

        CSP csp = CSPRepository.findOne(csp_id);
        if(csp == null)
            return new ResponseEntity<HashSet<Link>>(HttpStatus.BAD_REQUEST);

        for (Link link : links) {
            if(linkRepository.getByUrl(link.getUrl()) == null){
                link.setCsp(csp);
                linkRepository.save(link);
                System.out.println(link.getId() + " saved!");
            }
        }

        return new ResponseEntity<HashSet<Link>>(links, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getSingleUser(@PathVariable("id") Integer id) {
        System.out.println("Fetching User with id " + id);
        User user = userRepository.findOne(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            return new User();
        }
        return user;
    }
}
