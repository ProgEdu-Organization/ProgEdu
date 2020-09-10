package fcu.selab.progedu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;

public class MySqlDbConfig {

  private static final String PROPERTY_FILE = "/config/db_config.properties";

  private static MySqlDbConfig instance = new MySqlDbConfig();

  private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDbConfig.class);

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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Get database connection string
   *
   * @return db_connection
   * @throws LoadConfigFailureException on properties call error
   */
  public String getDbConnectionString() throws LoadConfigFailureException {
    return "jdbc:mysql://" + getDbHost() + "/" + getDbSchema() + getDbConnectionOption();
  }

  /**
   * Get database user
   *
   * @return user
   * @throws LoadConfigFailureException on properties call error
   */
  public String getDbUser() throws LoadConfigFailureException {
    String dbUser = System.getenv("DB_USER");
    if (dbUser != null && !dbUser.equals("")) {
      return dbUser;
    }
    if (props != null) {
      return props.getProperty("DB_USER").trim();
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
    String dbPassword = System.getenv("DB_PASSWORD");
    if (dbPassword != null && !dbPassword.equals("")) {
      return dbPassword;
    }
    if (props != null) {
      return props.getProperty("DB_PASSWORD").trim();
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL password from file;" + PROPERTY_FILE);
  }

  /**
   * Get database schema
   *
   * @return database schema
   */
  public String getDbSchema() throws LoadConfigFailureException {
    String dbSchema = System.getenv("DB_DATABASE");
    if (dbSchema != null && !dbSchema.equals("")) {
      return dbSchema;
    }
    if (props != null) {
      return props.getProperty("DB_DATABASE").trim();
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL database schema from file;" + PROPERTY_FILE);
  }

  /**
   * Get database host
   *
   * @return database host
   */
  public String getDbHost() throws LoadConfigFailureException {
    String dbHost = System.getenv("DB_HOST");
    if (dbHost != null && !dbHost.equals("")) {
      return dbHost;
    }
    if (props != null) {
      return props.getProperty("DB_HOST").trim();
    }
    throw new LoadConfigFailureException(
        "Unable to get config of MYSQL host from file;" + PROPERTY_FILE);
  }

  /**
   * Get database connection option
   *
   * @return database host
   */
  public String getDbConnectionOption() throws LoadConfigFailureException {
    String dbConnectionOption = System.getenv("DB_CONNECTION_OPTION");
    if (dbConnectionOption != null && !dbConnectionOption.equals("")) {
      return dbConnectionOption;
    }
    if (props != null) {
      return props.getProperty("DB_CONNECTION_OPTION").trim();
    }
    throw new LoadConfigFailureException(
            "Unable to get config of MYSQL connection option from file;" + PROPERTY_FILE);
  }
}
