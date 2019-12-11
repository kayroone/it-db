import application.database.Initialize;
import application.database.IntegrationTestDatabase;
import application.dataset.TestDataSet;
import domain.database.DatabaseVendor;
import domain.result.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IntegrationTestPostgresDatabaseIT {

    @Initialize
    private IntegrationTestDatabase integrationTestDatabase = IntegrationTestDatabase.newDatabaseContainer()
            .withDatabaseVendor(DatabaseVendor.POSTGRESQL)
            .withInitScript("datasets/init.sql")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .build();

    @Test
    public void executeSelectStatement() {

        Result result = integrationTestDatabase.executeQuery("SELECT 1");

        assertEquals(result.getRow(0).get(0), "1");
    }

    @TestDataSet(file = "datasets/test-insert.sql", cleanBefore = true)
    public void executeInsertStatement() {

        Result result = integrationTestDatabase.executeQuery("SELECT * FROM test_table");

        assertFalse(result.getRow(0).isEmpty());
    }
}
