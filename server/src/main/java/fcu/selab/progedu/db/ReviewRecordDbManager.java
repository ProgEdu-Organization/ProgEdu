package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.ReviewRecord;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewRecordDbManager {

  private static ReviewRecordDbManager dbManager = new ReviewRecordDbManager();

  public static ReviewRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRecordDbManager.class);

  /**
   * Insert review record into db
   *
   * @param pmId        pair matching Id
   * @param rsmId       review setting metrics Id
   * @param score       score
   * @param time        time
   * @param feedback    feedback
   * @param reviewOrder reviewOrder
   */
  public void insertReviewRecord(int pmId, int rsmId, int score,
                                 Date time, String feedback, int reviewOrder) {
    String query = "INSERT INTO Review_Record(pmId, rsmId, score, time, feedback, reviewOrder)"
        + " VALUES (?,?,?,?,?,?);";
    Timestamp timeTimestamp = new Timestamp(time.getTime());

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pmId);
      preStmt.setInt(2, rsmId);
      preStmt.setInt(3, score);
      preStmt.setTimestamp(4, timeTimestamp);
      preStmt.setString(5, feedback);
      preStmt.setInt(6, reviewOrder);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Get review record by Id
   *
   * @param id id
   * @return review record from specific owner, reviewer, assignment and question
   */
  public ReviewRecord getReviewRecordById(int id) {
    String query = "SELECT * FROM Review_Record WHERE id = ?";
    ReviewRecord reviewRecord = new ReviewRecord();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int pmId = rs.getInt("pmId");
          int rsmId = rs.getInt("rsmId");
          int score = rs.getInt("score");
          Date time = rs.getDate("time");
          String feedback = rs.getString("feedback");
          int reviewOrder = rs.getInt("reviewOrder");
          reviewRecord.setId(id);
          reviewRecord.setPmId(pmId);
          reviewRecord.setRsmId(rsmId);
          reviewRecord.setScore(score);
          reviewRecord.setTime(time);
          reviewRecord.setFeedback(feedback);
          reviewRecord.setReviewOrder(reviewOrder);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return reviewRecord;
  }

  /**
   * Get review record by specific pair matching Id
   *
   * @param pmId pair matching Id
   * @return review record from specific owner, reviewer and assignment
   */
  public List<ReviewRecord> getReviewRecordByPairMatchingId(int pmId) {
    String query = "SELECT * FROM Review_Record WHERE pmId = ?";
    List<ReviewRecord> reviewRecordList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pmId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int rsmId = rs.getInt("rsmId");
          int score = rs.getInt("score");
          Date time = rs.getDate("time");
          String feedback = rs.getString("feedback");
          int reviewOrder = rs.getInt("reviewOrder");
          ReviewRecord reviewRecord = new ReviewRecord();
          reviewRecord.setId(id);
          reviewRecord.setPmId(pmId);
          reviewRecord.setRsmId(rsmId);
          reviewRecord.setScore(score);
          reviewRecord.setTime(time);
          reviewRecord.setFeedback(feedback);
          reviewRecord.setReviewOrder(reviewOrder);
          reviewRecordList.add(reviewRecord);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return reviewRecordList;
  }

  /**
   * Whether the reviewer reviewed specific pair matching in the first time or not
   *
   * @param pmId pair matching id
   *
   * @return isFirstTime  return boolean
   */
  public boolean isFirstTimeReviewRecord(int pmId) {
    String query = "SELECT COUNT(*) AS isFirstTime FROM Review_Record WHERE pmId = ?";
    boolean isFirstTime = false;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pmId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int count = rs.getInt("isFirstTime");
          if (count == 0) {
            isFirstTime = true;
          }
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return isFirstTime;
  }

  /**
   * Get latest review order number for specific pair matching
   *
   * @param pmId pair matching id
   *
   * @return reviewOrder review order
   */
  public int getLatestReviewOrder(int pmId) {
    String query = "SELECT MAX(reviewOrder) AS latestCount FROM Review_Record WHERE pmId = ?";
    int latestCount = -1;

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pmId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          latestCount = rs.getInt("latestCount");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return latestCount;
  }

}
