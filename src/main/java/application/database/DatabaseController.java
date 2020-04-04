package application.database;

import domain.result.Result;
import net.sf.jsqlparser.JSQLParserException;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseController {

    private static DatabaseController INSTANCE;

    private DatabaseConnectionPool connectionPool;
    private JdbcDatabaseContainer container;

    private DatabaseController(final JdbcDatabaseContainer container) {

        this.container = container;
        this.connectionPool = new DatabaseConnectionPool(container);
    }

    public static DatabaseController getInstance() {

        return INSTANCE;
    }

    public static DatabaseController newInstance(final JdbcDatabaseContainer container) {

        if (INSTANCE == null) {
            INSTANCE = new DatabaseController(container);
        }

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

        if (this.container.isRunning()) {
            this.container.stop();
        }

        INSTANCE = null;
    }

    public Result executeStatement(final String statement) {

        return this.connectionPool.executeStatement(statement);
    }

    public void executeStatement(final String statement, final Connection connection) throws SQLException, JSQLParserException {

        this.connectionPool.executeStatement(statement, connection);
    }
}
