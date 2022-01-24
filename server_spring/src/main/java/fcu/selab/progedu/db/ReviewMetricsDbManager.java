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
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, category);
      preStmt.setInt(2, mode);
      preStmt.setString(3, metrics);
      preStmt.setString(4, description);
      preStmt.setString(5, link);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, category);
      rs = preStmt.executeQuery();

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
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try  {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        reviewMetrics.setId(id);
        reviewMetrics.setCategory(rs.getInt("category"));
        reviewMetrics.setMode(rs.getInt("mode"));
        reviewMetrics.setMetrics(rs.getString("metrics"));
        reviewMetrics.setDescription(rs.getString("description"));
        reviewMetrics.setLink(rs.getString("link"));
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);
      rs = preStmt.executeQuery();

      while (rs.next()) {
        metrics = rs.getString("metrics");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);
      rs = preStmt.executeQuery();

      while (rs.next()) {
        mode = rs.getInt("mode");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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
  public void editReviewMetricsById(int id, String metrics, int mode, String description, String link)
      throws SQLException {
    String query = "UPDATE Review_Metrics SET metrics = ?, mode = ?, description = ?, link = ? WHERE id = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, metrics);
      preStmt.setInt(2, mode);
      preStmt.setString(3, description);
      preStmt.setString(4, link);
      preStmt.setInt(5, id);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Delete review metrics by Id
   *
   * @param id Id
   */
  public void deleteReviewMetrics(int id) throws SQLException {
    String query = "DELETE FROM Review_Metrics WHERE id = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
