package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ReviewSettingMetricsDbManager {

  private static ReviewSettingMetricsDbManager dbManager = new ReviewSettingMetricsDbManager();

  public static ReviewSettingMetricsDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Insert review setting metrics into db
   *
   * @param rsId review setting Id
   * @param rmId review metrics Id
   */
  public void insertReviewSettingMetrics(int rsId, int rmId) throws SQLException {
    String query = "INSERT INTO Review_Setting_Metrics(rsId, rmId) VALUES(?,?)";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, rsId);
      preStmt.setInt(2, rmId);
      preStmt.executeUpdate();
    }
  }

  /**
   * Get review metrics from review setting metrics by specific assignment id
   *
   * @param rsId review setting Id
   */
  public List<Integer> getReviewSettingMetricsByAssignmentId(int rsId) throws SQLException {
    String query = "SELECT rmId FROM Review_Setting_Metrics WHERE rsId = ?";
    List<Integer> metricsList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, rsId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int rmId = rs.getInt("rmId");
          metricsList.add(rmId);
        }
      }
    }
    return metricsList;
  }

  /**
   * Get metrics id from review setting metrics by rsmId form review record
   *
   * @param rsmId review setting metrics id
   */
  public int getReviewMetricsIdByRsmId(int rsmId) throws SQLException {
    String query = "SELECT rmId FROM Review_Setting_Metrics WHERE id = ?";
    int rmId = 0;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, rsmId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          rmId = rs.getInt("rmId");
        }
      }
    }
    return rmId;
  }

  /**
   * Get metrics id from review setting metrics by rsmId form review record
   *
   * @param rsId review setting id
   * @param rmId review metrics id
   */
  public int getReviewSettingMetricsIdByRsIdRsmId(int rsId, int rmId) throws SQLException {
    String query = "SELECT id FROM Review_Setting_Metrics WHERE rsId = ? AND rmId = ?";
    int id = 0;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, rsId);
      preStmt.setInt(2, rmId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    }
    return id;
  }

  /**
   * Delete review metrics from review setting metrics by specific assignment id
   *
   * @param rsId review setting Id
   */
  public void deleteReviewSettingMetricsByAssignmentId(int rsId) throws SQLException {
    String query = "DELETE FROM Review_Setting_Metrics WHERE rsId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, rsId);
      preStmt.executeUpdate();
    }
  }

}
