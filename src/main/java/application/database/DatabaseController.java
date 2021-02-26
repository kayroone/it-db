package application.database;

import application.IntegrationTestDatabase;
import application.container.DatabaseContainer;
import domain.result.Result;
import net.sf.jsqlparser.JSQLParserException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseController {

    private static DatabaseController INSTANCE;

    private DatabaseConnectionPool connectionPool;
    private DatabaseContainer container;

    private DatabaseController(final IntegrationTestDatabase config) {

        this.container = new DatabaseContainer(config);
        this.connectionPool = container.getConnectionPool();
    }

    public static DatabaseController createInstance(IntegrationTestDatabase config) {

        if (INSTANCE == null) {
            INSTANCE = new DatabaseController(config);
        }

        return INSTANCE;
    }

    public static DatabaseController getInstance() {

        return INSTANCE;
    }

    public Connection getDatabaseConnection() throws SQLException {

        return this.connectionPool.getConnection();
    }

    public Connection closeDatabaseConnection() throws SQLException {

        return this.connectionPool.getConnection();
    }

    public void cleanDatabase() {

        this.connectionPool.cleanDatabase();
    }

    public void shutdownDatabase() {

        if (this.container.getContainer().isRunning()) {
            this.container.getContainer().stop();
        }

        INSTANCE = null;
    }

    public Result executeStatement(final String statement) {

        return this.connectionPool.executeStatement(statement);
    }

    public void executeStatement(final String statement, final Connection connection)
            throws SQLException, JSQLParserException {

        this.connectionPool.executeStatement(statement, connection);
    }
}
