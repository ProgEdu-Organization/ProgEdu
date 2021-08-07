package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class ScreenshotRecordDbManager {
  private static ScreenshotRecordDbManager dbManager = new ScreenshotRecordDbManager();
  private IDatabase database = MySqlDatabase.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotRecordDbManager.class);

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
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, crId);
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
   * get assignment to database
   *
   * @param crId commitRecord id
   */
  public ArrayList<String> getScreenshotUrl(int crId) {
    String sql = "SELECT * FROM Screenshot_Record WHERE crid = ?";
    ArrayList urls = new ArrayList<String>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, crId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        urls.add(rs.getString("pngUrl"));
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return urls;
  }


  /**
   * Delete Screenshot_Record from database by crid
   * 
   */
  public void deleteScreenshotByCrid(int crid) {
    String sql = "DELETE FROM Screenshot_Record WHERE crId = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, crid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
