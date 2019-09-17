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
      System.out.println("DB_USER: " + dbUser);
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
      System.out.println("DB_PASSWORD: " + dbPassword);
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
      System.out.println("DB_DATABASE: " + dbSchema);
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
      System.out.println("DB_HOST: " + dbHost);
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
  public String getDbConnectionOption() {
    String dbConnectionOption = System.getenv("DB_CONNECTION_OPTION");
    if (dbConnectionOption != null && !dbConnectionOption.equals("")) {
      System.out.println("DB_CONNECTION_OPTION: " + dbConnectionOption);
      return dbConnectionOption;
    }
    if (props != null) {
      return props.getProperty("DB_CONNECTION_OPTION").trim();
    }
    return "?relaxAutoCommit=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
  }
}
