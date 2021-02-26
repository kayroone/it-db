import application.IntegrationTestDatabase;
import application.container.ContainerNotRunningException;
import application.database.DatabaseController;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseController.class);

    private final static String MYSQL = "mysql";
    private final static String POSTGRESQL = "postgres";

    public static IntegrationTestDatabase startMySqlDatabase() throws ContainerNotRunningException {

        return createDatabase(MYSQL);
    }

    public static IntegrationTestDatabase startPostgresDatabase() throws ContainerNotRunningException {

        return createDatabase(POSTGRESQL);
    }

    private static IntegrationTestDatabase createDatabase(final String engine) throws ContainerNotRunningException {

        IntegrationTestDatabase integrationTestDatabase = IntegrationTestDatabase.newPostgresContainer();

        /* Inject mandatory properties */
        try {
            FieldUtils.writeField(integrationTestDatabase, "imageName", engine, true);
        } catch (IllegalAccessException e) {
            LOG.error("Failed to set image at: {}", e.getMessage());
        }

        /* Build test container for this test */
        integrationTestDatabase.withInitScript("datasets/mysql/init.sql")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .build();

        return integrationTestDatabase;
    }


}
