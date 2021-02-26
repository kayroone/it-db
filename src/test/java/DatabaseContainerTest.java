import application.IntegrationTestDatabase;
import application.container.ContainerNotRunningException;
import application.container.DatabaseContainer;
import domain.database.DatabaseVendor;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseContainerTest {

    private static final String DATABASE_NAME = "test";
    private static final String DATABASE_USERNAME = "test";
    private static final String DATABASE_PASSWORD = "test";

    @Mock
    private IntegrationTestDatabase config;

    @InjectMocks
    private DatabaseContainer databaseContainer;

    @AfterEach
    public void shutdown() {

        this.databaseContainer.getContainer().close();
    }

    @Test
    public void checkContainerRunningPostgres() throws ContainerNotRunningException {

        Mockito.when(this.config.getDatabaseVendor()).thenReturn(DatabaseVendor.POSTGRESQL);
        Mockito.when(this.config.getImageName()).thenReturn(DatabaseVendor.POSTGRESQL.getImageName());
        Mockito.when(this.config.getDatabaseName()).thenReturn(DATABASE_NAME);
        Mockito.when(this.config.getUsername()).thenReturn(DATABASE_USERNAME);
        Mockito.when(this.config.getPassword()).thenReturn(DATABASE_PASSWORD);

        this.databaseContainer.start();

        assertTrue(this.databaseContainer.getContainer().isRunning());
    }

    @Test
    public void checkContainerRunningMySql() throws ContainerNotRunningException {

        Mockito.when(this.config.getDatabaseVendor()).thenReturn(DatabaseVendor.MYSQL);
        Mockito.when(this.config.getImageName()).thenReturn(DatabaseVendor.MYSQL.getImageName());
        Mockito.when(this.config.getDatabaseName()).thenReturn(DATABASE_NAME);
        Mockito.when(this.config.getUsername()).thenReturn(DATABASE_USERNAME);
        Mockito.when(this.config.getPassword()).thenReturn(DATABASE_PASSWORD);

        this.databaseContainer.start();

        assertTrue(this.databaseContainer.getContainer().isRunning());
    }

    @Test
    public void checkContainerRunningWithInvalidImageNamePostgres() {

        Mockito.when(this.config.getDatabaseVendor()).thenReturn(DatabaseVendor.POSTGRESQL);
        Mockito.when(this.config.getImageName()).thenReturn("foobar");

        Assertions.assertThrows(ContainerNotRunningException.class, () -> this.databaseContainer.start());
    }

    @Test
    public void checkContainerRunningWithInvalidImageNameMySql() {

        Mockito.when(this.config.getDatabaseVendor()).thenReturn(DatabaseVendor.MYSQL);
        Mockito.when(this.config.getImageName()).thenReturn("foobar");

        Assertions.assertThrows(ContainerNotRunningException.class, () -> this.databaseContainer.start());
    }
}
