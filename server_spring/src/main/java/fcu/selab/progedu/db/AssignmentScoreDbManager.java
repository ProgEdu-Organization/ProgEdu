package fcu.selab.progedu.db;

import fcu.selab.progedu.data.AssignmentScore;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentScoreDbManager {

  private static AssignmentScoreDbManager dbManager = new AssignmentScoreDbManager();

  public static AssignmentScoreDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentScoreDbManager.class);

  /**
   * Add Assignment Score to database
   *
   * @param auid Assignment user id
   * @param score Score
   */
  public void addAssignmentScore(AssignmentScore assignmentScore) {
    String sql = "INSERT INTO ProgEdu.Assignment_Score (`auId`, `score`)  VALUES(?, ?)";
    Connection connection = null;
    PreparedStatement preStmt = null;

    try {
      connection = database.getConnection();
      preStmt = connection.prepareStatement(sql);

      preStmt.setInt(1, assignmentScore.getAuid());
      preStmt.setInt(2, assignmentScore.getScore());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, connection);
    }
  }

  /**
   * get assignment score by assignment id
   *
   * @param aid Assignment id
   * @return assignment score mean
   */
  public int getAssignmentMeanByAssignmentId(int aid) {
    String sql = "SELECT `score` FROM (" +
            "SELECT avg(`score`) AS `score`, `aid` FROM ProgEdu.Assignment_Score " +
            "INNER JOIN ProgEdu.Assignment_User ON auId = Assignment_User.id GROUP BY `aId`)" +
            " AS Assignment_Average WHERE `aid` = ? ";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    int score = 0;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aid);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        score = rs.getInt("score");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return score;
  }

  /**
   * Get all graded assignments ids
   *
   * @return assignment id list
   */
  public List<Integer> getAllGradedAssignments() {
    String sql = "SELECT `aid` FROM (" +
            "SELECT avg(`score`) AS `score`, `aid` FROM ProgEdu.Assignment_Score " +
            "INNER JOIN ProgEdu.Assignment_User ON `auId` = Assignment_User.id GROUP BY `aId`) " +
            "AS Assignment_Average";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    List<Integer> assignmentIds = new ArrayList<>();

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      rs = preStmt.executeQuery();

      while (rs.next()) {
        assignmentIds.add(rs.getInt("aid"));
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentIds;
  }

  /**
   * Get score by auId
   *
   * @param auId Assignment User id
   * @return assignment id list
   */
  public int getScoreByAuId(int auId) {
    String sql = "SELECT `score` FROM ProgEdu.Assignment_Score WHERE `auId` = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    int score = -1;
    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, auId);

      rs = preStmt.executeQuery();

      while (rs.next()) {
        score = rs.getInt("score");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return score;
  }

  /**
   * Delete assignment score by auid
   *
   */
  public void deleteAssignmentScoreByAuId(int auId) {
    String sql = "DELETE FROM ProgEdu.Assignment_Score WHERE (`auId` = ?)";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, auId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Get all graded assignments ids
   *
   * @param auId Assignment User id
   * @param score score
   * @return assignment id list
   */
  public void updateAssignmentScoreByAuId(int auId, int score) {
    String sql = "UPDATE ProgEdu.Assignment_Score SET `score` = ? WHERE (`auId` = ?)";

    Connection connection = null;
    PreparedStatement preStmt = null;

    try {
      connection = database.getConnection();
      preStmt = connection.prepareStatement(sql);

      preStmt.setInt(1, score);
      preStmt.setInt(2, auId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, connection);
    }
  }
}
