package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class MySqlDbConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDbConfigTest.class);

    @Test
    public void getDbConnectionString() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DbConnection", mySqlDbConfig.getDbConnectionString() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }

    }

    @Test
    public void getDbUser() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_USER", mySqlDbConfig.getDbUser() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getDbPassword() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_PASSWORD", mySqlDbConfig.getDbPassword() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getDbSchema() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DbSchema", mySqlDbConfig.getDbSchema() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getDbHost() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_HOST", mySqlDbConfig.getDbHost() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getDbConnectionOption() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_CONNECTION_OPTION", mySqlDbConfig.getDbConnectionOption() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }
}