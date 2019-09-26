import com.turboconsulting.DAO.*;
import com.turboconsulting.Entity.*;
import com.turboconsulting.Service.ConsentService;
import com.turboconsulting.Service.ConsentServiceInterface;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class VisitorExperimentServiceTests {

    @Autowired
    private ConsentService consentService;
    @MockBean
    private AccountDao accountDao;
    @MockBean
    private ExperimentDao experimentDao;
    @MockBean
    private VisitorDao visitorDao;
    @MockBean
    private VisitorExperimentDao visitorExperimentDao;
    @MockBean
    private ConsentOptionDao consentOptionDao;
    @MockBean
    private ConsentExperimentDao consentExperimentDao;


    @TestConfiguration
    static class ConsentServiceImplTestContextConfiguration {

        @Bean
        public ConsentServiceInterface consentService(){
            return new ConsentService();
        }

    }

    @Before
    public void setup() {
        MockEntityFactory mockEntityFactory = new MockEntityFactory( accountDao,
                 experimentDao,
                 visitorDao,
                 visitorExperimentDao,
                 consentOptionDao,
                 consentExperimentDao);
        ArrayList<Account> accounts = new ArrayList<>();
        ArrayList<Visitor> visitors = new ArrayList<>();
        ArrayList<Experiment> experiments = new ArrayList<>();
        ArrayList<ConsentOption> consentOptions = new ArrayList<>();
        Set<VisitorExperiment> visitorExperiments = new HashSet<>();

        accounts.add(mockEntityFactory.mockAccount( "Harry", "harry@bristol.ac.uk", "password", 1));
        accounts.add(mockEntityFactory.mockAccount( "Finn", "finn@bristol.ac.uk", "password", 2));
        accounts.add(mockEntityFactory.mockAccount( "Yeap", "yeap@bristol.ac.uk", "password", 3));


        visitors.add(mockEntityFactory.mockVisitor("Harry Visitor 1", 1, accounts.get(0)));
        visitors.add(mockEntityFactory.mockVisitor( "Harry Visitor 2", 2, accounts.get(0)));


        Mockito.when(visitorDao.findAllByAccount(any(Account.class))).thenAnswer(new Answer<Set<Visitor>>() {
            @Override
            public Set<Visitor> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return ((Account) args[0]).getVisitors();
            }
        });

        consentOptions.add(mockEntityFactory.mockConsentOption("No Consent", "Description",  1));
        consentOptions.add(mockEntityFactory.mockConsentOption("Full Consent", "Description",  2));



        experiments.add(mockEntityFactory.mockExperiment("Physics Experiment", "Description 1", 1));
        experiments.add(mockEntityFactory.mockExperiment("Chemistry Experiment", "Description 2", 2));

        experiments.get(0).addConsentOption(new ConsentExperiment(consentOptions.get(0), experiments.get(0)));
        experiments.get(0).addConsentOption(new ConsentExperiment(consentOptions.get(1), experiments.get(0)));

        VisitorExperiment visitorExperiment = mockEntityFactory.mockVisitorExperiment(1, 1, 1);
        visitorExperiments.add(visitorExperiment);
        visitorExperiment = mockEntityFactory.mockVisitorExperiment(1, 2, 2);
        visitorExperiments.add(visitorExperiment);
        visitors.get(0).setExperiments(visitorExperiments);




    }

    @Test
    public void getVisitorExperiments_success()  {
        Set<VisitorExperiment> experiments = Sets.newHashSet(consentService.getVisitorExperiments(1));

        assertEquals(experiments.contains(visitorExperimentDao.findOne(1)), true );
        assertEquals(experiments.contains(visitorExperimentDao.findOne(2)), true );
    }
    @Test
    public void getExperimentConsent_success()  {
        assertEquals(consentService.getExperimentConsent(1, 1), "No Consent");

    }
    @Test
    public void getExperimentConsent_failure()  {
        assertEquals(consentService.getExperimentConsent(1000, 1), "NULL");
        assertEquals(consentService.getExperimentConsent(-1, 1000), "NULL");
        assertEquals(consentService.getExperimentConsent(1000, -1), "NULL");
        assertEquals(consentService.getExperimentConsent(-1, -1), "NULL");

    }

    @Test
    public void updateExperimentConsent_success()  {
        assertEquals(consentService.getExperimentConsent(1, 1), "No Consent");
        consentService.updateExperimentConsent(1, consentOptionDao.findByName("Full Consent"), 1);
        assertEquals(consentService.getExperimentConsent(1, 1), "Full Consent");
    }
    @Test
    public void updateExperimentConsent_failure()  {
        assertEquals(consentService.getExperimentConsent(1, 1), "No Consent");
        assertFalse(consentService.updateExperimentConsent(1, new ConsentOption("Invalid Consent", "Description"), -1));
        assertEquals(consentService.getExperimentConsent(1, 1), "No Consent");
    }

}
