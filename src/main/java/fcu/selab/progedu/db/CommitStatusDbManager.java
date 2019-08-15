package fcu.selab.progedu.db;

public class CommitStatusDbManager {
  private static CommitStatusDbManager dbManager = new CommitStatusDbManager();

  public static CommitStatusDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

}
