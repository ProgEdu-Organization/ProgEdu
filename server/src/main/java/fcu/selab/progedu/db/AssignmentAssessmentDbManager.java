package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.status.StatusEnum;

public class AssignmentAssessmentDbManager {

  public static AssignmentAssessmentDbManager dbManager = new AssignmentAssessmentDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance();

  public static AssignmentAssessmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AssignmentAssessmentDbManager.class);

  /**
   * Add AssignmentAssessment to database
   *
   * @param aid Assignment Id
   * @param sid status name
   * @param order Assessment order
   */
  public void addAssignmentAssessment(int aid,int sid,int order) {
    String sql = "INSERT INTO Assignment_Assessment(`aId`, `status`, `order`) VALUES(?, ?, ?)";
    try (Connection conn = this.database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, sid);
      preStmt.setInt(3, order);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Add AssignmentAssessment to database
   *
   * @param aid Assignment Id
   */
  /*public List<Assignment> getAssignmentOrder(int aid) {
    List<Assignment> orders = new ArrayList<>();
    String sql = "SELECT status,order FROM Assignment_Assessment"
        + " WHERE aId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          Assignment assignment = new Assignment();
          int status = rs.getInt("status");
          int order = rs.getInt("order");
          StatusEnum statusEnum = csDb.getStatusNameById(status);
          assignment.setStatus(statusEnum);
          assignment.getOrder(order);
          orders.add(assignment);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return orders;
  }*/

  /**
   * Delete AssignmentAssessment to database
   *
   * @param aid Assignment Id
   */
  public void deleteAssignmentAssessment(int aid) {
    String sql = "DELETE FROM Assignment_Assessment WHERE aId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}
