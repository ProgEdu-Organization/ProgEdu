package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GitlabConfigTest {

    @Test
    public void getGitlabHostUrl() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GitlabHostUrl", gitlabConfig.getGitlabHostUrl());
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabRootUsername() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_USERNAME", gitlabConfig.getGitlabRootUsername());
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabAdminUsername() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "AdminUsername", gitlabConfig.getGitlabAdminUsername() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabRootPassword() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_PASSWORD", gitlabConfig.getGitlabRootPassword() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabAdminPassword() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "AdminPassword", gitlabConfig.getGitlabAdminPassword() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabApiToken() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_API_TOKEN", gitlabConfig.getGitlabApiToken() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGitlabRootUrl() {
        GitlabConfig gitlabConfig = GitlabConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "GITLAB_ROOT_URL", gitlabConfig.getGitlabRootUrl() );
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }
}