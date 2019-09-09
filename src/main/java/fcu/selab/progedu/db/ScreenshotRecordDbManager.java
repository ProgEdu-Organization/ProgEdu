package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
   * get assignment to database
   *
   * @param crId commitRecord id
   */
  public ArrayList<String> getScreenshotUrl(int crId) {
    String sql = "SELECT * FROM Screenshot_Record WHERE crid = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, crId);
      try (ResultSet rs = preStmt.executeQuery()) {
        ArrayList urls = new ArrayList<String>();
        while (rs.next()) {
          urls.add(rs.getString("pngUrl"));
        }
        return urls;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
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
