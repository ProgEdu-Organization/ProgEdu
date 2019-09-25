package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectScreenshotRecordDbManager {
  private static ProjectScreenshotRecordDbManager instance = new ProjectScreenshotRecordDbManager();

  private IDatabase database = new MySqlDatabase();

  public static ProjectScreenshotRecordDbManager getInstance() {
    return instance;
  }

  /**
   * Add project to database
   * 
   * @param pcrId ProjectCommitRecord id
   * @param url   ProjectScreenshot url
   */
  public void addProjectScreenshotRecord(int pcrId, String url) {
    String sql = "INSERT INTO Project_Screenshot_Record(crId, pngUrl)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pcrId);
      preStmt.setString(2, url);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Delete Project_Screenshot_Record by crid.
   * 
   * @param crid Project_Commit_Record id
   */
  public void deleteProjectScreenshot(int crid) {
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
