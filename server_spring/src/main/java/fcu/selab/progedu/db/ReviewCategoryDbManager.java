package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.ReviewCategory;

public class ReviewCategoryDbManager {

  private static ReviewCategoryDbManager dbManager = new ReviewCategoryDbManager();

  public static ReviewCategoryDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * insert new review category into db
   *
   * @param name    name
   * @param metrics metrics
   */
  public void insertReviewCategory(String name, String metrics) throws SQLException {
    String query = "INSERT INTO Review_Category(name,metrics) VALUES(?,?)";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, name);
      preStmt.setString(2, metrics);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Get review category Id by name
   *
   * @param name Review Category name
   * @return Id           Review Category Id
   */
  public int getCategoryIdByName(String name) throws SQLException {
    String query = "SELECT id FROM Review_Category WHERE name = ?";
    int id = 0;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, name);
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
   * Get all review category details
   */
  public List<ReviewCategory> getReviewCategory() throws SQLException {
    String query = "SELECT * FROM Review_Category";
    List<ReviewCategory> reviewCategories = new ArrayList<>();

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String metrics = rs.getString("metrics");
        ReviewCategory reviewCategory = new ReviewCategory();
        reviewCategory.setId(id);
        reviewCategory.setName(name);
        reviewCategory.setMetrics(metrics);
        reviewCategories.add(reviewCategory);
      }

    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewCategories;
  }

  /**
   * update review category by id
   *
   * @param id id
   * @param metrics metrics
   */
  public void editReviewCategoryById(int id, String metrics) throws SQLException {
    String query = "UPDATE Review_Category SET metrics = ? WHERE id = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, metrics);
      preStmt.setInt(2, id);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * delete review category by id
   *
   * @param id id
   */
  public void deleteReviewCategoryById(int id) throws SQLException {
    String query = "DELETE FROM Review_Category WHERE id = ?";

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

  /**
   * delete review category by name
   *
   * @param name name
   */
  public void deleteReviewCategoryByName(String name) throws SQLException {
    String query = "DELETE FROM Review_Category WHERE name = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, name);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
