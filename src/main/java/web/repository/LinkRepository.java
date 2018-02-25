package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.CSP;
import web.model.Link;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface LinkRepository extends JpaRepository<Link, Integer> {
    Link getByUrl(String url);
    List<Link> findAllByCspOrderByServiceAsc(CSP csp);
}