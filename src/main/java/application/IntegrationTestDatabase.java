package application;

import application.container.ContainerNotRunningException;
import application.database.DatabaseController;
import application.property.Property;
import domain.database.DatabaseVendor;
import domain.result.Result;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class IntegrationTestDatabase {

    private final static String DEFAULT_DATABASE_NAME = "test";
    private final static String DEFAULT_USERNAME = "test";
    private final static String DEFAULT_PASSWORD = "test";

    private DatabaseController databaseController;

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

    public IntegrationTestDatabase() {
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

    public IntegrationTestDatabase build() throws ContainerNotRunningException {

        this.databaseController = DatabaseController.createInstance(this);
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

    public DatabaseVendor getDatabaseVendor() {

        return this.databaseVendor;
    }

    public String getImageName() {

        return StringUtils.defaultIfEmpty(this.imageName, this.databaseVendor.getImageName());
    }

    public int getPort() {

        return this.port;
    }

    public String getBindAddress() {

        return this.bindAddress;
    }

    public String getInitScriptPath() {

        return StringUtils.defaultIfEmpty(this.initScriptPath, "");
    }

    public String getDatabaseName() {

        return StringUtils.defaultIfEmpty(this.databaseName, DEFAULT_DATABASE_NAME);
    }

    public String getUsername() {

        return StringUtils.defaultIfEmpty(this.username, DEFAULT_USERNAME);
    }

    public String getPassword() {

        return StringUtils.defaultIfEmpty(this.password, DEFAULT_PASSWORD);
    }

    public Result executeStatement(final String statement) {

        return this.databaseController.executeStatement(statement);
    }
}
