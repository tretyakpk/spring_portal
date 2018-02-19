package web.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HelloResource {

    @GetMapping("/notsecured")
    public String hello() {
        return "Hello Youtube";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/secured/admin")
    public String securedHello() {
        return "Secured Hello";
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/secured/user")
    public String alternate() {
        return "alternate";
    }
}
