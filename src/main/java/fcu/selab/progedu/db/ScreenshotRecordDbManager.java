package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.data.ScreenshotRecord;

public class ScreenshotRecordDbManager {
  private static ScreenshotRecordDbManager dbManager = new ScreenshotRecordDbManager();
  private IDatabase database = new MySqlDatabase();

  public static ScreenshotRecordDbManager getInstance() {
    return dbManager;
  }

  /**
   * Add assignment to database
   * 
   * @param screenshotRecord ScreenshotRecord
   */
  public void addScreenshotRecord(ScreenshotRecord screenshotRecord) {
    String sql = "INSERT INTO Screenshot_Record(crId, pmgUrl)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, screenshotRecord.getCrId());
      preStmt.setString(2, screenshotRecord.getPngUrl());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete ScreenshotRecord from database
   * 
   * @param crid commitRecord Id
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

  /**
   * get pmgUrl by crId
   * 
   * @param crid commitRecord Id
   */
  public String getPmgUrl(int crid) {
    String pmgUrl = null;
    String sql = "SELECT * FROM Screenshot_Record WHERE crid=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, crid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          pmgUrl = rs.getString("pmgUrl");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return pmgUrl;
  }

}
