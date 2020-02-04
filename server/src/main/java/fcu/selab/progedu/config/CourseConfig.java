package fcu.selab.progedu.config;

import java.io.IOException;
import java.io.InputStream;

import java.security.Key;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import io.jsonwebtoken.security.Keys;

public class CourseConfig {
  private static final String PROPERTY_FILE = "/config/course_config.properties";
  private static CourseConfig instance;
  private static final String EXCEPTION =
      "Unable to get config of COURSE" + " connection string from file;";
  private static final Logger LOGGER = LoggerFactory.getLogger(CourseConfig.class);

  /**
   *
   * @return instance.
   */
  public static CourseConfig getInstance() {
    if (instance == null) {
      instance = new CourseConfig();
    }
    return instance;
  }

  private Properties props;

  private CourseConfig() {
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
   * Get Course Name
   *
   * @return courseName
   * @throws LoadConfigFailureException on properties call error
   */
  public String getCourseName() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("COURSE_NAME").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get School Email
   *
   * @return schoolEmail
   * @throws LoadConfigFailureException on properties call error
   */
  public String getSchoolEmail() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("COURSE_SCHOOL_EMAIL").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get course full name
   *
   * @return course fill name
   * @throws LoadConfigFailureException when property file is not found, the exception is thrown
   */
  public String getCourseFullName() throws LoadConfigFailureException {
    if (props != null) {
      return props.getProperty("COURSE_FULL_NAME").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get tomcat server ip
   *
   * @return tomcat server ip
   * @throws LoadConfigFailureException when property file is not found, the exception is thrown
   */
  public String getTomcatServerIp() throws LoadConfigFailureException {
    String webExternalUrl = System.getenv("WEB_EXTERNAL_URL");
    if (webExternalUrl != null && !webExternalUrl.equals("")) {
      return webExternalUrl;
    }
    if (props != null) {
      return props.getProperty("COURSE_TOMCAT_SERVER_IP").trim();
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get Course Key
   *
   * @return courseKey
   * @throws LoadConfigFailureException when property file is not found, the exception is thrown
   */
  public Key getCourseKey() throws LoadConfigFailureException {
    if (props != null) {
      String key = props.getProperty("COURSE_KEY");
      return Keys.hmacShaKeyFor(key.getBytes());
    }
    throw new LoadConfigFailureException(EXCEPTION + PROPERTY_FILE);
  }

  /**
   * Get application base url
   *
   * @return application base url
   */
  public String getBaseuri() {
    String baseuri = System.getenv("WEB_API_BASEURI");
    if (baseuri != null) {
      return baseuri;
    }
    return "";
  }
}
