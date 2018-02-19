package web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.entity.CSP;

import java.util.List;

public interface CSPRepository extends MongoRepository<CSP, String> {
    public List<CSP> findAll();
    public CSP findById(String id);
    public CSP findByName(String name);
}
