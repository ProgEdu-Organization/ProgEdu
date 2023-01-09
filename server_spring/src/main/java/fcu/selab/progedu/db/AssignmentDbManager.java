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

import fcu.selab.progedu.data.AssessmentTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentDbManager {
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();
  AssessmentTimeDbManager assessmentTimeDbManager = AssessmentTimeDbManager.getInstance();

  private static AssignmentDbManager dbManager = new AssignmentDbManager();

  public static AssignmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentDbManager.class);

  private AssignmentDbManager() {

  }

  /**
   * Add assignment to database
   * 
   * @param assignment Project
   */
  public void addAssignment(Assignment assignment) {
    String sql = "INSERT INTO Assignment(`name`, `createTime`, `description`,"
        + " `type`, `display`)"
        + " VALUES(?, ?, ?, ?, ?)";
    // Todo above [hasTemplate, zipChecksum, zipUrl] is not need

    int typeId = atDb.getTypeIdByName(assignment.getType().getTypeName());
    Timestamp createTime = new Timestamp(assignment.getCreateTime().getTime());
    /*
    Timestamp deadlineTime = new Timestamp(assignment.getDeadline().getTime());
    Timestamp releaseTime = new Timestamp(assignment.getReleaseTime().getTime());
    */
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, assignment.getName());
      preStmt.setTimestamp(2, createTime);
      preStmt.setString(3, assignment.getDescription());
      preStmt.setInt(4, typeId);
      preStmt.setBoolean(5, assignment.isDisplay());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Add assignment to database and get auto increase id
   *
   * @param assignment Project
   */
  public int addAssignmentAndGetId(Assignment assignment) {
    String sql = "INSERT INTO Assignment(`name`, `createTime`, `description`,"
            + " `type`, `display`)"
            + " VALUES(?, ?, ?, ?, ?)";

    String sqlGetId = "SELECT LAST_INSERT_ID() as id";
    // Todo above [hasTemplate, zipChecksum, zipUrl] is not need

    int id = 0;
    int typeId = atDb.getTypeIdByName(assignment.getType().getTypeName());
    Timestamp createTime = new Timestamp(assignment.getCreateTime().getTime());
    /*
    Timestamp deadlineTime = new Timestamp(assignment.getDeadline().getTime());
    Timestamp releaseTime = new Timestamp(assignment.getReleaseTime().getTime());
    */
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, assignment.getName());
      preStmt.setTimestamp(2, createTime);
      preStmt.setString(3, assignment.getDescription());
      preStmt.setInt(4, typeId);
      preStmt.setBoolean(5, assignment.isDisplay());
      preStmt.executeUpdate();

      preStmt = conn.prepareStatement(sqlGetId);
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
   * get assignment info by assignment name
   * 
   * @param name assignment name
   * @return assignment
   */
  public Assignment getAssignmentByName(String name) {
    Assignment assignment = new Assignment();
    String sql = "SELECT * FROM Assignment WHERE name = ?";

    Connection conn = null;
    PreparedStatement stmt = null;


    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          assignment.setId(rs.getInt("id"));
          assignment.setName(name);
          assignment.setCreateTime(rs.getTimestamp("createTime"));
          assignment.setDescription(rs.getString("description"));
          assignment.setType(atDb.getTypeNameById(rs.getInt("type")));
          assignment.setDisplay(rs.getBoolean("display"));
          assignment.setAssessmentTimeList(assessmentTimeDbManager.getAssessmentTimeByName(name));
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(stmt, conn);
    }
    return assignment;
  }

  /**
   * get assignment name by assignment id
   * 
   * @param aid assignment id
   * @return assignment name
   */
  public String getAssignmentNameById(int aid) {
    String sql = "SELECT name FROM Assignment WHERE id = ?";
    String assignmentName = "";

    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setInt(1, aid);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          assignmentName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(stmt, conn);
    }
    return assignmentName;
  }

  /**
   * assignment name to find assignmentId in db
   * 
   * @param assignmentName assignment name
   * @return id
   */
  public int getAssignmentIdByName(String assignmentName) {
    String query = "SELECT id FROM Assignment WHERE name = ?";
    int id = -1;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setString(1, assignmentName);
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
   * get assignment type by name
   * 
   * @param name assignment name
   * @return type assignment type
   */
  public ProjectTypeEnum getAssignmentType(String name) {
    int typeId = getAssignmentTypeId(name);
    return atDb.getTypeNameById(typeId);
  }

  /**
   * get assignment type id by name
   * 
   * @param name assignment name
   * @return type assignment type
   */
  public int getAssignmentTypeId(String name) {
    int typeId = 0;
    String sql = "SELECT type FROM Assignment WHERE name=?";

    Connection conn = null;
    PreparedStatement preStmt = null;


    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          typeId = rs.getInt("type");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
    return typeId;
  }

  /**
   * get test file Url by assignment name
   * 
   * @param assignmentName assignment name
   * @return zipUrl
   */
  public String getTestFileUrl(String assignmentName) {
    String sql = "SELECT zipUrl FROM Assignment WHERE name = ?";
    String zipUrl = "";

    Connection conn = null;
    PreparedStatement stmt = null;


    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setString(1, assignmentName);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          zipUrl = rs.getString("zipUrl");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(stmt, conn);
    }
    return zipUrl;
  }

  /**
   * list all assignment names;
   * 
   * @return all names
   */
  public List<Assignment> getAllAssignment() {
    List<Assignment> assignments = new ArrayList<>();
    String sql = "SELECT id, name, createTime, display, description, type "
            + "FROM Assignment;";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);

      while (rs.next()) {
        if(!atDb.getTypeNameById(rs.getInt("type")).equals(ProjectTypeEnum.EXAM)) {
          Assignment assignment = new Assignment();
          assignment.setId(rs.getInt("id"));
          assignment.setName(rs.getString("name"));
          assignment.setCreateTime(rs.getTimestamp("createTime"));
          assignment.setDisplay(rs.getBoolean("display"));
          assignment.setDescription(rs.getString("description"));
          assignment.setType(atDb.getTypeNameById(rs.getInt("type")));
          List<AssessmentTime> assessmentTimes = assessmentTimeDbManager.getAssignmentTimeNameById(assignment.getId());
          assignment.setAssessmentTimeList(assessmentTimes);
          assignments.add(assignment);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return assignments;
  }

  /**
   * get all peer review assignment form assignment
   *
   */
  public List<Assignment> getAllReviewAssignment() throws SQLException { // Todo 應該不用丟出錯誤
    String query = "SELECT assign.id, assign.name, assign.createTime, "
        + "assign.display, assign.description "
        + "FROM Assignment AS assign, Review_Setting AS review "
        + "WHERE assign.id = review.aId ORDER BY assign.id";
    List<Assignment> assignmentList = new ArrayList<>();


    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;


    try {

      conn = database.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);

      while (rs.next()) {
        Assignment assignment = new Assignment();
        assignment.setId(rs.getInt("id"));
        assignment.setName(rs.getString("name"));
        assignment.setCreateTime(rs.getTimestamp("createTime"));
        assignment.setDisplay(rs.getBoolean("display"));
        assignment.setDescription(rs.getString("description"));
        List<AssessmentTime> assessmentTimes = assessmentTimeDbManager.getAssignmentTimeNameById(assignment.getId());
        assignment.setAssessmentTimeList(assessmentTimes);
        assignmentList.add(assignment);
      }
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return assignmentList;
  }

  /**
   * get auto assessment which is not assign as peer review
   */
  public List<Assignment> getAutoAssessment() throws SQLException {
    String query = "SELECT id, name, createTime, display, description, type "
        + "FROM Assignment WHERE id NOT IN (SELECT aId FROM Review_Setting);";
    List<Assignment> assignmentList = new ArrayList<>();

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);

      while (rs.next()) {
        if(!atDb.getTypeNameById(rs.getInt("type")).equals(ProjectTypeEnum.EXAM)) {
          Assignment assignment = new Assignment();
          assignment.setId(rs.getInt("id"));
          assignment.setName(rs.getString("name"));
          assignment.setCreateTime(rs.getTimestamp("createTime"));
          assignment.setDisplay(rs.getBoolean("display"));
          assignment.setDescription(rs.getString("description"));
          assignment.setType(atDb.getTypeNameById(rs.getInt("type")));
          assignment.setAssessmentTimeList(assessmentTimeDbManager.getAssignmentTimeNameById(assignment.getId()));
          assignmentList.add(assignment);
        }
      }
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return assignmentList;
  }


  /**
   * list all assignment names;
   * 
   * @return all names
   */
  public List<String> getAllAssignmentNames() {
    List<String> lsNames = new ArrayList<>();
    String sql = "SELECT name FROM Assignment";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String name = rs.getString("name");
        lsNames.add(name);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return lsNames;
  }

  /**
   * Delete assignment from database
   * 
   * @param name assignment name
   */
  public void deleteAssignment(String name) {
    String sql = "DELETE FROM Assignment WHERE name = ?";

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

  /**
   * edit assignment to database
   * 
   * @param deadline    deadline
   * @param releaseTime releaseTime
   * @param readMe      readMe
   * @param checksum    checksum
   * @param id          id
   */
  public void editAssignment(Date deadline, Date releaseTime, String readMe, long checksum,
      int id) {
    /*
    Timestamp deadlinetime = new Timestamp(deadline.getTime());
    Timestamp releasetime = new Timestamp(releaseTime.getTime());
    */
    String sql = "UPDATE Assignment SET description = ?,"
        + "zipChecksum = ? WHERE id = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      /*
      preStmt.setTimestamp(1, deadlinetime);
      preStmt.setTimestamp(2, releasetime);
      */
      preStmt.setString(1, readMe);
      preStmt.setLong(2, checksum);
      preStmt.setInt(3, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * edit assignment
   *
   * @param readMe      readMe
   * @param id          id
   */
  public void editAssignment(String readMe, int id) {
    /*
    Timestamp deadlinetime = new Timestamp(deadline.getTime());
    Timestamp releasetime = new Timestamp(releaseTime.getTime());
    */
    String sql = "UPDATE Assignment SET "
        + "description = ? WHERE id = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);
      preStmt.setString(1, readMe);
      preStmt.setInt(2, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
