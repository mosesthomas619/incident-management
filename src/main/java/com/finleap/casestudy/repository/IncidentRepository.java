package com.finleap.casestudy.repository;


import com.finleap.casestudy.domain.IncidentDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * This class is the list of mongo queries that return Incident objects.
 * NOTE: The @Query annotations will stop working if the attribute name changes in the database.
 */
@Repository
public interface IncidentRepository extends MongoRepository<IncidentDO, String> {

    Optional<IncidentDO> findByTitle(String s);

    List<IncidentDO> findByAssignee(String assignee);

    List<IncidentDO> findByCreatedBy(String name);
}
