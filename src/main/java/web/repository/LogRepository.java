package web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.entity.Log;

public interface LogRepository extends MongoRepository<Log, String> {


}
