package application.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseController {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseController.class);

    private static DatabaseController INSTANCE;

    private HikariDataSource dataSource;
    private JdbcDatabaseContainer container;

    private DatabaseController(final JdbcDatabaseContainer container) {

        this.container = container;

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(this.container.getJdbcUrl());
        hikariConfig.setUsername(this.container.getUsername());
        hikariConfig.setPassword(this.container.getPassword());

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public static void newConnectionPool(final JdbcDatabaseContainer container) {

        if (INSTANCE == null) {
            INSTANCE = new DatabaseController(container);
        }
    }

    public static DatabaseController getInstance() {

        return INSTANCE;
    }

    public void executeQuery(final String queryString) {

        try (Connection dbConnection = this.dataSource.getConnection()) {

            Statement query = CCJSqlParserUtil.parse(queryString);
            PreparedStatement statement = dbConnection.prepareStatement(query.toString());
            statement.execute();

        } catch (SQLException | JSQLParserException e) {
            LOG.error("Failed to execute statement '{}' with error {}", queryString, e.getMessage());
        }
    }

    public ResultSet executeQuery(final String queryString, final Connection dbConnection)
            throws JSQLParserException, SQLException {

        Statement query = CCJSqlParserUtil.parse(queryString);

        PreparedStatement statement = dbConnection.prepareStatement(query.toString());
        statement.execute();

        return statement.getResultSet();
    }

    public void cleanDatabase() {

        String selectTables = this.getTablesQuery();
        ArrayList<String> tableNames = new ArrayList<>();

        try (Connection dbConnection = this.dataSource.getConnection()) {

            ResultSet resultSet = this.executeQuery(selectTables, dbConnection);

            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }

            /* Clear table if any */
            if (!tableNames.isEmpty()) {
                for (String tableName : tableNames) {
                    String truncateStatement = String.format("TRUNCATE TABLE %s CASCADE", tableName);
                    this.executeQuery(truncateStatement, dbConnection);
                }
            }

        } catch (SQLException | JSQLParserException e) {
            LOG.error("Cleanup error. Failed to fetch table names: {}", e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {

        return this.dataSource.getConnection();
    }

    public void shutdown() {

        this.dataSource.close();
        INSTANCE = null;
    }

    private String getTablesQuery() {

        return "SELECT table_name FROM information_schema.tables WHERE table_schema='public' " +
                "AND table_type='BASE TABLE'";
    }
}
