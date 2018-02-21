package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.CSP;
import web.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String username);

}
