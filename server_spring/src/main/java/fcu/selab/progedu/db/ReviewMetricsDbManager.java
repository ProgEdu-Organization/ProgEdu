package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.ReviewMetrics;

public class ReviewMetricsDbManager {

  private static ReviewMetricsDbManager dbManager = new ReviewMetricsDbManager();

  public static ReviewMetricsDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Insert review metrics into db
   *
   * @param category    category Id
   * @param mode        mode Id
   * @param metrics     metrics
   * @param description description
   * @param link        link
   */
  public void insertReviewMetrics(int category, int mode, String metrics,
                                  String description, String link) throws SQLException {
    String query = "INSERT INTO Review_Metrics(category, mode, metrics, description, link) "
        + "VALUES(?,?,?,?,?);";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, category);
      preStmt.setInt(2, mode);
      preStmt.setString(3, metrics);
      preStmt.setString(4, description);
      preStmt.setString(5, link);
      preStmt.executeUpdate();
    }
  }

  /**
   * Get all review metrics by category Id
   *
   * @param category category Id
   * @return review metrics details
   */
  public List<ReviewMetrics> getReviewMetricsList(int category) throws SQLException {
    String query = "SELECT * FROM Review_Metrics WHERE category = ?";
    List<ReviewMetrics> reviewMetricsList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, category);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int mode = rs.getInt("mode");
          String metrics = rs.getString("metrics");
          String description = rs.getString("description");
          String link = rs.getString("link");
          ReviewMetrics reviewMetrics = new ReviewMetrics();
          reviewMetrics.setId(id);
          reviewMetrics.setCategory(category);
          reviewMetrics.setMode(mode);
          reviewMetrics.setMetrics(metrics);
          reviewMetrics.setDescription(description);
          reviewMetrics.setLink(link);
          reviewMetricsList.add(reviewMetrics);
        }
      }
    }
    return reviewMetricsList;
  }

  /**
   * Get review metrics from review metrics by id
   *
   * @param id id
   */
  public ReviewMetrics getReviewMetrics(int id) throws SQLException {
    String query = "SELECT * FROM Review_Metrics WHERE id = ?";
    ReviewMetrics reviewMetrics = new ReviewMetrics();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          reviewMetrics.setId(id);
          reviewMetrics.setCategory(rs.getInt("category"));
          reviewMetrics.setMode(rs.getInt("mode"));
          reviewMetrics.setMetrics(rs.getString("metrics"));
          reviewMetrics.setDescription(rs.getString("description"));
          reviewMetrics.setLink(rs.getString("link"));
        }
      }
    }
    return reviewMetrics;
  }

  /**
   * Get metrics from review metrics by Id
   *
   * @param id id
   */
  public String getReviewMetricsById(int id) throws SQLException {
    String query = "SELECT metrics FROM Review_Metrics WHERE id = ?";
    String metrics = null;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          metrics = rs.getString("metrics");
        }
      }
    }
    return metrics;
  }

  /**
   * Get metrics from review metrics by Id
   *
   * @param id id
   */
  public int getScoreModeIdById(int id) throws SQLException {
    String query = "SELECT mode FROM Review_Metrics WHERE id = ?";
    int mode = 0;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          mode = rs.getInt("mode");
        }
      }
    }
    return mode;
  }

  /**
   * Update review metrics by Id
   *
   * @param id          Id
   * @param mode        mode Id
   * @param description description
   * @param link        link
   */
  public void editReviewMetricsById(int id, int mode, String description, String link)
      throws SQLException {
    String query = "UPDATE Review_Metrics SET mode = ?, description = ?, link = ? WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, mode);
      preStmt.setString(2, description);
      preStmt.setString(3, link);
      preStmt.setInt(4, id);
      preStmt.executeUpdate();
    }
  }

  /**
   * Delete review metrics by Id
   *
   * @param id Id
   */
  public void deleteReviewMetrics(int id) throws SQLException {
    String query = "DELETE FROM Review_Metrics WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    }
  }

}
