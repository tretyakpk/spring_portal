package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.CSP;
import web.model.User;

import java.util.List;

public interface CSPRepository extends JpaRepository<CSP, Integer> {
}