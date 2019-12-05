import application.database.Initialize;
import application.database.IntegrationTestDatabase;
import application.dataset.TestDataSet;
import domain.DatabaseVendor;

import java.sql.ResultSet;

public class IntegrationTestPostgresDatabaseIT {

    @Initialize
    private IntegrationTestDatabase integrationTestDatabase = IntegrationTestDatabase.newDatabaseContainer()
            .withDatabaseVendor(DatabaseVendor.POSTGRESQL)
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .build();

    @TestDataSet(file = "datasets/test-insert.sql", cleanBefore = true)
    public void executeTestInsertStatement() {

        ResultSet resultSet = this.integrationTestDatabase.performQuery();
    }
}
