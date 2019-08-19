package fcu.selab.progedu.db;

public class RoleUserDbManager {
  private static RoleUserDbManager dbManager = new RoleUserDbManager();

  public static RoleUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

}