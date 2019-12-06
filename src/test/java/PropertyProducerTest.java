import application.property.PropertyProducer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PropertyProducerTest {

    private static final String testPropertyKey = "psql.image.name";
    private static final String testPropertyValue = "postgres/latest";

    @BeforeAll
    static void init() {

        new PropertyProducer().init();
    }

    @Test
    public void executeTestInsertStatement() {

        String propertyValue = PropertyProducer.getPropertyValue(testPropertyKey);

        assertNotNull(propertyValue);
        assertEquals(propertyValue, testPropertyValue);
    }
}
