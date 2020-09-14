package fcu.selab.progedu.config;

import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class JenkinsConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsConfigTest.class);

    @Test
    public void getJenkinsHostUrl() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JenkinsHostUrl", jenkinsConfig.getJenkinsHostUrl());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getSeleniumHostUrl() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "SELENIUM_HOST_URL", jenkinsConfig.getSeleniumHostUrl());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsRootUsername() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JENKINS_ROOT_USERNAME", jenkinsConfig.getJenkinsRootUsername());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsAdminUsername() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JenkinsAdminUsername", jenkinsConfig.getJenkinsAdminUsername());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsRootPassword() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JENKINS_ROOT_PASSWORD", jenkinsConfig.getJenkinsRootPassword());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsAdminPassword() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JenkinsAdminPassword", jenkinsConfig.getJenkinsAdminPassword());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsApiToken() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JENKINS_API_TOKEN", jenkinsConfig.getJenkinsApiToken());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getJenkinsRootUrl() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "JENKINS_ROOT_URL", jenkinsConfig.getJenkinsRootUrl());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getMailUser() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "MAIL_USERNAME", jenkinsConfig.getMailUser(), false);
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getMailPassword() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "MAIL_PASSWORD", jenkinsConfig.getMailPassword(), false);
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }
}