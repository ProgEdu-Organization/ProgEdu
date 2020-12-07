package fcu.selab.progedu.config;

import java.io.IOException;
import java.io.InputStream;


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;

public class MongoDbConfig {
  private static final String PROPERTY_FILE = "/config/mongo_config.properties";
  private static MongoDbConfig instance;
  private static final String EXCEPTION =
      "Unable to get config of MONGO" + " connection string from file;";
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbConfig.class);

  /**
   *
   * @return instance.
   */
  public static MongoDbConfig getInstance() {
    if (instance == null) {
      instance = new MongoDbConfig();
    }
    return instance;
  }

  private Properties props;

  private MongoDbConfig() {
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
     * Get database user
     *
     * @return user
     * @throws LoadConfigFailureException on properties call error
     */
  public String getDbUser() throws LoadConfigFailureException {
    String dbUser = System.getenv("MONGO_USER");
    if (dbUser != null && !dbUser.equals("")) {
      return dbUser;
    }
    if (props != null) {
      return props.getProperty("MONGO_USER").trim();
    }
    throw new LoadConfigFailureException(
              "Unable to get config of MONGO user from file;" + PROPERTY_FILE);
  }

    /**
     * Get database password
     *
     * @return password
     * @throws LoadConfigFailureException on properties call error
     */
  public String getDbPassword() throws LoadConfigFailureException {
    String dbPassword = System.getenv("MONGO_PASSWORD");
    if (dbPassword != null && !dbPassword.equals("")) {
      return dbPassword;
    }
    if (props != null) {
      return props.getProperty("MONGO_PASSWORD").trim();
    }
    throw new LoadConfigFailureException(
              "Unable to get config of MONGO password from file;" + PROPERTY_FILE);
  }

    /**
     * Get database schema
     *
     * @return database schema
     */
  public String getDbSchema() throws LoadConfigFailureException {
    String dbSchema = System.getenv("MONGO_DATABASE");
    if (dbSchema != null && !dbSchema.equals("")) {
      return dbSchema;
    }
    if (props != null) {
      return props.getProperty("MONGO_DATABASE").trim();
    }
    throw new LoadConfigFailureException(
              "Unable to get config of MONGO database schema from file;" + PROPERTY_FILE);
  }

    /**
     * Get database connection string
     *
     * @return db_connection
     * @throws LoadConfigFailureException on properties call error
     */
  public String getDbConnectionString() throws LoadConfigFailureException {
    return "mongodb://" + getDbUser() + ":" + getDbPassword() + "@"  +  getMongoHostUrl() + "/"
              + getDbSchema() + "?authSource=" + getDbSchema() + getDbConnectionOption();
  }

    /**
     * Get mongo host url
     *
     * @return url
     * @throws LoadConfigFailureException on properties call error
     */
  public String getMongoHostUrl() throws LoadConfigFailureException {
    String mongoHostUrl = System.getenv("MONGO_HOST_URL");
    if (mongoHostUrl != null && !mongoHostUrl.equals("")) {
      return mongoHostUrl;
    }
    if (props != null) {
      return props.getProperty("MONGO_HOST_URL").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

    /**
     * Get database connection option
     *
     * @return mongo database connection option
     */
  public String getDbConnectionOption() throws LoadConfigFailureException {
    String dbConnectionOption = System.getenv("MONGO_DB_CONNECTION_OPTION");
    if (dbConnectionOption != null && !dbConnectionOption.equals("")) {
      return dbConnectionOption;
    }
    if (props != null) {
      return props.getProperty("MONGO_DB_CONNECTION_OPTION").trim();
    }
    throw new LoadConfigFailureException(
              "Unable to get config of MONGO connection option from file;" + PROPERTY_FILE);
  }

}
