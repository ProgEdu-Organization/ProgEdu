package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.ReviewCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class ReviewCategoryDbManager {

    private static ReviewCategoryDbManager dbManager = new ReviewCategoryDbManager();

    public static ReviewCategoryDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewCategoryDbManager.class);

    /**
     * insert new review category into db
     *
     * @param name          name
     * @param metrics       metrics
     */
    public void insertReviewCategory(String name, String metrics) {
        String query = "INSERT INTO Review_Category(name,metrics) VALUES(?,?)";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setString(1, name);
            preStmt.setString(2, metrics);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Get review category Id by name
     *
     * @param name          Review Category name
     * @return Id           Review Category Id
     */
    public int getCategoryIdByName(String name) {
        String query = "SELECT id FROM Review_Category WHERE name = ?";
        int Id = 0;

        try(Connection conn = database.getConnection();
            PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setString(1, name);
            try (ResultSet rs = preStmt.executeQuery();) {
                while (rs.next()) {
                    Id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return Id;
    }

    /**
     * Get all review category details
     */
    public List<ReviewCategory> getReviewCategory() {
        String query = "SELECT * FROM Review_Category";
        List<ReviewCategory> reviewCategories = new ArrayList<>();

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            try (ResultSet rs = preStmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return reviewCategories;
    }

    /**
     * delete review category by id
     *
     * @param id            id
     */
    public void deleteReviewCategoryById(int id) {
        String query = "DELETE FROM Review_Category WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * delete review category by name
     *
     * @param name          name
     */
    public void deleteReviewCategoryByName(String name) {
        String query = "DELETE FROM Review_Category WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setString(1, name);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

}
