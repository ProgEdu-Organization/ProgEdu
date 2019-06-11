package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ScreenshotRecordDbManager {
  private static final String STATUS = "status";
  private static final String COMMIT = "commit";
  private static ScreenshotRecordDbManager dbManager = new ScreenshotRecordDbManager();
  private UserDbManager udb = UserDbManager.getInstance();
  private IDatabase database = new MySqlDatabase();

  public static ScreenshotRecordDbManager getInstance() {
    return dbManager;
  }

  /**
   * aggregate jenkins situation
   *
   * @param id
   *          student id
   * @param hw
   *          hw name
   * @param commit
   *          commit count
   * @param url
   *          screenshot url
   * @return check
   */
  public boolean insertJenkinsCommitCount(int id, String hw, int commit, List<String> url) {
    String sql = "INSERT INTO Screenshot_Record" + "(stuId, hw, commitNumber, pngUrl) "
        + "VALUES(?, ?, ?, ?)";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      for (String data : url) {
        preStmt.setInt(1, id);
        preStmt.setString(2, hw);
        preStmt.setInt(3, commit);
        preStmt.setString(4, data);
        preStmt.executeUpdate();
      }
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * check if result is in db
   *
   * @param id
   *          student id
   * @param hw
   *          he number
   * @return boolean
   */
  public boolean checkJenkinsJobTimestamp(int id, String hw) {
    String query = "SELECT * FROM Screenshot_Record WHERE stuId=? AND hw=?";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.setString(2, hw);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          check = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

}
