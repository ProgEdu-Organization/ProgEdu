package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.project.ProjectTypeEnum;

public class AssignmentDbManager {
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

  private static AssignmentDbManager dbManager = new AssignmentDbManager();

  public static AssignmentDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentDbManager() {

  }

  /**
   * Add assignment to database
   * 
   * @param assignment
   *          Project
   */
  public void addAssignment(Assignment assignment) {
    String sql = "INSERT INTO Assignment(name, createTime, deadline, description, hasTemplate"
        + ", type, zipChecksum, zipUrl, releaseTime, display)  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?)";
    int typeId = atDb.getTypeIdByName(assignment.getType().getTypeName());

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, assignment.getName());
      preStmt.setString(2, assignment.getCreateTime());
      preStmt.setString(3, assignment.getDeadline());
      preStmt.setString(4, assignment.getDescription());
      preStmt.setBoolean(5, assignment.isHasTemplate());
      preStmt.setInt(6, typeId);
      preStmt.setLong(7, assignment.getTestZipChecksum());
      preStmt.setString(8, assignment.getTestZipUrl());
      preStmt.setString(9, assignment.getReleaseTime());
      preStmt.setBoolean(10, assignment.isDisplay());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get assignment info by assignment name
   * 
   * @param name
   *          assignment name
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
          String createTime = rs.getString("createTime");
          String deadline = rs.getString("deadline").replace("T", " ");
          String description = rs.getString("description");
          boolean hasTemplate = rs.getBoolean("hasTemplate");
          int typeId = rs.getInt("type");
          ProjectTypeEnum typeEnum = atDb.getTypeNameById(typeId);
          long checksum = rs.getInt("zipChecksum");
          String zipUrl = rs.getString("zipUrl");
          String releaseTime = rs.getString("releaseTime");
          boolean display = rs.getBoolean("display");

          assignment.setName(name);
          assignment.setCreateTime(createTime);
          assignment.setDescription(description);
          assignment.setHasTemplate(hasTemplate);
          assignment.setType(typeEnum);
          assignment.setDeadline(deadline);
          assignment.setTestZipChecksum(checksum);
          assignment.setTestZipUrl(zipUrl);
          assignment.setReleaseTime(releaseTime);
          assignment.setDisplay(display);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return assignment;
  }

  /**
   * get assignment name by assignment id
   * 
   * @param aid
   *          assignment id
   * @return assignment name
   */
  public String getAssignmentNameById(int aid) {
    String sql = "SELECT * FROM Assignment WHERE aId = ?";
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
      e.printStackTrace();
    }
    return assignmentName;
  }

  /**
   * assignment name to find assignmentId in db
   * 
   * @param assignmentName
   *          assignment name
   * @return id
   */
  public int getAssignmentIdByName(String assignmentName) {
    String query = "SELECT * FROM Assignment WHERE name = ?";
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
      e.printStackTrace();
    }
    return id;
  }

  /**
   * get assignment type by name
   * 
   * @param name
   *          assignment name
   * @return type assignment type
   */
  public int getAssignmentType(String name) {
    int typeId = 0;
    String sql = "SELECT * FROM Assignment WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          typeId = rs.getInt("type");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typeId;
  }

  /**
   * list all assignment names;
   * 
   * @return all names
   */
  public List<String> listAllAssignmentNames() {
    List<String> lsNames = new ArrayList<>();
    String sql = "SELECT * FROM Assignment";

    try (Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        String name = rs.getString("name");
        lsNames.add(name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsNames;
  }

  /**
   * Delete assignment from database
   * 
   * @param name
   *          assignment name
   */
  public void deleteAssignment(String name) {
    String sql = "DELETE FROM Assignment WHERE name='" + name + "'";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
