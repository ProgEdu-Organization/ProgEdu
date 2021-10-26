package fcu.selab.progedu.db;

import fcu.selab.progedu.data.ReviewRecordStatus;
import fcu.selab.progedu.service.ReviewStatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewRecordStatusDbManager {

  private static ReviewRecordStatusDbManager dbManager = new ReviewRecordStatusDbManager();

  public static ReviewRecordStatusDbManager getInstance() {
    return dbManager;
  }

  public IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRecordStatusDbManager.class);

  private ReviewRecordStatusDbManager() {

  }

  private ReviewStatusDbManager rsDbManager = ReviewStatusDbManager.getInstance();

  public ReviewRecordStatus getReviewRecordStatusByPairMatchingIdAndRound(int pmId, int round) {
    String sql = "SELECT RRS.* FROM ProgEdu.Pair_Matching AS PM, ProgEdu.Review_Record_Status AS RRS WHERE PM.id = RRS.pmId AND PM.id = ? AND RRS.round = ?;";
    ReviewRecordStatus reviewRecordStatus = new ReviewRecordStatus();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setInt(1, pmId);
      stmt.setInt(2, round);
      rs = stmt.executeQuery();
      while (rs.next()) {
        reviewRecordStatus.setId(rs.getInt("id"));
        reviewRecordStatus.setPmId(pmId);
        reviewRecordStatus.setReviewStatusEnum(rsDbManager.getReviewStatusById(rs.getInt("status")));
        reviewRecordStatus.setRound(round);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return reviewRecordStatus;
  }

  public List<ReviewRecordStatus> getAllReviewRecordStatusByPairMatchingId(int pmId) {
    String sql = "SELECT RRS.* FROM ProgEdu.Pair_Matching AS PM, ProgEdu.Review_Record_Status AS RRS WHERE PM.id = RRS.pmId AND PM.id = ?;";
    List<ReviewRecordStatus> reviewRecordStatusList = new ArrayList<>();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setInt(1, pmId);

      rs = stmt.executeQuery();
      while (rs.next()) {
        ReviewRecordStatus reviewRecordStatus = new ReviewRecordStatus();
        reviewRecordStatus.setId(rs.getInt("id"));
        reviewRecordStatus.setPmId(pmId);
        reviewRecordStatus.setReviewStatusEnum(rsDbManager.getReviewStatusById(rs.getInt("status")));
        reviewRecordStatus.setRound(rs.getInt("round"));
        reviewRecordStatusList.add(reviewRecordStatus);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return reviewRecordStatusList;
  }

  public ReviewRecordStatus getLatestCompletedReview(int pmId) {
    String sql = "SELECT * FROM Review_Record_Status WHERE pmId = ? AND status = ? ORDER BY round desc LIMIT 1;";
    ReviewRecordStatus latestCompletedReview = new ReviewRecordStatus();

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pmId);
      preStmt.setInt(2, 3);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        latestCompletedReview.setId(rs.getInt("id"));
        latestCompletedReview.setPmId(rs.getInt("pmId"));
        latestCompletedReview.setReviewStatusEnum(rsDbManager.getReviewStatusById(rs.getInt("status")));
        latestCompletedReview.setRound(rs.getInt("round"));
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return latestCompletedReview;
  }

  public void insertReviewRecordStatus(int pmId, ReviewStatusEnum reviewStatusEnum, int round) {
    String sql = "INSERT INTO Review_Record_Status(pmId, status, round) VALUES (? ,? , ?)";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      int reviewStatus = rsDbManager.getReviewStatusIdByStatus(reviewStatusEnum.getTypeName());

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pmId);
      preStmt.setInt(2, reviewStatus);
      preStmt.setInt(3, round);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public void updateReviewRecordStatusByPmId(int status, int pmId, int round) throws SQLException {
    String query = "UPDATE ProgEdu.Review_Record_Status SET status = ? WHERE pmId = ? AND round = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, status);
      preStmt.setInt(2, pmId);
      preStmt.setInt(3, round);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public int getIdByPmIdAndRound(int pmId, int round) {
    String sql = "SELECT id FROM Review_Record_Status WHERE pmId = ? AND round = ?";
    int id = 0;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pmId);
      preStmt.setInt(2, round);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        id = rs.getInt("id");
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return id;
  }

  /**
   * delete review record status by pmId
   * @param pmId pmId
   * @throws SQLException SQLException
   */
  public void deleteReviewRecordStatusByPmId(int pmId) throws SQLException {
    String query = "DELETE FROM Review_Record_Status WHERE pmId = ?";
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

}
