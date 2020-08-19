package fcu.selab.progedu.db;

import java.sql.*;

import fcu.selab.progedu.data.ReviewSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class ReviewSettingDbManager {

    private static ReviewSettingDbManager dbManager = new ReviewSettingDbManager();

    public static ReviewSettingDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSettingDbManager.class);

    /**
     * Insert assignment review setting into db
     *
     * @param aId               assignment Id
     * @param amount            reviewer amount
     * @param releaseTime       releaseTime
     * @param deadline          deadline
     */
    public void insertReviewSetting(int aId, int amount, Date releaseTime, Date deadline) {
        String query = "INSERT INTO Review_Setting(aId, amount, releaseTime, deadline)"
                + " VALUES(?,?,?,?);";
        Timestamp releaseTimestamp = new Timestamp(releaseTime.getTime());
        Timestamp deadlineTimestamp = new Timestamp(deadline.getTime());

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, aId);
            preStmt.setInt(2, amount);
            preStmt.setTimestamp(3, releaseTimestamp);
            preStmt.setTimestamp(4, deadlineTimestamp);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Get all from review setting by assignment Id
     *
     * @param aId assignment Id
     * @return review setting details
     */
    public ReviewSetting getReviewSetting(int aId) {
        String query = "SELECT * FROM Review_Setting WHERE aId = ?";
        ReviewSetting reviewSetting = new ReviewSetting();

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, aId);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int amount = rs.getInt("amount");
                    Date releaseTime = rs.getDate("releaseTime");
                    Date deadline = rs.getDate("deadline");
                    reviewSetting.setId(id);
                    reviewSetting.setaId(aId);
                    reviewSetting.setAmount(amount);
                    reviewSetting.setReleaseTime(releaseTime);
                    reviewSetting.setDeadline(deadline);
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return reviewSetting;
    }


    /**
     * Get amount by aId
     *
     * @param aId assignment Id
     * @return amount reviewer amount
     */
    public int getReviewSettingAmountByAId(int aId) {
        String query = "SELECT amount FROM Review_Setting WHERE aId = ?";
        int amount = 0;

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, aId);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    amount = rs.getInt("amount");
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return amount;
    }

    /**
     * delete review setting of specific assignment Id
     *
     * @param aId assignment Id
     */
    public void deleteReviewSettingByAId(int aId) {
        String query = "DELETE FROM Review_Setting WHERE aId = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, aId);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * delete review setting of specific Id
     *
     * @param id Id
     */
    public void deleteReviewSettingById(int id) {
        String query = "DELETE FROM Review_Setting WHERE id = ?";

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
