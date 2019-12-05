package application.dataset;

import application.database.IntegrationTestDatabase;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
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
            IntegrationTestDatabase.cleanDatabase();
        }

        /* Parse and execute valid SQL statements */
        this.parseDataSetFile(testDataSet.file());
    }

    private void parseDataSetFile(final String testDataSetFile) {

        try (FileInputStream fileInputStream = new FileInputStream(testDataSetFile)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String strLine;
            while (null != (strLine = bufferedReader.readLine())) {

                Statement statement;
                try {
                    statement = CCJSqlParserUtil.parse(strLine);
                } catch (JSQLParserException e) {
                    LOG.warn("Invalid SQL syntax for statement '{}'. Skipping line.", strLine);
                    continue;
                }

                IntegrationTestDatabase.executeSqlStatement(statement);
            }

        } catch (IOException e) {
            LOG.warn("Dataset {} not found/broken. Not parsing file.", testDataSetFile);
        }
    }
}
