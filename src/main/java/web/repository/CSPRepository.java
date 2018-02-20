package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.CSP;
import web.model.Log;
import web.model.User;

import java.util.Set;

public interface CSPRepository extends JpaRepository<CSP, Integer> {
}