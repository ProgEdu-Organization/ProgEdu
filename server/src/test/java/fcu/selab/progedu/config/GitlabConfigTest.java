package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class GitlabConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabConfigTest.class);

    @Test
    public void getGitlabHostUrl() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GitlabHostUrl", gitlabConfig.getGitlabHostUrl());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabRootUsername() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_USERNAME", gitlabConfig.getGitlabRootUsername());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabAdminUsername() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "AdminUsername", gitlabConfig.getGitlabAdminUsername() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabRootPassword() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_PASSWORD", gitlabConfig.getGitlabRootPassword() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabAdminPassword() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "AdminPassword", gitlabConfig.getGitlabAdminPassword() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabApiToken() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_API_TOKEN", gitlabConfig.getGitlabApiToken() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getGitlabRootUrl() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_URL", gitlabConfig.getGitlabRootUrl() );
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }
}