package web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.entity.User;

import java.util.Date;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByName(String name);
    public User findByCreatedAtAndName(Date date, String string);
    public List<User> findAll();
}
