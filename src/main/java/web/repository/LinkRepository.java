package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.CSP;
import web.model.Link;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Integer> {
    Link getByUrl(String url);
    List<Link> findAllByCspOrderByServiceAsc(CSP csp);
}