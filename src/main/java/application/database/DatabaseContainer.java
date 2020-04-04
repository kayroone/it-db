package application.database;

import domain.database.DatabaseVendor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainer {

    private JdbcDatabaseContainer container;

    public DatabaseContainer(final IntegrationTestDatabase config) {

        if (DatabaseVendor.POSTGRESQL == config.getDatabaseVendor()) {

            container = new PostgreSQLContainer(config.getImageName())
                    .withInitScript(config.getInitScriptPath())
                    .withDatabaseName(config.getDatabaseName())
                    .withUsername(config.getUsername())
                    .withPassword(config.getPassword());

        } else if (DatabaseVendor.MYSQL == config.getDatabaseVendor()) {

            container = new MySQLContainer(config.getImageName())
                    .withInitScript(config.getInitScriptPath())
                    .withDatabaseName(config.getDatabaseName())
                    .withUsername(config.getUsername())
                    .withPassword(config.getPassword());
        }

        /* Start container and initiate controller with connection pool */
        if (null != container) {
            container.start();
            DatabaseController.newInstance(container);
        }
    }

    public JdbcDatabaseContainer getContainer() {

        return this.container;
    }
}
