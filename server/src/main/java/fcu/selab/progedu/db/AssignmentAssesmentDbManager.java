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

public class AssignmentAssesmentDbManager {

  public static AssignmentAssesmentDbManager dbManager = new AssignmentAssesmentDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance(); 
  
  public static AssignmentAssesmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AssignmentAssesmentDbManager.class);

  /**
   * Add AssignmentAssesment to database
   * 
   * @param aid Assignment Id
   * @param status status Id
   * @param order Assignment order
   */
  public void addAssignmentAssesment(int aid,StatusEnum status,int order) {
    String sql = "INSTERT INTO Assignment_Assesment"
        + "(aId, status, order) "
        + "VALUES(?, ?, ?, ?)";
    int statusId = csDb.getStatusIdByName(status.getType());
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, statusId);
      preStmt.setInt(3, order);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
  
  /**
   * Add AssignmentAssesment to database
   * 
   * @param aid Assignment Id
   */
  public List<Assignment> getAssignmentOrder(int aid) {
    List<Assignment> orders = new ArrayList<>();
    String sql = "SELECT status,order FROM Assignment_Assesment"
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
  }

  /**
   * Delete AssignmentAssesment to database
   * 
   * @param aid Assignment Id
   */
  public void deleteAssignmentAssesment(int aid) {
    String sql = "DELETE FROM Assignment_Assesment WHERE aId = ?";

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