package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.service.ReviewStatusEnum;

public class ReviewStatusDbManager {

  private static ReviewStatusDbManager dbManager = new ReviewStatusDbManager();

  public static ReviewStatusDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Get Id by status
   *
   * @param status review status
   * @return id review Id
   */
  public int getReviewStatusIdByStatus(String status) throws SQLException {
    String query = "SELECT id FROM Review_Status WHERE status = ?";
    int id = 0;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setString(1, status);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        id = rs.getInt("id");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return id;
  }

  /**
   * Get status by id
   *
   * @param id review status Id
   * @return status review status
   */
  public ReviewStatusEnum getReviewStatusById(int id) throws SQLException {
    String query = "SELECT status FROM Review_Status WHERE id = ?";
    String reviewStatus = null;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        reviewStatus = rs.getString("status");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getReviewStatusEnum(reviewStatus);
    return reviewStatusEnum;
  }
}
