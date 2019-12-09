package application.dataset;

import application.database.DatabaseController;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

class TestDataSetProcessor implements BeforeEachCallback {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataSetProcessor.class);

    @Override
    public void beforeEach(ExtensionContext context) {

        Optional<Method> testMethod = context.getTestMethod();

        if (!testMethod.isPresent()) {
            return;
        }

        TestDataSet testDataSet = testMethod.get().getAnnotation(TestDataSet.class);

        /* Clean before executing new statements */
        if (testDataSet.cleanBefore()) {
            DatabaseController.getInstance().cleanDatabase();
        }

        /* Parse and execute only valid SQL statements */
        this.parseDataSetFile(testDataSet.file());
    }

    private void parseDataSetFile(final String testDataSetFile) {

        try (InputStream fileInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(testDataSetFile)) {

            if (null == fileInputStream) {
                LOG.warn("Dataset {} not found. Not parsing file.", testDataSetFile);
                return;
            }

            Connection connection = DatabaseController.getInstance().getConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String strLine;
            while (null != (strLine = bufferedReader.readLine())) {

                try {
                    String statement = CCJSqlParserUtil.parse(strLine).toString();
                    DatabaseController.getInstance().executeQuery(statement, connection);
                } catch (JSQLParserException e) {
                    LOG.warn("Invalid SQL syntax for statement '{}'. Skipping line.", strLine);
                }
            }

            connection.close();

        } catch (IOException e) {
            LOG.error("Dataset {} broken. Not parsing file.", testDataSetFile);
        } catch (SQLException e) {
            LOG.error("Failed to establish database connection. Aborting.");
        }
    }
}
