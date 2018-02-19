package web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.entity.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findAll();
    public User findById(String id);
}
