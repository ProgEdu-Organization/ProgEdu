package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.AssignmentTime;
import fcu.selab.progedu.service.AssignmentActionEnum;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentTimeDbManager {

  AssignmentActionDbManager aaDb = AssignmentActionDbManager.getInstance();
  AssignmentDbManager aDb = AssignmentDbManager.getInstance();


  private static AssignmentTimeDbManager dbManager = new AssignmentTimeDbManager();

  public static AssignmentTimeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentDbManager.class);

  private AssignmentTimeDbManager(){

  }

  /**
   * Add assignment time to db
   * @param assignmnetName assignment name
   * @param assignmentTime assugnment time
   */
  public void addAssignmentTime(String assignmnetName, AssignmentTime assignmentTime) {
    String sql = "INSERT INTO Assignment_Time(`aId`, `aaId`, `releaseTime`, `deadline` VALUES(?, ?, ?, ?)";

    Timestamp deadlineTime = new Timestamp(assignmentTime.getDeadline().getTime());
    Timestamp releaseTime = new Timestamp(assignmentTime.getReleaseTime().getTime());
    int actionId = aaDb.getAssignmentActionIdByAction(assignmentTime.getActionEnum().getActionName());

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aDb.getAssignmentIdByName(assignmnetName)); //aId
      preStmt.setInt(2, actionId); //aaId
      preStmt.setTimestamp(3, releaseTime); //releaseTime
      preStmt.setTimestamp(4, deadlineTime); //deadlineTime
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * get assignemnt time by assignment name
   * @param name assignment name
   * @return assignment time
   */
  public AssignmentTime getAssignmentTimeByName(String name) {
    AssignmentTime assignmentTime = new AssignmentTime();
    String sql = "SELECT a_t.* FROM Assignment_Time a_t join Assignment a on a.id = a_t.aId where a.name = ?";

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          assignmentTime.setId(rs.getInt("id"));
          assignmentTime.setAId(rs.getInt("aId"));
          assignmentTime.setAaId(rs.getInt("aaId"));
          assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
          assignmentTime.setDeadline(rs.getTimestamp("deadline"));
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(stmt, conn);
    }
    return assignmentTime;
  }

  /**
   * get assignment time name by id
   * @param aId aid
   * @return assignment name
   */
  public AssignmentTime getAssignmentTimeNameById(int aId) {
    String sql = "SELECT * FROM Assignment_Time WHERE aId = ?";
    AssignmentTime assignmentTime = new AssignmentTime();

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aId);

      try (ResultSet rs = preStmt.executeQuery();){
        while (rs.next()) {
          assignmentTime.setAId(rs.getInt("aId"));
          assignmentTime.setAaId(rs.getInt("aaId"));
          assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
          assignmentTime.setDeadline(rs.getTimestamp("deadline"));
        }
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
    return assignmentTime;
  }

  public void editAssignmentTime(AssignmentTime assignmentTime, int id) {
    String sql = "UPDATE Assignment_Time SET `releaseTime` = ? , `deadline` = ? WHERE id = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;
    Timestamp releaseTime = new Timestamp(assignmentTime.getReleaseTime().getTime());
    Timestamp deadlineTime = new Timestamp(assignmentTime.getDeadline().getTime());

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setTimestamp(1, releaseTime);
      preStmt.setTimestamp(2, deadlineTime);
      preStmt.setInt(3, id);
      preStmt.executeUpdate();

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
