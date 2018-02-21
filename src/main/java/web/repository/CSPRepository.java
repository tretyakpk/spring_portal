package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.CSP;

public interface CSPRepository extends JpaRepository<CSP, Integer> {}