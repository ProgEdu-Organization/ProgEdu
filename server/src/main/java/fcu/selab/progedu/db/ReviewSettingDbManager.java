package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewSettingDbManager {

  private static ReviewSettingDbManager dbManager = new ReviewSettingDbManager();

  public static ReviewSettingDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSettingDbManager.class);

  /**
   * Insert assignment review setting into db
   *
   * @param aid         assignment Id
   * @param amount      reviewer amount
   * @param releaseTime releaseTime
   * @param deadline    deadline
   */
  public void insertReviewSetting(int aid, int amount, Date releaseTime, Date deadline) {
    String query = "INSERT INTO Review_Setting(aId, amount, releaseTime, deadline)"
        + " VALUES(?,?,?,?)";
    Timestamp releaseTimestamp = new Timestamp(releaseTime.getTime());
    Timestamp deadlineTimestamp = new Timestamp(deadline.getTime());

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, aid);
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
   * @param aid assignment Id
   * @return review setting details
   */
  public ReviewSetting getReviewSetting(int aid) {
    String query = "SELECT * FROM Review_Setting WHERE aId = ?";
    ReviewSetting reviewSetting = new ReviewSetting();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, aid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int amount = rs.getInt("amount");
          Date releaseTime = rs.getDate("releaseTime");
          Date deadline = rs.getDate("deadline");
          reviewSetting.setId(id);
          reviewSetting.setaId(aid);
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
   * @param aid assignment Id
   * @return amount reviewer amount
   */
  public int getReviewSettingAmountByAId(int aid) {
    String query = "SELECT amount FROM Review_Setting WHERE aId = ?";
    int amount = 0;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, aid);
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
   * @param aid assignment Id
   */
  public void deleteReviewSettingByAId(int aid) {
    String query = "DELETE FROM Review_Setting WHERE aId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, aid);
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
