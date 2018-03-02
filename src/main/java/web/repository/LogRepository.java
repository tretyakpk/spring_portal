package web.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Log;
import web.model.User;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findTop50ByUserOrderByIdDesc(User user);

    List<Log> findAllByUserOrderByIdAsc(User user, Pageable pageable);
}