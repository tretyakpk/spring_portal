package web.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByName(String username);
}
