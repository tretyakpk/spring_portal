package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Link;

public interface LinkRepository extends JpaRepository<Link, Integer> {
}