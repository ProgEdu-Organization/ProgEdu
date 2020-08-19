package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.service.ReviewStatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewStatusDbManager {

  private static ReviewStatusDbManager dbManager = new ReviewStatusDbManager();

  public static ReviewStatusDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewStatusDbManager.class);

  /**
   * Get Id by status
   *
   * @param status review status
   * @return id review Id
   */
  public int getReviewStatusIdByStatus(String status) {
    String query = "SELECT id FROM Review_Status WHERE status = ?";
    int id = 0;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, status);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return id;
  }

  /**
   * Get status by id
   *
   * @param id review status Id
   * @return status review status
   */
  public ReviewStatusEnum getReviewStatusById(int id) {
    String query = "SELECT status FROM Review_Status WHERE id = ?";
    String reviewStatus = null;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          reviewStatus = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getScoreModeEnum(reviewStatus);
    return reviewStatusEnum;
  }
}
