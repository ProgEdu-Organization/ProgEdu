package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
   * @param urls
   *          screenshot urls
   * @throws SQLException
   *           SQLException
   */
  public void insertJenkinsCommitCount(int id, String hw, int commit, List<String> urls)
      throws SQLException {
    String sql = "INSERT INTO Screenshot_Record" + "(stuId, hw, commitNumber, pngUrl) "
        + "VALUES(?, ?, ?, ?)";
    Connection conn = database.getConnection();
    PreparedStatement preStmt = conn.prepareStatement(sql);
    for (String url : urls) {
      preStmt.setInt(1, id);
      preStmt.setString(2, hw);
      preStmt.setInt(3, commit);
      preStmt.setString(4, url);
      preStmt.executeUpdate();
    }
  }
  
  /**
   * get Screenshots(
   *
   * @param stuId
   *          student id
   * @param hw
   *          hw name
   *          
   * @throws SQLException
   *           SQLException
   */
  public List<String> getScreenshots(int stuId, String hw)
      throws SQLException {
    String sql = "SELECT  pngUrl FROM Screenshot_Record WHERE stuId='" + stuId + " ' AND hw='" 
        + hw + "';";
    Connection conn = database.getConnection();
    PreparedStatement preStmt = conn.prepareStatement(sql);
    List<String> pngUrls = new ArrayList<>();
    try (ResultSet rs = preStmt.executeQuery()) {
      while (rs.next()) {
        pngUrls.add(rs.getString("pngUrl"));
      }
    }
    return pngUrls;
  }
}
