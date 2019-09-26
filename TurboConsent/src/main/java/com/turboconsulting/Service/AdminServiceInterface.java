package com.turboconsulting.Service;

import com.turboconsulting.Entity.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;

public interface AdminServiceInterface {

    @PostConstruct
    void AdminService();

    //////////////////////////////////////////////////////////////////////////ACCOUNT FUNCTIONS
    boolean addNewAccount(Account a);
    Iterable<Account> getAllAccounts();
    boolean deleteAccount(int accountId);

    //////////////////////////////////////////////////////////////////////////VISITOR FUNCTIONS
    boolean addNewVisitor(Visitor v, int accountID);
    Iterable<Visitor> getAllVisitors();
    boolean deleteVisitor(int visitorId);

    //////////////////////////////////////////////////////////////////////////EXPERIMENT FUNCTIONS
    boolean addNewExperiment(Experiment e, HashSet<ConsentOption> newConsentOptions);
    Iterable<Experiment> getAllExperiments();
    boolean deleteExperiment(int experimentId);

    //////////////////////////////////////////////////////////////////////////VISITOR_EXPERIMENT FUNCTIONS
    boolean addVisitorExperiment(int visitorId, int experimentId);
    Iterable<VisitorExperiment> getVisitorExperiments(int id);
    Iterable<VisitorExperiment>getAllVisitorExperiments();
    boolean deleteVisitorExperiment(int visitorExperimentId);


    //////////////////////////////////////////////////////////////////////////CONSENT_EXPERIMENT FUNCTIONS
    boolean deleteConsentExperiment(int consentExperimentId);

}
