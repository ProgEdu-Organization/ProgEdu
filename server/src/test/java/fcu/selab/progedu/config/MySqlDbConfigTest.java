package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MySqlDbConfigTest {

    @Test
    public void getDbConnectionString() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DbConnection", mySqlDbConfig.getDbConnectionString() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getDbUser() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_USER", mySqlDbConfig.getDbUser() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDbPassword() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_PASSWORD", mySqlDbConfig.getDbPassword() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDbSchema() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DbSchema", mySqlDbConfig.getDbSchema() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDbHost() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_HOST", mySqlDbConfig.getDbHost() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDbConnectionOption() {
        MySqlDbConfig mySqlDbConfig = MySqlDbConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "DB_CONNECTION_OPTION", mySqlDbConfig.getDbConnectionOption() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }
}