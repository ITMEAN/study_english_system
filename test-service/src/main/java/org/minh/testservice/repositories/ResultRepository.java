package org.minh.testservice.repositories;

import org.minh.testservice.entity.ResultTest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResultRepository extends MongoRepository<ResultTest,String> {
    List<ResultTest> findAllByEmailOrderByDateDesc(String email);
}
