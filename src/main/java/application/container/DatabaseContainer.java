package application.container;

import application.IntegrationTestDatabase;
import application.database.DatabaseConnectionPool;
import domain.database.DatabaseVendor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainer {

    private final IntegrationTestDatabase config;
    private JdbcDatabaseContainer container;
    private DatabaseConnectionPool connectionPool;

    public DatabaseContainer(final IntegrationTestDatabase config) {

        this.config = config;
    }

    public void start() throws ContainerNotRunningException {

        try {
            /* Init container based on database vendor */
            if (DatabaseVendor.POSTGRESQL == config.getDatabaseVendor()) {
                initPostgresContainer();
            } else if (DatabaseVendor.MYSQL == config.getDatabaseVendor()) {
                initMySqlContainer();
            }

            /* Start container */
            container.start();

            /* Initialize connection pool */
            connectionPool = new DatabaseConnectionPool(container);
            connectionPool.initConnectionPool();

        } catch (Exception e) {
            throw new ContainerNotRunningException("Failed to start container with image "
                    + config.getImageName() + ". Failed at: " + e.getMessage());
        }
    }

    public JdbcDatabaseContainer getContainer() {

        return container;
    }

    public DatabaseConnectionPool getConnectionPool() {

        return connectionPool;
    }

    private void initPostgresContainer() {

        container = new PostgreSQLContainer(config.getImageName())
                .withInitScript(config.getInitScriptPath())
                .withDatabaseName(config.getDatabaseName())
                .withUsername(config.getUsername())
                .withPassword(config.getPassword());
    }

    private void initMySqlContainer() {

        container = new MySQLContainer(config.getImageName())
                .withInitScript(config.getInitScriptPath())
                .withDatabaseName(config.getDatabaseName())
                .withUsername(config.getUsername())
                .withPassword(config.getPassword());
    }
}
