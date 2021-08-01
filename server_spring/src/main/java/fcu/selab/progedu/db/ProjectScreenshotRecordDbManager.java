package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class ProjectScreenshotRecordDbManager {
  private static ProjectScreenshotRecordDbManager instance = new ProjectScreenshotRecordDbManager();

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER;

  static {
    LOGGER = LoggerFactory.getLogger(ProjectScreenshotRecordDbManager.class);
  }

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

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pcrId);
      preStmt.setString(2, url);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Delete Project_Screenshot_Record by crid.
   * 
   * @param pcrid Project_Commit_Record id
   */
  public void deleteProjectScreenshot(int pcrid) {
    String sql = "DELETE FROM Project_Screenshot_Record WHERE pcrid = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pcrid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * get screenshot url
   * 
   * @param pcrid commitRecord id
   */
  public List<String> getScreenshotUrl(int pcrid) {
    String sql = "SELECT pngUrl FROM Project_Screenshot_Record WHERE pcrid = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pcrid);
      rs = preStmt.executeQuery();

      List<String> urls = new ArrayList<>();
      while (rs.next()) {
        urls.add(rs.getString("pngUrl"));
      }
      return urls;

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return null;
  }

}
