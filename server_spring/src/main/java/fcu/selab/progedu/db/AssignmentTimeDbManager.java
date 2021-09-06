package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
   * @param assignmentName assignment name
   * @param assignmentTime assignment time
   */
  public void addAssignmentTime(String assignmentName, AssignmentTime assignmentTime)  {
    String sql = "INSERT INTO Assignment_Time(`aId`, `aaId`, `releaseTime`, `deadline`) VALUES(?, ?, ?, ?)";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {

      Timestamp deadlineTime = new Timestamp(assignmentTime.getDeadline().getTime());
      Timestamp releaseTime = new Timestamp(assignmentTime.getReleaseTime().getTime());
      Integer actionId = aaDb.getAssignmentActionIdByAction(assignmentTime.getActionEnum().toString());
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aDb.getAssignmentIdByName(assignmentName)); //aId
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
   * get assignment time by assignment name
   * @param name assignment name
   * @return assignment time
   */
  public List<AssignmentTime> getAssignmentTimeByName(String name) {
    String sql = "SELECT a_t.* FROM Assignment_Time a_t join Assignment a on a.id = a_t.aId where a.name = ?";

    List<AssignmentTime> assignmentTimeList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        AssignmentTime assignmentTime = new AssignmentTime();
        assignmentTime.setId(rs.getInt("id"));
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setActionEnum(aaDb.getAssignmentActionById(rs.getInt("aaId")));
        assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignmentTime.setDeadline(rs.getTimestamp("deadline"));
        assignmentTimeList.add(assignmentTime);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentTimeList;
  }

  /**
   * get assignment time name by id
   * @param aId aid
   * @return assignment name
   */
  public List<AssignmentTime> getAssignmentTimeNameById(int aId) {
    String sql = "SELECT * FROM Assignment_Time WHERE aId = ?";

    List<AssignmentTime> assignmentTimeList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, aId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        AssignmentTime assignmentTime = new AssignmentTime();
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setActionEnum(aaDb.getAssignmentActionById(rs.getInt("aaId")));
        assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignmentTime.setDeadline(rs.getTimestamp("deadline"));
        assignmentTimeList.add(assignmentTime);
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentTimeList;
  }

  public void editAssignmentTime(AssignmentTime assignmentTime) {
    String sql = "UPDATE Assignment_Time SET releaseTime = ? , deadline = ? WHERE aId ? AND aaId = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    Timestamp releaseTimestamp = new Timestamp(assignmentTime.getReleaseTime().getTime());
    Timestamp deadlineTimestamp = new Timestamp(assignmentTime.getDeadline().getTime());

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setTimestamp(1, releaseTimestamp);
      preStmt.setTimestamp(2, deadlineTimestamp);
      preStmt.setInt(3, assignmentTime.getAId());
      preStmt.setInt(4, aaDb.getAssignmentActionIdByAction(assignmentTime.getActionEnum().toString()));
      preStmt.executeUpdate();

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public AssignmentTime getAssignmentTimeByCurrentTime(String name) {
    String sql = "SELECT a_t.* FROM ProgEdu.Assignment_Time a_t join ProgEdu.Assignment a on a.id = a_t.aId WHERE " +
            "a.name = ? AND ( ? between a_t.releaseTime and a_t.deadline)";

    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
    Date currentDate = new Date();

    Timestamp currentDateStamp = new Timestamp(currentDate.getTime());

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    AssignmentTime assignmentTime = new AssignmentTime();

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      preStmt.setTimestamp(2, currentDateStamp);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setActionEnum(aaDb.getAssignmentActionById(rs.getInt("aaId")));
        assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignmentTime.setDeadline(rs.getTimestamp("deadline"));
      }

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentTime;
  }
  public void deleteAssignmentTimeById(int id){
    String sql = "DELETE FROM Assignment_Time WHERE aid = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public void deleteAssignmentTimeByName(String name){
    String sql = "DELETE FROM Assignment_Time a_t JOIN Assignment a ON a.id = a_t.aId WHERE a.name = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  public AssignmentTime getAssignmentTimeByTimeAndName(String name, Date time) {
    String sql = "SELECT a_t.* FROM ProgEdu.Assignment_Time a_t join ProgEdu.Assignment a on a.id = a_t.aId WHERE " +
            "a.name = ? AND ( ? between a_t.releaseTime and a_t.deadline)";

    Timestamp dateStamp = new Timestamp(time.getTime());

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    AssignmentTime assignmentTime = new AssignmentTime();

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      preStmt.setTimestamp(2, dateStamp);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setActionEnum(aaDb.getAssignmentActionById(rs.getInt("aaId")));
        assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignmentTime.setDeadline(rs.getTimestamp("deadline"));
      }

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentTime;
  }

  public AssignmentTime getAssignmentTimeByaIdAndTime(int aId, Date time) {
    String sql = "SELECT * FROM ProgEdu.Assignment_Time WHERE aId = ? AND ( ? between releaseTime and deadline)";

    Timestamp dateStamp = new Timestamp(time.getTime());
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    AssignmentTime assignmentTime = new AssignmentTime();

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, aId);
      preStmt.setTimestamp(2, dateStamp);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setActionEnum(aaDb.getAssignmentActionById(rs.getInt("aaId")));
        assignmentTime.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignmentTime.setDeadline(rs.getTimestamp("deadline"));
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assignmentTime;
  }

  public int getAssignmentTotalRound(int aId) {
    String sql = "SELECT COUNT(*) AS round FROM ProgEdu.Assignment_Time WHERE aId = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    int round = 0;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, aId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        round = rs.getInt("round") / 2;
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return round;
  }

}
