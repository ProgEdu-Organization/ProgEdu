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

public class ReviewRecordDbManager {

  private static ReviewRecordDbManager dbManager = new ReviewRecordDbManager();

  public static ReviewRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Insert review record into db
   *
   * @param roId        pair matching Id
   * @param rsmId       review setting metrics Id
   * @param score       score
   * @param time        time
   * @param feedback    feedback
   * @param reviewOrder reviewOrder
   */
  public void insertReviewRecord(int roId, int rsmId, int score, Date time,
                                 String feedback, int reviewOrder) throws SQLException {
    String query = "INSERT INTO Review_Record(roId, rsmId, score, time, feedback, round)"
        + " VALUES (?,?,?,?,?,?);";
    Timestamp timeTimestamp = new Timestamp(time.getTime());
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roId);
      preStmt.setInt(2, rsmId);
      preStmt.setInt(3, score);
      preStmt.setTimestamp(4, timeTimestamp);
      preStmt.setString(5, feedback);
      preStmt.setInt(6, reviewOrder);
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
        int roId = rs.getInt("roId");
        int rsmId = rs.getInt("rsmId");
        int score = rs.getInt("score");
        Date time = rs.getTimestamp("time");
        String feedback = rs.getString("feedback");
        int round = rs.getInt("round");
        reviewRecord.setId(id);
        reviewRecord.setRoId(roId);
        reviewRecord.setRsmId(rsmId);
        reviewRecord.setScore(score);
        reviewRecord.setTime(time);
        reviewRecord.setFeedback(feedback);
        reviewRecord.setRound(round);
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewRecord;
  }

  /**
   * Get review record by specific review order Id
   *
   * @param roId review order Id
   * @param order review order
   * @return review record from specific owner, reviewer and assignment
   */
  public List<ReviewRecord> getReviewRecordByReviewOrderId(int roId, int order)
      throws SQLException {
    String query = "SELECT * FROM Review_Record WHERE roId = ? AND reviewOrder = ?";
    List<ReviewRecord> reviewRecordList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roId);
      preStmt.setInt(2, order);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        int rsmId = rs.getInt("rsmId");
        int score = rs.getInt("score");
        Date time = rs.getTimestamp("time");
        String feedback = rs.getString("feedback");
        int round = rs.getInt("round");
        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setId(id);
        reviewRecord.setRoId(roId);
        reviewRecord.setRsmId(rsmId);
        reviewRecord.setScore(score);
        reviewRecord.setTime(time);
        reviewRecord.setFeedback(feedback);
        reviewRecord.setRound(round);
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
   * @param roId pair matching id
   *
   * @return isFirstTime  return boolean
   */
  public boolean isFirstTimeReviewRecord(int roId) throws SQLException {
    String query = "SELECT COUNT(*) AS isFirstTime FROM Review_Record WHERE roId = ?";
    boolean isFirstTime = false;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roId);
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
   * @param roId pair matching id
   *
   * @return reviewOrder review order
   */
  public int getLatestRound(int roId) throws SQLException {
    String query = "SELECT MAX(round) AS latestCount FROM Review_Record WHERE roId = ?";
    int latestCount = -1;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roId);
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
   * @param roId pair matching id
   * @throws SQLException SQLException
   */
  public void deleteReviewRecordByPmId(int roId) throws SQLException {
    String query = "DELETE FROM Review_Record WHERE roId = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roId);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
