package com.turboconsulting.DAO;

import com.turboconsulting.Entity.ConsentExperiment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("sqlConsentExperimentData")
public interface ConsentExperimentDao extends CrudRepository<ConsentExperiment, Integer>{

}
