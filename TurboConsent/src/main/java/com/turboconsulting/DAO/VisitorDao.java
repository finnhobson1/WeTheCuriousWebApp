package com.turboconsulting.DAO;

import com.turboconsulting.Entity.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Repository
@Qualifier("sqlVisitorData")
public interface VisitorDao extends CrudRepository<Visitor, Integer> {

    Visitor findByVisitorId(int id);

    Iterable<Visitor> findAllByAccount(Account account);

}
