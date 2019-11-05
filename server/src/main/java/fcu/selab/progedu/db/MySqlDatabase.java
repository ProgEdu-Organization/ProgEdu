
package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.MySqlDbConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;

public class MySqlDatabase implements IDatabase {

  private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
  private Connection con = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDatabase.class);

  /**
   * Connection to db
   */
  public Connection getConnection() {

    try {
      Class.forName(DB_DRIVER);
    } catch (ClassNotFoundException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    try {
      String connection = MySqlDbConfig.getInstance().getDbConnectionString();
      String user = MySqlDbConfig.getInstance().getDbUser();
      String password = MySqlDbConfig.getInstance().getDbPassword();
      con = DriverManager.getConnection(connection, user, password);

    } catch (SQLException | LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return con;
  }
}