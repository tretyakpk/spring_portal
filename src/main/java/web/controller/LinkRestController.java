package web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.model.Link;

import java.util.List;

@RestController
public class LinkRestController {

    @RequestMapping(value = "link", method = RequestMethod.POST)
    public ResponseEntity<List<Link>> saveLinks(@RequestBody List<Link> links){

        links.forEach(link -> System.out.println(link.getUrl()));


        return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
    }



}
