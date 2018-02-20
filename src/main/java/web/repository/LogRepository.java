package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Log;
import web.model.User;

import java.util.Set;

public interface LogRepository extends JpaRepository<Log, Integer> {
    Set<Log> findAllByUserOrderByIdDesc(User user);
}