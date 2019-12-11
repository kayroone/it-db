package application.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import domain.result.Result;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

    public Connection getConnection() throws SQLException {

        return this.dataSource.getConnection();
    }

    public void shutdown() {

        if (this.dataSource.isRunning()) {
            this.dataSource.close();
        }
        if (this.container.isRunning()) {
            this.container.stop();
        }

        INSTANCE = null;
    }

    public Result executeQuery(final String query) {

        Result result = new Result();

        try (Connection dbConnection = this.dataSource.getConnection()) {

            ResultSet resultSet = this.executeQuery(query, dbConnection);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            final int columnCount = resultSetMetaData.getColumnCount();

            /* Extract each row */
            while (resultSet.next()) {
                Object[] values = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    values[i - 1] = resultSet.getObject(i);
                }
                result.addRow(values);
            }

        } catch (SQLException | JSQLParserException e) {
            LOG.error("Cleanup error. Failed to fetch table names: {}", e.getMessage());
        }

        return result;
    }

    /**
     * Execute an SQL query on the running database container.
     * <p>
     * WARN: This method does not close the given database connection!
     *
     * @param query        The SQL query as string.
     * @param dbConnection A active database connection.
     * @return {@link ResultSet} containing the query rows if any.
     * @throws JSQLParserException For invalid SQL syntax.
     * @throws SQLException        For an invalid database connection.
     */
    public ResultSet executeQuery(final String query, final Connection dbConnection)
            throws JSQLParserException, SQLException {

        Statement statement = CCJSqlParserUtil.parse(query);

        PreparedStatement preparedStatement = dbConnection.prepareStatement(statement.toString());
        preparedStatement.execute();

        return preparedStatement.getResultSet();
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

    private String getTablesQuery() {

        return "SELECT table_name FROM information_schema.tables WHERE table_schema='public' " +
                "AND table_type='BASE TABLE'";
    }
}
