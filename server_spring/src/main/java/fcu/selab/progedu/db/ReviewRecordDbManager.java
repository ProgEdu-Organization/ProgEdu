package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;
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

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRecordDbManager.class);

  /**
   * Insert review record into db
   *
   * @param rrsId       review record status Id
   * @param rsmId       review setting metrics Id
   * @param score       score
   * @param time        time
   * @param feedback    feedback
   */
  public void insertReviewRecord(int rrsId, int rsmId, int score, Date time,
                                 String feedback) throws SQLException {
    String query = "INSERT INTO Review_Record(rrsId, rsmId, score, time, feedback)"
        + " VALUES (?,?,?,?,?);";
    Timestamp timeTimestamp = new Timestamp(time.getTime());
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, rrsId);
      preStmt.setInt(2, rsmId);
      preStmt.setInt(3, score);
      preStmt.setTimestamp(4, timeTimestamp);
      preStmt.setString(5, feedback);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Get review record by Id
   *
   * @param id id
   * @return review record from specific owner, reviewer, assignment and question
   */
  public ReviewRecord getReviewRecordById(int id) throws SQLException {
    String query = "SELECT * FROM Review_Record WHERE id = ?";
    ReviewRecord reviewRecord = new ReviewRecord();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        int pmId = rs.getInt("pmId");
        int rsmId = rs.getInt("rsmId");
        int score = rs.getInt("score");
        Date time = rs.getTimestamp("time");
        String feedback = rs.getString("feedback");
        int reviewOrder = rs.getInt("reviewOrder");
        reviewRecord.setId(id);
        //reviewRecord.setPmId(pmId);
        reviewRecord.setRsmId(rsmId);
        reviewRecord.setScore(score);
        reviewRecord.setTime(time);
        reviewRecord.setFeedback(feedback);
        //reviewRecord.setReviewOrder(reviewOrder);
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewRecord;
  }

  /**
   * Get review record by specific pair matching Id
   *
   * @param pmId pair matching Id
   * @param order review order
   * @return review record from specific owner, reviewer and assignment
   */
  public List<ReviewRecord> getReviewRecordByPairMatchingId(int pmId, int order)
      throws SQLException {
    String query = "SELECT * FROM Review_Record WHERE pmId = ? AND reviewOrder = ?";
    List<ReviewRecord> reviewRecordList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pmId);
      preStmt.setInt(2, order);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        int rsmId = rs.getInt("rsmId");
        int score = rs.getInt("score");
        Date time = rs.getTimestamp("time");
        String feedback = rs.getString("feedback");
        int reviewOrder = rs.getInt("reviewOrder");
        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setId(id);
        //reviewRecord.setPmId(pmId);
        reviewRecord.setRsmId(rsmId);
        reviewRecord.setScore(score);
        reviewRecord.setTime(time);
        reviewRecord.setFeedback(feedback);
        //reviewRecord.setReviewOrder(reviewOrder);
        reviewRecordList.add(reviewRecord);
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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
  public boolean isFirstTimeReviewRecord(int pmId) throws SQLException {
    String query = "SELECT COUNT(*) AS isFirstTime FROM Review_Record WHERE pmId = ?";
    boolean isFirstTime = false;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pmId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int count = rs.getInt("isFirstTime");
        if (count == 0) {
          isFirstTime = true;
        }
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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
  public int getLatestReviewOrder(int pmId) throws SQLException {
    String query = "SELECT MAX(reviewOrder) AS latestCount FROM Review_Record WHERE pmId = ?";
    int latestCount = -1;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pmId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        latestCount = rs.getInt("latestCount");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return latestCount;
  }

  /**
   * delete review record by pair matching id
   *
   * @param pmId pair matching id
   * @throws SQLException SQLException
   */
  public void deleteReviewRecordByPmId(int pmId) throws SQLException {
    String query = "DELETE FROM Review_Record WHERE pmId = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pmId);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public List<ReviewRecord> getReviewRecordByRrsId(int rrsId) {
    String sql = "SELECT * FROM ProgEdu.Review_Record WHERE rrsId = ?;";
    List<ReviewRecord> reviewRecordList = new ArrayList<>();

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, rrsId);
      rs = preStmt.executeQuery();

      while (rs.next()) {
        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setId(rs.getInt("id"));
        reviewRecord.setRrsId(rrsId);
        reviewRecord.setRsmId(rs.getInt("rsmId"));
        reviewRecord.setScore(rs.getInt("score"));
        reviewRecord.setTime(rs.getTimestamp("time"));
        reviewRecord.setFeedback(rs.getString("feedback"));
        reviewRecordList.add(reviewRecord);
      }
    } catch (SQLException e) {
      LOGGER.error(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.debug(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewRecordList;
  }
}
