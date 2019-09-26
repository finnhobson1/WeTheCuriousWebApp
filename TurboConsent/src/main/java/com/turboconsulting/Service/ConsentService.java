package com.turboconsulting.Service;

import com.turboconsulting.DAO.*;
import com.turboconsulting.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ConsentService implements ConsentServiceInterface {

    @Autowired
    @Qualifier("sqlVisitorData")
    private VisitorDao visitorDao;

    @Autowired
    @Qualifier("sqlVisitorExperimentData")
    private VisitorExperimentDao visitorExperimentDao;

    @Autowired
    @Qualifier("sqlAccountData")
    private AccountDao accountDao;

    @Autowired
    @Qualifier("sqlExperimentData")
    private ExperimentDao experimentDao;

    @Autowired
    @Qualifier("sqlConsentData")
    private ConsentOptionDao consentOptionDao;

    @Autowired
    @Qualifier("sqlConsentExperimentData")
    private ConsentExperimentDao consentExperimentDao;



    //////////////////////////////////////////////////////////////////////////ACCOUNT FUNCTIONS
    @Override
    public int getAccountID(String email)  {
        if(accountDao.findByEmail(email) != null)
            return accountDao.findByEmail(email).getAccountId();
        return -1;
    }
    @Override
    public Iterable<Visitor> getAccountsVisitors(int aID) {
        return visitorDao.findAllByAccount(getAccount(aID));
    }
    @Override
    public Account getAccount(int id)  {
        return accountDao.findByAccountId(id);
    }
    @Override
    public boolean updateAccountConsent(List<Integer> vIds, ConsentOption c)  {
        for (int vId : vIds)  {
            updateVisitorConsent(vId, c);
        }
        return true;
    }


    //////////////////////////////////////////////////////////////////////////VISITOR FUNCTIONS
    @Override
    public Visitor getVisitor(int id)  {
        return visitorDao.findByVisitorId(id);
    }
    @Override
    public boolean updateVisitorConsent(int id, ConsentOption c)  {
        Visitor v = getVisitor(id);
        if(consentOptionDao.findByName(c.getName()) == null)  return false;
        if(!c.getName().equals("No Consent") && !c.getName().equals("Full Consent"))  return false;
        v.setDefaultConsent(consentOptionDao.findByName(c.getName()));
        return visitorDao.save(v) != null;
    }


    //////////////////////////////////////////////////////////////////////////EXPERIMENT FUNCTIONS
    @Override
    public Experiment getExperiment(int id)  {
        return experimentDao.findById(id);
    }
    @Override
    public Iterable<ConsentOption> getExperimentsConsentOptions(int eId)  {
        ArrayList<ConsentOption> consentOptions = new ArrayList<>();
        Experiment experiment = experimentDao.findById(eId);
        for (ConsentExperiment consentExperiment : experiment.getConsentExperiments())  {
            consentOptions.add(consentExperiment.getConsentOption());
        }
        return consentOptions;

    }


    //////////////////////////////////////////////////////////////////////VISITOR_EXPERIMENT FUNCTIONS
    @Override
    public ArrayList<VisitorExperiment> getVisitorExperiments(int id)  {
        ArrayList<VisitorExperiment> visitorExperimentsList = new ArrayList<>();
        Iterable<VisitorExperiment> visitorExperimentsIterable = visitorExperimentDao.findAllByVisitor(visitorDao.findByVisitorId(id));

        visitorExperimentsIterable.forEach(visitorExperimentsList::add);
        visitorExperimentsList.sort((ve1, ve2) -> ve2.getDate().compareTo(ve1.getDate()));

        return visitorExperimentsList;
    }

    @Override
    public VisitorExperiment getVisitorExperiment(int visitorID, int experimentID)  {
        ArrayList<Experiment> experiments = new ArrayList<>();
        Visitor v = visitorDao.findByVisitorId(visitorID);
        Experiment e = experimentDao.findById(experimentID);
        return visitorExperimentDao.findByVisitorAndExperiment(v, e);
    }
    @Override
    public String getExperimentConsent(int visitorID, int experimentID)  {
        Visitor v = visitorDao.findByVisitorId(visitorID);
        Experiment e = experimentDao.findById(experimentID);
        VisitorExperiment visitorExperiment = visitorExperimentDao.findByVisitorAndExperiment(v, e);
        return visitorExperiment == null ? "NULL" : visitorExperiment.getConsentOption().getName();
    }
    @Override
    public boolean updateExperimentConsent(int visitorId, ConsentOption newConsentOption, int experimentID)  {
        Visitor v = visitorDao.findByVisitorId(visitorId);
        Experiment e = experimentDao.findById(experimentID);
        if ( v == null || e == null )  return false;

        VisitorExperiment visitorExperiment = visitorExperimentDao.findByVisitorAndExperiment(v, e);
        for(ConsentExperiment c : e.getConsentExperiments())  {
            if(c.getConsentOption().getName().equals(newConsentOption.getName()))
                visitorExperiment.setConsentOption(c.getConsentOption());
        }
        return visitorExperimentDao.save(visitorExperiment) != null;
    }
    @Override
    public boolean updateBatchExperimentConsents(int visitorId, ConsentOption c, List<Integer> experimentIds)  {
        boolean batchSuccess = true;
        for (int eID : experimentIds)  batchSuccess = updateExperimentConsent(visitorId, c, eID) && batchSuccess;
        return batchSuccess;
    }
    @Override
    public int getPendingExperiments(int visitorId)  {
        return visitorDao.findByVisitorId(visitorId).getPendingExperiments();

    }
    @Override
    public boolean moveAllToReviewed(int visitorId)  {
        Iterable<VisitorExperiment> visitorExperiments = visitorExperimentDao.findAllByVisitor(getVisitor(visitorId));
        for(VisitorExperiment ve : visitorExperiments)  {
            ve.setChangedConsent(true);
            visitorExperimentDao.save(ve);
        }
        return true;
    }

}
