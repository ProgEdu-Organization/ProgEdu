package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    String sql = "INSERT INTO Project_Screenshot_Record(pcrid, pngUrl)  VALUES(?, ?)";

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
   * @param pcrid Project_Commit_Record id
   */
  public void deleteProjectScreenshot(int pcrid) {
    String sql = "DELETE FROM Project_Screenshot_Record WHERE pcrid = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pcrid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get screenshot url
   * 
   * @param pcrid commitRecord id
   */
  public List<String> getScreenshotUrl(int pcrid) {
    String sql = "SELECT pngUrl FROM Project_Screenshot_Record WHERE pcrid = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pcrid);
      try (ResultSet rs = preStmt.executeQuery()) {
        List<String> urls = new ArrayList<>();
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

}
