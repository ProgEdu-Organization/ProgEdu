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
import fcu.selab.progedu.data.AssessmentTime;
import fcu.selab.progedu.service.AssessmentActionEnum;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.utils.ExceptionUtil;


public class AssessmentTimeDbManager {
  AssessmentActionDbManager aaDb = AssessmentActionDbManager.getInstance();
  AssignmentDbManager aDb = AssignmentDbManager.getInstance();

  private static AssessmentTimeDbManager dbManager = new AssessmentTimeDbManager();

  public static AssessmentTimeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentTimeDbManager.class);

  /**
   * Add assignment time to db
   * @param aId assignment id
   * @param assessmentTime assignment time
   */
  public void addAssignmentTime(int aId, AssessmentTime assessmentTime)  {
    String sql = "INSERT INTO ProgEdu.Assessment_Time(`aId`, `aaId`, `startTime`, `endTime`) VALUES(?, ?, ?, ?)";

    Connection conn = null;
    PreparedStatement preStmt = null;


    try {
      Timestamp startTime = new Timestamp(assessmentTime.getStartTime().getTime());
      Timestamp endTime = new Timestamp(assessmentTime.getEndTime().getTime());

      int actionId = aaDb.getAssessmentActionIdByAction(assessmentTime.getAssessmentActionEnum().toString());
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aId); //aId
      preStmt.setInt(2, actionId); //aaId
      preStmt.setTimestamp(3, startTime); //releaseTime
      preStmt.setTimestamp(4, endTime); //deadlineTime
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
  public List<AssessmentTime> getAssessmentTimeByName(String name) {
    String sql = "SELECT a_t.* FROM ProgEdu.Assessment_Time a_t join ProgEdu.Assignment a on a.id = a_t.aId where a.name = ?";

    List<AssessmentTime> assignmentTimeList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        AssessmentTime assignmentTime = new AssessmentTime();
        assignmentTime.setId(rs.getInt("id"));
        assignmentTime.setAId(rs.getInt("aId"));
        assignmentTime.setAssessmentActionEnum(aaDb.getAssessmentActionById(rs.getInt("aaId")));
        assignmentTime.setStartTime(rs.getTimestamp("startTime"));
        assignmentTime.setEndTime(rs.getTimestamp("endTime"));
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
   * get assignment review time by assignment name
   * @param aId assignment id
   * @return assignment review time
   */
  public List<AssessmentTime> getAssignmentReviewTimeNameById(int aId) {
    String sql = "SELECT * FROM ProgEdu.Assessment_Time WHERE `aId` = ? AND aaId = 3";

    List<AssessmentTime> assessmentTimes = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, aId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        AssessmentTime assessmentTime = new AssessmentTime();
        assessmentTime.setId(rs.getInt("id"));
        assessmentTime.setAId(rs.getInt("aId"));
        assessmentTime.setAssessmentActionEnum(aaDb.getAssessmentActionById(rs.getInt("aaId")));
        assessmentTime.setStartTime(rs.getTimestamp("startTime"));
        assessmentTime.setEndTime(rs.getTimestamp("endTime"));
        assessmentTimes.add(assessmentTime);
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assessmentTimes;
  }

  /**
   * get assignment time name by id
   * @param aId aid
   * @return assignment name
   */
  public List<AssessmentTime> getAssignmentTimeNameById(int aId) {
    String sql = "SELECT * FROM ProgEdu.Assessment_Time WHERE `aId` = ?";

    List<AssessmentTime> assessmentTimes = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setInt(1, aId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        AssessmentTime assessmentTime = new AssessmentTime();
        assessmentTime.setId(rs.getInt("id"));
        assessmentTime.setAId(rs.getInt("aId"));
        assessmentTime.setAssessmentActionEnum(aaDb.getAssessmentActionById(rs.getInt("aaId")));
        assessmentTime.setStartTime(rs.getTimestamp("startTime"));
        assessmentTime.setEndTime(rs.getTimestamp("endTime"));
        assessmentTimes.add(assessmentTime);
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assessmentTimes;
  }

  public AssessmentTime getAssignmentTimeByTimeAndName(String name, Date time) {
    String sql = "SELECT a_t.* FROM ProgEdu.Assessment_Time a_t join ProgEdu.Assignment a on a.id = a_t.aId WHERE " +
            "a.name = ? AND ( ? between a_t.startTime and a_t.endTime)";

    Timestamp dateStamp = new Timestamp(time.getTime());

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    AssessmentTime assessmentTime = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      preStmt.setTimestamp(2, dateStamp);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        assessmentTime = new AssessmentTime();
        assessmentTime.setAId(rs.getInt("aId"));
        assessmentTime.setAssessmentActionEnum(aaDb.getAssessmentActionById(rs.getInt("aaId")));
        assessmentTime.setStartTime(rs.getTimestamp("startTime"));
        assessmentTime.setEndTime(rs.getTimestamp("endTime"));
      }

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return assessmentTime;
  }

  public void editAssignmentTime(AssessmentTime assessmentTime) {
    String sql = "UPDATE Assessment_Time SET startTime = ? , endTime = ? WHERE id = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    Timestamp startTimestamp = new Timestamp(assessmentTime.getStartTime().getTime());
    Timestamp endTimestamp = new Timestamp(assessmentTime.getEndTime().getTime());

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setTimestamp(1, startTimestamp);
      preStmt.setTimestamp(2, endTimestamp);
      preStmt.setInt(3, assessmentTime.getId());
      preStmt.executeUpdate();

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Delete assignment_time from database by aId
   *
   */
  public void deleteAssignmentTimeByAid(int aId) {
    String sql = "DELETE FROM Assessment_Time WHERE aId = ?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

}
