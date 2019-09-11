package conn;

import java.sql.Connection;

import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;

public class TestGetCommitSituation {

  public static void main(String[] args) {
    IDatabase database = new MySqlDatabase();
    Connection connection = database.getConnection();
    CommitRecordDbManager db = CommitRecordDbManager.getInstance();
  }

}
