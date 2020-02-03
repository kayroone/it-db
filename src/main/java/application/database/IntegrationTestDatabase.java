package application.database;

import application.property.Property;
import domain.database.DatabaseVendor;
import domain.result.Result;

import javax.inject.Inject;

public class IntegrationTestDatabase {

    private DatabaseContainer databaseContainer;

    private DatabaseVendor databaseVendor;

    @Inject
    @Property(key = "database.image.name")
    private String imageName;

    @Inject
    @Property(key = "database.port.number")
    private Integer port;

    @Inject
    @Property(key = "database.bind.address")
    private String bindAddress;

    @Inject
    @Property(key = "database.init.script")
    private String initScriptPath;

    @Inject
    @Property(key = "database.name")
    private String databaseName;

    @Inject
    @Property(key = "database.username")
    private String username;

    @Inject
    @Property(key = "database.password")
    private String password;

    private IntegrationTestDatabase() {
    }

    private IntegrationTestDatabase(final DatabaseVendor databaseVendor) {

        this.databaseVendor = databaseVendor;
    }

    public static IntegrationTestDatabase newPostgresContainer() {

        return new IntegrationTestDatabase(DatabaseVendor.POSTGRESQL);
    }

    public static IntegrationTestDatabase newMySqlContainer() {

        return new IntegrationTestDatabase(DatabaseVendor.MYSQL);
    }

    public IntegrationTestDatabase build() {

        this.databaseContainer = new DatabaseContainer(this);
        return this;
    }

    public IntegrationTestDatabase withInitScript(final String initScriptPath) {

        this.initScriptPath = initScriptPath;
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

    public DatabaseVendor getDatabaseVendor() {

        return this.databaseVendor;
    }

    public String getImageName() {

        return this.imageName;
    }

    public int getPort() {

        return this.port;
    }

    public String getBindAddress() {

        return this.bindAddress;
    }

    public String getInitScriptPath() {

        return this.initScriptPath;
    }

    public String getDatabaseName() {

        return this.databaseName;
    }

    public String getUsername() {

        return this.username;
    }

    public String getPassword() {

        return this.password;
    }

    public Result executeQuery(final String query) {

        return DatabaseController.getInstance().executeQuery(query);
    }
}
