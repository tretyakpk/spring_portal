package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.LinkShow;

import java.util.List;

public interface LinkShowRepository extends JpaRepository<LinkShow, Integer> {
    List<LinkShow> findAllByCarrier(String carrier);
}