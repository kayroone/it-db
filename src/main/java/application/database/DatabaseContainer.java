package application.database;

import domain.DatabaseVendor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainer {

    private JdbcDatabaseContainer CONTAINER;

    public DatabaseContainer(final IntegrationTestDatabase config) {

        if (DatabaseVendor.POSTGRESQL == config.getDatabaseVendor()) {

            CONTAINER = new PostgreSQLContainer(config.getDatabaseVendor().getImageName())
                    .withDatabaseName(config.getDatabaseName())
                    .withUsername(config.getUsername())
                    .withPassword(config.getPassword());

        } else if (DatabaseVendor.MYSQL == config.getDatabaseVendor()) {

            CONTAINER = new MySQLContainer(config.getDatabaseVendor().getImageName())
                    .withDatabaseName(config.getDatabaseName())
                    .withUsername(config.getUsername())
                    .withPassword(config.getPassword());
        }

        /* Create global connection pool */
        DatabaseController.newConnectionPool(CONTAINER);
    }
}
