package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.rmi.runtime.LoggerLogFactory;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.project.status.StatusEnum;

public class AssignmentAssesmentDbManager {

  public static AssignmentAssesmentDbManager dbManager = new AssignmentAssesmentDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance(); 
  public static AssignmentAssesmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerLogFactory.getLogger(AssignmentAssesmentDbManager.class);

  /**
   * Add AssignmentAssesment to database
   * 
   * @param aid Assignment Id
   * @param status status Id
   * @param order Assignment order
   */
  public void addAssignmentAssesment(int aid,StatusEnum status,int order) {
    String sql = "INSTERT INTO Assignment_Assesment"
        +"(aId, status, order) "
        + "VALUES(?, ?, ?, ?)";
    int statusId = csDb.getStatusNameById(status.getType());
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
    List<Assignment> Isorders = new ArrayList<>();
    String sql = "SELECT status,order FROM Assignment_Assesment"
        +" WHERE aId = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      try(ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          Assignment assignment = new Assignment();
          int status = rs.getInt("status");
          int order = rs.getInt("order");
          StatusEnum statusEnum = csDb.getStatusNameById(status);
          assignment.setStatus(statusEnum);
          assignment.getOrder(order);
          Isorders.add(assignment);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER(e.getMessage());
    }
    return Isorders;
  } 
}