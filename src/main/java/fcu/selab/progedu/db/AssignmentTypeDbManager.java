package fcu.selab.progedu.db;

public class AssignmentTypeDbManager {

  private static AssignmentTypeDbManager dbManager = new AssignmentTypeDbManager();

  public static AssignmentTypeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentTypeDbManager() {

  }

}
