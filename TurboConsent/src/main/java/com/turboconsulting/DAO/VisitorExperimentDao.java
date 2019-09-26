package com.turboconsulting.DAO;

import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Entity.Visitor;
import com.turboconsulting.Entity.VisitorExperiment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Qualifier("sqlVisitorExperimentData")
public interface VisitorExperimentDao extends CrudRepository<VisitorExperiment, Integer>{

    VisitorExperiment findByVisitorAndExperiment(Visitor v, Experiment e);

    Iterable<VisitorExperiment> findAllByVisitor(Visitor v);

}
