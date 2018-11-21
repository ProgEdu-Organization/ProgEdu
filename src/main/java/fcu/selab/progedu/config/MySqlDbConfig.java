package fcu.selab.progedu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fcu.selab.progedu.exception.LoadConfigFailureException;

public class MySqlDbConfig {

  private static final String PROPERTY_FILE = "/config/db_config.properties";

  private static MySqlDbConfig instance = new MySqlDbConfig();

  public static MySqlDbConfig getInstance() {
    return instance;
  }

  private Properties props;

  private MySqlDbConfig() {

    InputStream is = this.getClass().getResourceAsStream(PROPERTY_FILE);
    try {
      props = new Properties();
      props.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get database connection string
   * 
   * @return db_connection
   * @throws LoadConfigFailureException on properties call error
   */
  public String getDbConnectionString() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("DB_CONNECTION");
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL connection string from file;" + PROPERTY_FILE);
  }

  /**
   * Get database user
   * 
   * @return user
   * @throws LoadConfigFailureException on properties call error
   */
  public String getDbUser() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("DB_USER");
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL user from file;" + PROPERTY_FILE);
  }

  /**
   * Get database password
   * 
   * @return password
   * @throws LoadConfigFailureException on properties call error
   */
  public String getDbPassword() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("DB_PASSWORD");
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL password from file;" + PROPERTY_FILE);
  }
}
