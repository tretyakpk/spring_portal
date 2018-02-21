package web.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Log;
import web.model.User;

import java.util.List;
import java.util.Set;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findTop50ByUserOrderByIdDesc(User user);
}