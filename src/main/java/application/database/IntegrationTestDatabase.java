package application.database;

import domain.DatabaseVendor;

public class IntegrationTestDatabase {

    private DatabaseContainer databaseContainer;

    private DatabaseVendor databaseVendor;
    private String databaseName;
    private String username;
    private String password;

    private IntegrationTestDatabase() {
    }

    public static IntegrationTestDatabase newDatabaseContainer() {

        return new IntegrationTestDatabase();
    }

    public IntegrationTestDatabase build() {

        this.databaseContainer = new DatabaseContainer(this);
        return this;
    }

    public IntegrationTestDatabase withDatabaseVendor(final DatabaseVendor databaseVendor) {

        this.databaseVendor = databaseVendor;
        return this;
    }

    public IntegrationTestDatabase withDatabaseName(final String databaseName) {

        this.databaseName = databaseName;
        return this;
    }

    public IntegrationTestDatabase withUsername(final String username) {

        this.username = username;
        return this;
    }

    public IntegrationTestDatabase withPassword(final String password) {

        this.password = password;
        return this;
    }

    public DatabaseContainer getDatabaseContainer() {

        return this.databaseContainer;
    }

    DatabaseVendor getDatabaseVendor() {

        return this.databaseVendor;
    }

    String getDatabaseName() {

        return this.databaseName;
    }

    String getUsername() {

        return this.username;
    }

    String getPassword() {

        return this.password;
    }
}
