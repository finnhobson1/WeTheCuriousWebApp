import com.turboconsulting.DAO.*;
import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.Visitor;
import com.turboconsulting.Service.ConsentService;
import com.turboconsulting.Service.ConsentServiceInterface;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Set;

@RunWith(SpringRunner.class)
public class VisitorServiceTests {

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

        accounts.add(mockEntityFactory.mockAccount( "Harry", "harry@bristol.ac.uk", "password", 1));
        accounts.add(mockEntityFactory.mockAccount( "Finn", "finn@bristol.ac.uk", "password", 2));
        accounts.add(mockEntityFactory.mockAccount( "Yeap", "yeap@bristol.ac.uk", "password", 3));
        //Mockito.when(accountDao.save(any(Account.class))).thenAnswer(AdditionalAnswers.<Account>returnsFirstArg());
        //Mockito.when(accountDao.findAll()).thenReturn(accounts);

        visitors.add(mockEntityFactory.mockVisitor( "Harry Visitor 1", 1, accounts.get(0)));
        visitors.add(mockEntityFactory.mockVisitor( "Harry Visitor 2", 2, accounts.get(0)));
        //Mockito.when(visitorDao.save(any(Visitor.class))).thenAnswer(AdditionalAnswers.<Visitor>returnsFirstArg());
        //Mockito.when(visitorDao.findAll()).thenReturn(visitors);

        Mockito.when(visitorDao.findAllByAccount(any(Account.class))).thenAnswer(new Answer<Set<Visitor>>() {
            @Override
            public Set<Visitor> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return ((Account) args[0]).getVisitors();
            }
        });

        mockEntityFactory.mockConsentOption("No Consent", "Description",  1);
        mockEntityFactory.mockConsentOption("Full Consent", "Description",  2);

    }

    @Test
    public void getVisitor_withValidId() {
        Visitor found = consentService.getVisitor(1);
        assertEquals(found.getName(), "Harry Visitor 1");
        found = consentService.getVisitor(2);
        assertEquals(found.getName(), "Harry Visitor 2");

    }
    @Test
    public void getVisitor_withInvalidId() {
        assertEquals(consentService.getVisitor(-1), null);
        assertEquals(consentService.getVisitor(0), null);
        assertEquals(consentService.getVisitor(1000), null);

    }

    @Test
    public void updateVisitorConsent_validConsentLevel()  {
        consentService.updateVisitorConsent(1, new ConsentOption("Full Consent", " "));
        Visitor v = consentService.getVisitor(1);
        assertEquals("Full Consent", v.getDefaultConsent().getName());

    }
    @Test
    public void updateVisitorConsent_invalidConsentLevel()  {
        consentService.updateVisitorConsent(1, new ConsentOption("Invalid Consent", " "));
        Visitor v = consentService.getVisitor(1);
        assertEquals("No Consent", v.getDefaultConsent().getName());

    }
    @Test
    public void checkInitialVisitorConsent_isNoConsent()  {
        Visitor found = consentService.getVisitor(1);
        assertEquals("No Consent", found.getDefaultConsent().getName());
        found = consentService.getVisitor(2);
        assertEquals("No Consent", found.getDefaultConsent().getName());
    }


}
