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
public class AdminService implements AdminServiceInterface {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
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



    @Override
    @PostConstruct
    public void AdminService() {

        accountDao.deleteAll();
        experimentDao.deleteAll();
        consentOptionDao.deleteAll();
        consentExperimentDao.deleteAll();

        consentOptionDao.save(new ConsentOption("Full Consent",
                "You agree to allow WTC and the researchers involved in this experiment to view and use your experiment results and your details."));
        consentOptionDao.save(new ConsentOption("No Consent",
                "You do not agree to allow anyone to view or use your experiment results or any of your details."));

        Account account1 = new Account("Admin", "admin@turboconsent.com", bCryptPasswordEncoder.encode("tcadmin123"));
        addNewAccount(account1);

    }


    //////////////////////////////////////////////////////////////////////////ACCOUNT FUNCTIONS
    @Override
    public boolean addNewAccount(Account a)  {
        return (accountDao.findByEmail(a.getEmail()) == null) && (accountDao.save(a) != null);
    }
    @Override
    public Iterable<Account> getAllAccounts(){
        return accountDao.findAll();
    }
    @Override
    public boolean deleteAccount(int accountId) {
        boolean successful = true;
        Collection<Visitor> visitors = new ArrayList<>();
        visitors.addAll(accountDao.findByAccountId(accountId).getVisitors());
        for(Visitor visitor : visitors)  {
            successful = successful & deleteVisitor(visitor.getVisitorId());
        }
        accountDao.delete(accountId);
        return successful;
    }

    //////////////////////////////////////////////////////////////////////////VISITOR FUNCTIONS
    @Override
    public boolean addNewVisitor(Visitor v, int accountID)  {
        v.setAccount(accountDao.findByAccountId(accountID));
        v.setDefaultConsent(consentOptionDao.findByName("No Consent"));
        consentOptionDao.findByName("No Consent").addVisitor(v);
        return visitorDao.save(v) != null;
    }
    @Override
    public Iterable<Visitor> getAllVisitors(){
        return visitorDao.findAll();
    }
    @Override
    public boolean deleteVisitor(int visitorId) {
        boolean successful = true;
        Visitor v = visitorDao.findByVisitorId(visitorId);
        Collection<VisitorExperiment> visitorExperiments = new ArrayList<>();
        visitorExperiments.addAll(v.getExperiments());
        for (VisitorExperiment visitorExperiment : visitorExperiments)  {
            successful = successful & deleteVisitorExperiment(visitorExperiment.getCompoundKey());
        }
        v.getAccount().removeVisitor(v);
        v.getDefaultConsent().removeVisitor(v);
        visitorDao.delete(visitorId);
        return successful;
    }

    //////////////////////////////////////////////////////////////////////////EXPERIMENT FUNCTIONS
    @Override
    public boolean addNewExperiment(Experiment e, HashSet<ConsentOption> newConsentOptions){
        if( experimentDao.findByName(e.getName()) != null  )  return false;

        Set<ConsentExperiment> consentExperiments = new HashSet<>();
        consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName("Full Consent"), e));
        consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName("No Consent"), e));

        for (ConsentOption c : newConsentOptions)  {
            if (consentOptionDao.findByName(c.getName()) == null)  {
                consentOptionDao.save(c);
            }
            consentExperiments.add(new ConsentExperiment(consentOptionDao.findByName(c.getName()), e));
        }
        e.setConsentExperiments(consentExperiments);
        experimentDao.save(e);
        for(ConsentExperiment consentExperiment : consentExperiments)  {
            consentExperiment.getConsentOption().addConsentExperiment(consentExperiment);
            consentOptionDao.save(consentExperiment.getConsentOption());
        }
        return true;
    }
    @Override
    public Iterable<Experiment> getAllExperiments(){
        return experimentDao.findAll();
    }
    @Override
    public boolean deleteExperiment(int experimentId) {
        Experiment e = experimentDao.findById(experimentId);

        Collection<VisitorExperiment> visitorExperiments = new ArrayList<>();
        visitorExperiments.addAll(e.getVisitors());
        for(VisitorExperiment visitorExperiment: visitorExperiments)  {
            deleteVisitorExperiment(visitorExperiment.getCompoundKey());
        }

        Collection<ConsentExperiment> consentExperiments = new ArrayList<>();
        consentExperiments.addAll(e.getConsentExperiments());
        for(ConsentExperiment consentExperiment : consentExperiments)  {
            deleteConsentExperiment(consentExperiment.getCompoundKey());
        }

        experimentDao.delete(experimentId);
        return true;
    }

    //////////////////////////////////////////////////////////////////////////VISITOR_EXPERIMENT FUNCTIONS
    @Override
    public ArrayList<VisitorExperiment> getVisitorExperiments(int id)  {
        ArrayList<VisitorExperiment> visitorExperimentsList = new ArrayList<>();
        Iterable<VisitorExperiment> visitorExperimentsIterable = visitorExperimentDao.findAllByVisitor(visitorDao.findByVisitorId(id));

        visitorExperimentsIterable.forEach(visitorExperimentsList::add);
        visitorExperimentsList.sort((ve1, ve2) -> ve2.getDate().compareTo(ve1.getDate()));

        return visitorExperimentsList;
    }

    @Override
    public boolean addVisitorExperiment(int visitorId, int experimentId)  {
        VisitorExperiment visitorExperiment = new VisitorExperiment( visitorDao.findByVisitorId(visitorId),
                experimentDao.findById(experimentId));

        experimentDao.findById(experimentId).addVisitorExperiment(visitorExperiment);
        visitorDao.findByVisitorId(visitorId).doExperiment(visitorExperiment);
        visitorExperiment.getConsentOption().addExperiment(visitorExperiment);
        //consentOptionDao.save(visitorExperiment.getConsentOption());
        return visitorExperimentDao.save(visitorExperiment) != null;
    }
    @Override
    public Iterable<VisitorExperiment> getAllVisitorExperiments()  {
        return visitorExperimentDao.findAll();
    }
    @Override
    public boolean deleteVisitorExperiment(int visitorExperimentId) {
        visitorExperimentDao.findOne(visitorExperimentId).getExperiment().removeVisitor(visitorExperimentDao.findOne(visitorExperimentId));
        visitorExperimentDao.findOne(visitorExperimentId).getVisitor().removeExperiment(visitorExperimentDao.findOne(visitorExperimentId));
        visitorExperimentDao.findOne(visitorExperimentId).getConsentOption().removeVisitorExperiment(visitorExperimentDao.findOne(visitorExperimentId));
        visitorExperimentDao.delete(visitorExperimentId);
        return true;
    }
    @Override
    public boolean deleteConsentExperiment(int consentExperimentId)  {
        consentExperimentDao.findOne(consentExperimentId).getConsentOption().removeConsentExperiment(consentExperimentDao.findOne(consentExperimentId));
        consentExperimentDao.findOne(consentExperimentId).getExperiment().removeConsentExperiment(consentExperimentDao.findOne(consentExperimentId));
        consentExperimentDao.delete(consentExperimentId);
        return true;
    }
}
