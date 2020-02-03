import application.database.IntegrationTestDatabase;
import application.dataset.TestDataSet;
import application.property.PropertyProducer;
import domain.result.Result;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IntegrationTestPostgresDatabaseIT {

    private static IntegrationTestDatabase integrationTestDatabase;

    @BeforeAll
    public static void setUp() throws IllegalAccessException {

        /* Init singleton property producer */
        new PropertyProducer().init();

        integrationTestDatabase = IntegrationTestDatabase.newPostgresContainer();

        /* Inject mandatory properties */
        FieldUtils.writeField(integrationTestDatabase, "imageName",
                PropertyProducer.getPropertyValueAsString("database.image.name"), true);

        /* Build test container for this test */
        integrationTestDatabase.withInitScript("datasets/init.sql")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .build();
    }

    @Test
    public void executeSelectStatement() {

        Result result = integrationTestDatabase.executeQuery("SELECT 1");

        assertEquals(result.getRow(0).get(0), "1");
    }

    @TestDataSet(file = "datasets/test-insert.sql", cleanBefore = true)
    public void executeInsertStatement() {

        Result result = integrationTestDatabase.executeQuery("SELECT * FROM test_table");

        assertFalse(result.getRow(0).isEmpty());
    }
}
