package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import web.model.Log;
import web.model.User;
import web.repository.LogRepository;
import web.repository.UserRepository;

import java.util.List;

@RestController
public class LogRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @RequestMapping("/user/{id}/logs/{page}/size/{size}")
    public List<Log> getLogs(@PathVariable("id") Integer userId,
                             @PathVariable("page") Integer page,
                             @PathVariable("size") Integer size){
        User user = userRepository.findOne(userId);
        return logRepository.findAllByUserOrderByIdAsc(user, new PageRequest(--page, size));
    }
}
