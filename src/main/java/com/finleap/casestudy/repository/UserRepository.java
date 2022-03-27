package com.finleap.casestudy.repository;

import com.finleap.casestudy.domain.IncidentUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<IncidentUser, String> {

    Optional<IncidentUser> findByUsername(String userName);

}
