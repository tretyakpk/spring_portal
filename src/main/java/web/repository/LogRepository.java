package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Log;
import web.model.User;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
