package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotRecordDbManager {
  private static ScreenshotRecordDbManager dbManager = new ScreenshotRecordDbManager();
  private IDatabase database = new MySqlDatabase();

  public static ScreenshotRecordDbManager getInstance() {
    return dbManager;
  }

  /**
   * Add assignment to database
   * 
   * @param crId
   *          commitRecord id
   * @param url
   *          screenshot url
   */
  public void addScreenshotRecord(int crId, String url) {
    String sql = "INSERT INTO Screenshot_Record(crId, pngUrl)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, crId);
      preStmt.setString(2, url);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
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
  public List<String> getScreenshots(int stuId, String hw) throws SQLException {
    String sql = "SELECT  pngUrl FROM Screenshot_Record WHERE stuId='" + stuId + " ' AND hw='" + hw
        + "';";
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

  /**
   * delete ScreenshotRecord from database
   * 
   * @param crid
   *          commitRecord Id
   */
  public void deleteScreenshotRecord(int crid) {
    String sql = "DELETE FROM Screenshot_Record WHERE crid ='" + crid + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
