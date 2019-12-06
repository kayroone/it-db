package application.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@ApplicationScoped
public class PropertyProducer {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyProducer.class);
    private static final String PROPERTY_FILE_PATH = "src/test/resources/it-db-config.properties";

    private static Properties properties;

    @PostConstruct
    public void init() {

        if (PropertyProducer.properties == null) {
            this.loadProperties();
        }
    }

    @Produces
    public String getPropertyValue(final InjectionPoint ip) {

        final String key = this.getConfigKeyFromAnnotatedField(ip.getAnnotated());

        return PropertyProducer.properties.getProperty(key);
    }

    @Produces
    public Integer getPropertyValueForInt(final InjectionPoint ip) {

        final String key = this.getConfigKeyFromAnnotatedField(ip.getAnnotated());
        final String value = PropertyProducer.properties.getProperty(key);

        try {
            return Integer.valueOf(value);
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    public static String getPropertyValue(final String key) {

        return PropertyProducer.properties.getProperty(key);
    }

    private String getConfigKeyFromAnnotatedField(final Annotated annotated) {

        return annotated.getAnnotation(Property.class).key();
    }

    private void loadProperties() {

        properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(PROPERTY_FILE_PATH)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            properties.load(bufferedReader);
        } catch (IOException e) {
            LOG.warn("Property file {} not found/broken. Not parsing file.", PROPERTY_FILE_PATH);
        }
    }
}
