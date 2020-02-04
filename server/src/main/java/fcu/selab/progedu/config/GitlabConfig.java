package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitlabConfig {
  private static final String PROPERTY_FILE = "/config/gitlab_config.properties";

  private static GitlabConfig instance = new GitlabConfig();
  private static final String EXCEPTION =
      "Unable to get config of GITLAB" + " connection string from file;";
  private static final Logger LOGGER = LoggerFactory.getLogger(GitlabConfig.class);

  public static GitlabConfig getInstance() {
    return instance;
  }

  private Properties props;

  private GitlabConfig() {
    InputStream is = this.getClass().getResourceAsStream(PROPERTY_FILE);
    try {
      props = new Properties();
      props.load(is);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Get gitlab host url
   *
   * @return url
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabHostUrl() throws LoadConfigFailureException {
    String gitlabHost = System.getenv("GITLAB_HOST");
    if (gitlabHost != null && !gitlabHost.equals("")) {
      return gitlabHost;
    }
    if (props != null) {
      return props.getProperty("GITLAB_HOST_URL").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab root username
   *
   * @return username
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabRootUsername() throws LoadConfigFailureException {
    String gitlabAdminUsername = System.getenv("WEB_GITLAB_ADMIN_USERNAME");

    if (gitlabAdminUsername != null && !gitlabAdminUsername.equals("")) {
      return gitlabAdminUsername;
    }
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_USERNAME").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab admin username
   *
   * @return username
   */
  public String getGitlabAdminUsername() throws LoadConfigFailureException {
    return getGitlabRootUsername();
  }

  /**
   * Get gitlab root password
   *
   * @return password
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabRootPassword() throws LoadConfigFailureException {
    String gitlabRootPassword = System.getenv("GITLAB_ROOT_PASSWORD");
    if (gitlabRootPassword != null && !gitlabRootPassword.equals("")) {
      return gitlabRootPassword;
    }
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_PASSWORD").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab admin password
   *
   * @return password
   */
  public String getGitlabAdminPassword() throws LoadConfigFailureException {
    return getGitlabRootPassword();
  }

  /**
   * Get gitlab api token
   *
   * @return token
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabApiToken() throws LoadConfigFailureException {
    String gitlabApiToken = System.getenv("WEB_GITLAB_ADMIN_PERSONAL_TOKEN");
    if (gitlabApiToken != null && !gitlabApiToken.equals("")) {
      return gitlabApiToken;
    }
    if (props != null) {
      return props.getProperty("GITLAB_API_TOKEN").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab root url
   *
   * @return url
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabRootUrl() throws LoadConfigFailureException {
    String gitlabHost = System.getenv("GITLAB_HOST");
    if (gitlabHost != null && !gitlabHost.equals("")) {
      return gitlabHost.substring(0, gitlabHost.indexOf("//") + 2)
          + getGitlabAdminUsername()
          + ":"
          + getGitlabRootPassword()
          + "@"
          + gitlabHost.substring(gitlabHost.indexOf("//") + 2, gitlabHost.length());
    }
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_URL").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }
}
