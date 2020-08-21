package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.ReviewMetrics;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewMetricsDbManager {

  private static ReviewMetricsDbManager dbManager = new ReviewMetricsDbManager();

  public static ReviewMetricsDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewMetricsDbManager.class);

  /**
   * Insert review metrics into db
   *
   * @param category    category Id
   * @param mode        mode Id
   * @param metrics     metrics
   * @param description description
   * @param link        link
   */
  public void insertReviewMetrics(int category, int mode,
                                  String metrics, String description, String link) {
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
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Get all review metrics by category Id
   *
   * @param category category Id
   * @return review metrics details
   */
  public List<ReviewMetrics> getReviewMetrics(int category) {
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
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return reviewMetricsList;
  }

  /**
   * Update review metrics by Id
   *
   * @param id          Id
   * @param mode        mode Id
   * @param description description
   * @param link        link
   */
  public void editReviewMetricsById(int id, int mode, String description, String link) {
    String query = "UPDATE Review_Metrics SET mode = ?, description = ?, link = ? WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, mode);
      preStmt.setString(2, description);
      preStmt.setString(3, link);
      preStmt.setInt(4, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Delete review metrics by Id
   *
   * @param id Id
   */
  public void deleteReviewMetrics(int id) {
    String query = "DELETE FROM Review_Metrics WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

}
