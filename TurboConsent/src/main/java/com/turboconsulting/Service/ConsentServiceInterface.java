package com.turboconsulting.Service;

import com.turboconsulting.Entity.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface ConsentServiceInterface {


    //////////////////////////////////////////////////////////////////////////ACCOUNT FUNCTIONS

    int getAccountID(String email);

    Iterable<Visitor> getAccountsVisitors(int aID);

    Account getAccount(int id);

    boolean updateAccountConsent(List<Integer> vIds, ConsentOption c);

    //////////////////////////////////////////////////////////////////////////VISITOR FUNCTIONS

    Visitor getVisitor(int id);

    boolean updateVisitorConsent(int id, ConsentOption c);

    //////////////////////////////////////////////////////////////////////////EXPERIMENT FUNCTIONS

    Experiment getExperiment(int id);


    Iterable<ConsentOption> getExperimentsConsentOptions(int eId);


    //////////////////////////////////////////////////////////////////////////VISITOR_EXPERIMENT FUNCTIONS
    Iterable<VisitorExperiment> getVisitorExperiments(int id);

    VisitorExperiment getVisitorExperiment(int visitorID, int experimentID);

    String getExperimentConsent(int id, int experimentID);

    boolean updateExperimentConsent(int visitorId, ConsentOption c, int experimentID);

    boolean updateBatchExperimentConsents(int visitorId, ConsentOption c, List<Integer> experimentIds);

    int getPendingExperiments(int id);

    boolean moveAllToReviewed(int visitorId);
}
