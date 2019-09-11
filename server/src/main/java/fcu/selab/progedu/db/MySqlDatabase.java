
package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fcu.selab.progedu.config.MySqlDbConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class MySqlDatabase implements IDatabase {

  private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
  private Connection con = null;

  /**
   * Connection to db
   */
  public Connection getConnection() {

    try {
      Class.forName(DB_DRIVER);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      String connection = MySqlDbConfig.getInstance().getDbConnectionString();
      String user = MySqlDbConfig.getInstance().getDbUser();
      String password = MySqlDbConfig.getInstance().getDbPassword();
      con = DriverManager.getConnection(connection, user, password);

    } catch (SQLException | LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return con;
  }
}