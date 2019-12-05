package application.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseController {

    private static DatabaseController INSTANCE;
    private HikariDataSource dataSource;

    private DatabaseController(final JdbcDatabaseContainer container) {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());

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

        this.dataSource.close();
        INSTANCE = null;
    }
}
