package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ScreenshotRecordDbManager {
  private static ScreenshotRecordDbManager dbManager = new ScreenshotRecordDbManager();
  private IDatabase database = new MySqlDatabase();

  public static ScreenshotRecordDbManager getInstance() {
    return dbManager;
  }

  /**
   * Add assignment to database
   * 
   * @param crId commitRecord id
   * @param url  screenshot url
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
   * Delete Screenshot_Record from database by crid
   * 
   */
  public void deleteScreenshotByCrid(int crid) {
    String sql = "DELETE FROM Screenshot_Record WHERE crId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, crid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
