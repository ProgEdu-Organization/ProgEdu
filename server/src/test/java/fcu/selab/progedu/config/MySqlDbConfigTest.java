package fcu.selab.progedu.config;

import org.junit.Assert;
import org.junit.Test;

import fcu.selab.progedu.exception.LoadConfigFailureException;

public class MySqlDbConfigTest {

  public void testGetDbConnectionString() {
    MySqlDbConfig config = MySqlDbConfig.getInstance();
    try {
      String connString = config.getDbConnectionString();
      Assert.assertTrue(connString.startsWith("jdbc:mysql"));
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public void testGetDbUser() {
    MySqlDbConfig config = MySqlDbConfig.getInstance();
    try {
      String user = config.getDbUser();
      Assert.assertNotNull(user);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public void testGetDbPassword() {
    MySqlDbConfig config = MySqlDbConfig.getInstance();
    try {
      String password = config.getDbPassword();
      Assert.assertNotNull(password);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

}
