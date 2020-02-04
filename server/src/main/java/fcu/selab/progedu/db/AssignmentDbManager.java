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
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentDbManager {
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

  private static AssignmentDbManager dbManager = new AssignmentDbManager();

  public static AssignmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentDbManager.class);

  private AssignmentDbManager() {

  }

  /**
   * Add assignment to database
   * 
   * @param assignment Project
   */
  public void addAssignment(Assignment assignment) {
    String sql = "INSERT INTO Assignment(name, createTime, deadline, description, hasTemplate"
        + ", type, zipChecksum, zipUrl, releaseTime, display)  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?)";
    int typeId = atDb.getTypeIdByName(assignment.getType().getTypeName());
    Timestamp createtimes = new Timestamp(assignment.getCreateTime().getTime());
    Timestamp deadlinetimes = new Timestamp(assignment.getDeadline().getTime());
    Timestamp releasetimes = new Timestamp(assignment.getReleaseTime().getTime());

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, assignment.getName());
      preStmt.setTimestamp(2, createtimes);
      preStmt.setTimestamp(3, deadlinetimes);
      preStmt.setString(4, assignment.getDescription());
      preStmt.setBoolean(5, assignment.isHasTemplate());
      preStmt.setInt(6, typeId);
      preStmt.setLong(7, assignment.getTestZipChecksum());
      preStmt.setString(8, assignment.getTestZipUrl());
      preStmt.setTimestamp(9, releasetimes);
      preStmt.setBoolean(10, assignment.isDisplay());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
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

    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          assignment.setName(name);
          assignment.setCreateTime(rs.getTimestamp("createTime"));
          assignment.setDeadline( rs.getTimestamp("deadline"));
          assignment.setDescription(rs.getString("description"));
          assignment.setHasTemplate(rs.getBoolean("hasTemplate"));
          assignment.setType(atDb.getTypeNameById(rs.getInt("type")));
          assignment.setTestZipChecksum(rs.getLong("zipChecksum"));
          assignment.setTestZipUrl(rs.getString("zipUrl"));
          assignment.setReleaseTime(rs.getTimestamp("releaseTime"));
          assignment.setDisplay(rs.getBoolean("display"));
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, aid);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          assignmentName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, assignmentName);
      try (ResultSet rs = preStmt.executeQuery();) {
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
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          typeId = rs.getInt("type");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, assignmentName);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          zipUrl = rs.getString("zipUrl");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
    String sql = "SELECT id, name, createTime, deadline, releaseTime, display, description "
            + "FROM Assignment;";
    try (Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Assignment assignment = new Assignment();
        assignment.setId(rs.getInt("id"));
        assignment.setName(rs.getString("name"));
        assignment.setCreateTime(rs.getTimestamp("createTime"));
        assignment.setDeadline(rs.getTimestamp("deadline"));
        assignment.setReleaseTime(rs.getTimestamp("releaseTime"));
        assignment.setDisplay(rs.getBoolean("display"));
        assignment.setDescription(rs.getString("description"));
        assignments.add(assignment);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return assignments;
  }

  /**
   * list all assignment names;
   * 
   * @return all names
   */
  public List<String> getAllAssignmentNames() {
    List<String> lsNames = new ArrayList<>();
    String sql = "SELECT name FROM Assignment";

    try (Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        String name = rs.getString("name");
        lsNames.add(name);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, name);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
    Timestamp deadlinetime = new Timestamp(deadline.getTime());
    Timestamp releasetime = new Timestamp(releaseTime.getTime());
    String sql = "UPDATE Assignment SET deadline = ?, releaseTime = ?, description = ?,"
        + "zipChecksum = ? WHERE id = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setTimestamp(1, deadlinetime);
      preStmt.setTimestamp(2, releasetime);
      preStmt.setString(3, readMe);
      preStmt.setLong(4, checksum);
      preStmt.setInt(5, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * edit assignment
   * 
   * @param deadline    deadline
   * @param releaseTime releaseTime
   * @param readMe      readMe
   * @param id          id
   */
  public void editAssignment(Date deadline, Date releaseTime, String readMe, int id) {
    Timestamp deadlinetime = new Timestamp(deadline.getTime());
    Timestamp releasetime = new Timestamp(releaseTime.getTime());
    String sql = "UPDATE Assignment SET deadline = ?, releaseTime = ?, "
        + "description = ? WHERE id = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setTimestamp(1, deadlinetime);
      preStmt.setTimestamp(2, releasetime);
      preStmt.setString(3, readMe);
      preStmt.setInt(4, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

}
