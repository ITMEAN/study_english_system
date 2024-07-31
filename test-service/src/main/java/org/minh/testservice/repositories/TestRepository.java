package org.minh.testservice.repositories;

import org.minh.testservice.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TestRepository extends MongoRepository<Test,String>{

}
