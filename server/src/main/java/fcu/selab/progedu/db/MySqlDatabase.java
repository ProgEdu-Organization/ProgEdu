
package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.SQLException;

import fcu.selab.progedu.config.MySqlDbConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class MySqlDatabase implements IDatabase {

  private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

  private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDatabase.class);

  private static MySqlDatabase instance = new MySqlDatabase();
  private static BoneCP connectionPool = null;

  private MySqlDatabase() {
    try {
      Class.forName(DB_DRIVER); // load the DB driver
      BoneCPConfig config = getConfig();
      if (connectionPool == null) {
        connectionPool = new BoneCP(config);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static MySqlDatabase getInstance() {
    return instance;
  }

  /**
   * Connection to db
   */
  public Connection getConnection() {


    Connection connection = null;

    if (connectionPool == null) {
      BoneCPConfig config = getConfig();
      try {
        connectionPool = new BoneCP(config);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
        throw new NullPointerException();
      }
    }


    try {
      connection = connectionPool.getConnection();
    } catch (SQLException throwables) {
      connectionPool = null;
      throwables.printStackTrace();
      throw new NullPointerException();
    }

    return connection;
  }

  private BoneCPConfig getConfig() {
    BoneCPConfig config = new BoneCPConfig();

    try {
      String connection = MySqlDbConfig.getInstance().getDbConnectionString();
      String user = MySqlDbConfig.getInstance().getDbUser();
      String password = MySqlDbConfig.getInstance().getDbPassword();

      config.setJdbcUrl(connection);
      config.setUsername(user);
      config.setPassword(password);

      config.setPartitionCount(2);
      config.setMinConnectionsPerPartition(1);
      config.setMaxConnectionsPerPartition(4);

    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return config;
  }

}