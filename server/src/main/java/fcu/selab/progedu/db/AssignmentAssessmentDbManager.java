package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.status.StatusEnum;

public class AssignmentAssessmentDbManager {

  public static AssignmentAssessmentDbManager dbManager = new AssignmentAssessmentDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance(); 
  
  public static AssignmentAssessmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AssignmentAssessmentDbManager.class);

  /**
   * Add AssignmentAssessment to database
   *
   * @param aid Assignment Id
   * @param sid status name
   * @param order Assessment order
   * @param score Assessment score
   */
  public void addAssignmentAssessment(int aid, int sid, int order, int score) {
    String sql = "INSERT INTO ProgEdu.Assignment_Assessment"
        + " (`aid`, `status`, `order`, `score`)"
        + " VALUES(?, ?, ?, ?)";
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, sid);
      preStmt.setInt(3, order);
      preStmt.setInt(4, score);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * update score to database
   *
   * @param aid Assignment Id
   * @param order Assessment order
   * @param score Assessment score
   */
  public void updateScore(int aid, int order, int score) {
    String sql = "UPDATE ProgEdu.Assignment_Assessment"
        + " SET `score` = ? WHERE `id` = ?";
    int id = getAssignmentAssessmentId(aid, order);
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, score);
      preStmt.setInt(2, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get AssignmentAssessment id from database
   * 
   * @param aid Assignment Id
   * @param order Assignment order
   * @return AssignmentAssessmentId AssignmentAssessmentId
   */
  public int getAssignmentAssessmentId(int aid, int order) {
    int id = -1;
    String sql = "SELECT `id` FROM ProgEdu.Assignment_Assessment"
        + " WHERE `aId` = ? AND `order` = ?";
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, order);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return id;
  }

  /**
   * get AssignmentAssessment ids by aid from database
   * 
   * @param aid Assignment Id
   * @return aaIds AssignmentAssessmentIds
   */
  public List<Integer> getAssignmentAssessmentIdByaId(int aid) {
    List<Integer> aaIds = new ArrayList<>();
    String sql = "SELECT `id` FROM ProgEdu.Assignment_Assessment"
        + " WHERE `aId` = ?";
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int aaId = rs.getInt("id");
          aaIds.add(aaId);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return aaIds;
  }

  /**
   * Delete AssignmentAssessment from database
   * 
   * @param id Assignment Assessment Id
   */
  public void deleteAssignmentAssessment(int id) {
    String sql = "DELETE FROM ProgEdu.Assignment_Assessment WHERE `id` = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get Assignment order from database
   * 
   * @param aid Assignment Id
   * @param status status
   * @return assessment order
   */
  public int getAssessmentOrder(int aid, int status) {
    String sql = "SELECT `order` FROM ProgEdu.Assignment_Assessment" 
        + " WHERE `aId` = ? AND `status` = ?";
    int order = -1;
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, status);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          order = rs.getInt("order");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return order;
  }

  /**
   * get Assignment order from database
   *
   * @param aaid Assignment Assessment Id
   * @return assessment order
   */
  public int getAssessmentOrder(int aaid) {
    String sql = "SELECT `order` FROM ProgEdu.Assignment_Assessment"
            + " WHERE `id` = ?";
    int order = -1;
    try (Connection conn = this.database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aaid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          order = rs.getInt("order");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return order;
  }

  /**
   * get Assignment score from database
   * 
   * @param aid Assignment Id
   * @param order order
   * @return total score
   */
  public int getScore(int aid, int order) {
    String sql = "SELECT SUM(`score`) FROM ProgEdu.Assignment_Assessment"
        + " WHERE `aId` = ? AND `order` < ?";
    int score = 0;
    try (Connection conn = this.database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, order);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          score = rs.getInt("SUM(`score`)");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return score;
  }

  /**
  * get Assignment Assessment Order
  * 
  * @param aid Assignment Id
  */
  public String getAssignmentOrderAndScore(int aid) {
    String orders = "";
    String sql = "SELECT `status`,`score` FROM ProgEdu.Assignment_Assessment"
        + " WHERE `aId` = ? ORDER BY `order`";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          if (!orders.isEmpty()) {
            orders = orders + ", ";
          }
          int status = rs.getInt("status");
          int score = rs.getInt("score"); 
          StatusEnum statusEnum = csDb.getStatusNameById(status);
          orders = orders + statusEnum.toString()
            + ":" + score;
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return orders;
  }
}
