package fcu.selab.progedu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fcu.selab.progedu.exception.LoadConfigFailureException;

public class GitlabConfig {
  private static final String PROPERTY_FILE = "/config/gitlab_config.properties";

  private static GitlabConfig instance = new GitlabConfig();
  private static final String EXCEPTION = "Unable to get config of GITLAB"
      + " connection string from file;";

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
      e.printStackTrace();
    }
  }

  /**
   * Get gitlab host url
   * 
   * @return url
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabHostUrl() throws LoadConfigFailureException {
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
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_USERNAME").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab root password
   * 
   * @return password
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabRootPassword() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_PASSWORD").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab api token
   * 
   * @return token
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabApiToken() throws LoadConfigFailureException {
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
    if (props != null) {
      return props.getProperty("GITLAB_ROOT_URL").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get gitlab container id
   * 
   * @return container id
   * @throws LoadConfigFailureException on properties call error
   */
  public String getGitlabContainerId() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("GITLAB_CONTAINER").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }
}
