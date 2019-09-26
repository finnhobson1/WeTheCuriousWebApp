import com.turboconsulting.DAO.*;
import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.ConsentExperiment;
import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Service.ConsentService;
import com.turboconsulting.Service.ConsentServiceInterface;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class ExperimentServiceTests {

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
        ArrayList<Experiment> experiments = new ArrayList<>();
        ArrayList<ConsentOption> consentOptions = new ArrayList<>();

        experiments.add(mockEntityFactory.mockExperiment( "Physics Experiment", "Description 1", 1));
        experiments.add(mockEntityFactory.mockExperiment( "Chemistry Experiment", "Description 2", 2));

        consentOptions.add(mockEntityFactory.mockConsentOption("No Consent", " ",  1));
        consentOptions.add(mockEntityFactory.mockConsentOption("Full Consent", " ",  2));

        experiments.get(0).addConsentOption(new ConsentExperiment(consentOptions.get(0), experiments.get(0)));
        experiments.get(0).addConsentOption(new ConsentExperiment(consentOptions.get(1), experiments.get(0)));

//        Mockito.when(experimentDao.save(any(Experiment.class))).thenAnswer(AdditionalAnswers.<Account>returnsFirstArg());
//        Mockito.when(experimentDao.findAll()).thenReturn(experiments);
    }

    @Test
    public void getExperiment_withValidId() {
        Experiment found = consentService.getExperiment(1);
        assertEquals(found.getName(), "Physics Experiment");
        found = consentService.getExperiment(2);
        assertEquals(found.getName(), "Chemistry Experiment");

    }
    @Test
    public void getExperiment_withInvalidId() {
        assertEquals(consentService.getExperiment(-1), null);
        assertEquals(consentService.getExperiment(0), null);
        assertEquals(consentService.getExperiment(1000), null);

    }
    @Test
    public void getExperimentsConsentOptions()  {
        ArrayList<ConsentOption> consentOptions = Lists.newArrayList(consentService.getExperimentsConsentOptions(1));
        if(consentOptions.get(0).getName().equals("Full Consent"))  {
            assertEquals("No Consent", consentOptions.get(1).getName());

        }else{
            assertEquals("Full Consent", consentOptions.get(1).getName());
        }
    }

}
