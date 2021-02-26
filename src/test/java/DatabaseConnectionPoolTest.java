import application.database.DatabaseConnectionPool;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testcontainers.containers.JdbcDatabaseContainer;

public class DatabaseConnectionPoolTest {

    private static final String DATABASE_NAME = "test";
    private static final String DATABASE_USERNAME = "test";
    private static final String DATABASE_PASSWORD = "test";

    @Mock
    private JdbcDatabaseContainer container;

    @InjectMocks
    private DatabaseConnectionPool databaseConnectionPool;

    @Test
    public void checkContainerRunningPostgres() {

        Mockito.when(this.container.getDatabaseName()).thenReturn(DATABASE_NAME);
        Mockito.when(this.container.getUsername()).thenReturn(DATABASE_USERNAME);
        Mockito.when(this.container.getPassword()).thenReturn(DATABASE_PASSWORD);
        Mockito.when(this.container.getPassword()).thenReturn(DATABASE_PASSWORD);

        this.databaseConnectionPool.initConnectionPool();
    }
}
