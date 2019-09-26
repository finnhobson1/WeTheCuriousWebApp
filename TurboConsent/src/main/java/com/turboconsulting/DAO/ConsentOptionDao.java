package com.turboconsulting.DAO;

import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.ConsentOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("sqlConsentData")
public interface ConsentOptionDao extends CrudRepository<ConsentOption, Integer> {
    ConsentOption findByName(String name);

    ConsentOption findByConsentId(int consentId);
}
