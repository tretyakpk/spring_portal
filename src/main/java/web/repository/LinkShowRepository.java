package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.LinkShow;

public interface LinkShowRepository extends JpaRepository<LinkShow, Integer> {
}