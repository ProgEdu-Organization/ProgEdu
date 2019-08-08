package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.Assignment;

public class AssignmentDbManager {

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

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, assignment.getName());
      preStmt.setString(2, assignment.getCreateTime());
      preStmt.setString(3, assignment.getDeadline());
      preStmt.setString(4, assignment.getDescription());
      preStmt.setBoolean(5, assignment.isHasTemplate());
      preStmt.setInt(6, assignment.getType());
      preStmt.setString(7, assignment.getTestZipChecksum());
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
          int type = rs.getInt("type");
          String checksum = rs.getString("zipChecksum");
          String zipUrl = rs.getString("zipUrl");
          String releaseTime = rs.getString("releaseTime");
          boolean display = rs.getBoolean("display");

          assignment.setName(name);
          assignment.setCreateTime(createTime);
          assignment.setDescription(description);
          assignment.setHasTemplate(hasTemplate);
          assignment.setType(type);
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
   * @param aId
   *          assignment id
   * @return assignment name
   */
  public String getAssignmentNameById(int aId) {
    String sql = "SELECT * FROM Assignment WHERE aId = ?";
    String assignmentName = "";
    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, aId);
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
   * List all the assignments
   * 
   * @return List of assignments
   */
  public List<Assignment> listAllAssignments() {
    List<Assignment> lsAssignments = new ArrayList<>();
    String sql = "SELECT * FROM Assignment";

    try (Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        String name = rs.getString("name");
        String description = rs.getString("description");
        boolean hasTemplate = rs.getBoolean("hasTemplate");
        int type = rs.getInt("type");
        String checksum = rs.getString("zipChecksum");
        String zipUrl = rs.getString("zipUrl");
        String createTime = rs.getString("createTime");
        String deadline = rs.getString("deadline");
        String releaseTime = rs.getString("releaseTime");
        boolean display = rs.getBoolean("display");

        Assignment assignment = new Assignment();
        assignment.setName(name);
        assignment.setDescription(description);
        assignment.setHasTemplate(hasTemplate);
        assignment.setType(type);
        assignment.setTestZipChecksum(checksum);
        assignment.setTestZipUrl(zipUrl);
        assignment.setCreateTime(createTime);
        assignment.setDeadline(deadline);
        assignment.setReleaseTime(releaseTime);
        assignment.setDisplay(display);

        lsAssignments.add(assignment);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsAssignments;
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

  /**
   * Edit assignment from database
   * 
   * @param deadline
   *          new deadline
   * @param readMe
   *          new readMe
   * @param name
   *          assignment name
   */
  public void editAssignment(String deadline, String readMe, String releaseTime, String name) {
    String sql = "UPDATE Assignment SET deadline=?, description=?, releaseTime=? WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, deadline);
      preStmt.setString(2, readMe);
      preStmt.setString(3, releaseTime);
      preStmt.setString(4, name);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Edit assignment checksum
   * 
   * @param name
   *          assignment name
   * @param checksum
   *          new checksum
   */
  public void updateAssignmentChecksum(String name, String checksum) {
    String sql = "UPDATE Assignment SET zipChecksum=? WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, checksum);
      preStmt.setString(2, name);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
   * Get assignmentdisplay from database
   *
   * @param assignmentname The gitlab user id
   * @return display
   */
  public boolean getAssignmentDisplay(String assignmentname) {
    String query = "SELECT * FROM Assignment WHERE name = ?";
    boolean display = true;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, assignmentname);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          display = rs.getBoolean("display");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return display;
  }

  /**
   * Set user display in database
   *
   * @param assignmentname The gitlab user id
   */
  public void setAssignmentDisplay(String assignmentname) {
    String query = "UPDATE Assignment SET display= ? WHERE name = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setBoolean(1, false);
      preStmt.setString(2, assignmentname);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
