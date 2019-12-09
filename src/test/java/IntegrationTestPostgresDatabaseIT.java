import application.database.DatabaseController;
import application.database.Initialize;
import application.database.IntegrationTestDatabase;
import application.dataset.TestDataSet;
import domain.DatabaseVendor;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void executeSelectStatement() throws SQLException, JSQLParserException {

        Connection connection = DatabaseController.getInstance().getConnection();
        ResultSet resultSet = DatabaseController.getInstance().executeQuery("SELECT 1", connection);

        resultSet.next();
        assertEquals(resultSet.getInt(1), 1);

        connection.close();
    }

    @TestDataSet(file = "datasets/test-insert.sql", cleanBefore = true)
    public void executeInsertStatement() throws SQLException, JSQLParserException {

        Connection connection = DatabaseController.getInstance().getConnection();
        ResultSet resultSet = DatabaseController.getInstance().executeQuery("SELECT * FROM test_table", connection);

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
        }

        connection.close();
    }
}
