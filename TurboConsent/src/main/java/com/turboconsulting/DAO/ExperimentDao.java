package com.turboconsulting.DAO;

import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Entity.Visitor;
import com.turboconsulting.Entity.VisitorExperiment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("sqlExperimentData")
public interface ExperimentDao extends CrudRepository<Experiment, Integer> {

    Experiment findById(int id);

    Iterable<Experiment> findAllByVisitors(VisitorExperiment v);

    Experiment findByName(String name);

}
