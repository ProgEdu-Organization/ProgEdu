package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class CommitStatusDbManager {
  private static CommitStatusDbManager dbManager = new CommitStatusDbManager();

  public static CommitStatusDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(CommitStatusDbManager.class);

  /**
   * Get CommitStatus status Id by status name
   * 
   * @param statusName status name
   * @return statusId status id
   */
  public int getStatusIdByName(String statusName) {
    String query = "SELECT id FROM Commit_Status WHERE status = ?";
    int statusId = 0;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, statusName);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          statusId = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return statusId;
  }

  /**
   * Get CommitStatus status name by status Id
   * 
   * @param statusId type name
   * @return statusEnum statusEnum
   */
  public StatusEnum getStatusNameById(int statusId) {
    String query = "SELECT status FROM Commit_Status WHERE id = ?";
    String statusName = null;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, statusId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          statusName = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    StatusEnum statusEnum = StatusEnum.getStatusEnum(statusName);
    return statusEnum;
  }


//  public ArrayList<Integer> getAllStatusCountByAssignmentName(String assignmentName) {
//    String query = "SELECT status FROM Commit_Status WHERE id = ?";
//    String statusName = null;
//  }

}
