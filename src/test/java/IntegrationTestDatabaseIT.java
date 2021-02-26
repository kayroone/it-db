import application.IntegrationTestDatabase;
import application.container.ContainerNotRunningException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntegrationTestDatabaseIT {

    @BeforeAll
    public void setUp() throws ContainerNotRunningException {

        IntegrationTestDatabase integrationTestDatabase = TestUtils.startPostgresDatabase();
    }

    @Test
    public void startWithMySqlImage() {


        // WRITE UNIT TESTS!

    }
}
