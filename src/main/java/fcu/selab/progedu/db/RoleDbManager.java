package fcu.selab.progedu.db;

public class RoleDbManager {

  private static RoleDbManager dbManager = new RoleDbManager();

  public static RoleDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

}
