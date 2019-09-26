import com.turboconsulting.Service.AdminService;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//JUnit Suite Test
@RunWith(Suite.class)

@Suite.SuiteClasses({
        AccountServiceTests.class,
        VisitorServiceTests.class,
        ExperimentServiceTests.class,
        VisitorExperimentServiceTests.class
})

public class JunitTestSuite {

}