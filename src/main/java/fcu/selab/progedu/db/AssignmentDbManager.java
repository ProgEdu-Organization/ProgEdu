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

  UserDbManager udb = UserDbManager.getInstance();

  /**
   * Add project to database
   * 
   * @param project Project
   */
  public void addProject(Assignment project) {
    String sql = "INSERT INTO Assignment(name, createTime, deadline, description, hasTemplate"
        + ", typeId, zipChecksum, zipUrl, releaseTime, display)  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,"
        + "?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, project.getName());
      preStmt.setString(2, project.getCreateTime());
      preStmt.setString(3, project.getDeadline());
      preStmt.setString(4, project.getDescription());
      preStmt.setBoolean(5, project.isHasTemplate());
      preStmt.setInt(6, project.getType());
      preStmt.setString(7, project.getTestZipChecksum());
      preStmt.setString(8, project.getTestZipUrl());
      preStmt.setString(9, project.getReleaseTime());
      preStmt.setBoolean(10, project.isDisplay());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get project info by project name
   * 
   * @param name project name
   * @return project
   */
  public Assignment getProjectByName(String name) {
    Assignment project = new Assignment();
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

          project.setName(name);
          project.setCreateTime(createTime);
          project.setDescription(description);
          project.setHasTemplate(hasTemplate);
          project.setType(type);
          project.setDeadline(deadline);
          project.setTestZipChecksum(checksum);
          project.setTestZipUrl(zipUrl);
          project.setReleaseTime(releaseTime);
          project.setDisplay(display);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return project;
  }

  /**
   * List all the projects
   * 
   * @return List of projects
   */
  public List<Assignment> listAllProjects() {
    List<Assignment> lsProjects = new ArrayList<>();
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

        Assignment project = new Assignment();
        project.setName(name);
        project.setDescription(description);
        project.setHasTemplate(hasTemplate);
        project.setType(type);
        project.setTestZipChecksum(checksum);
        project.setTestZipUrl(zipUrl);
        project.setCreateTime(createTime);
        project.setDeadline(deadline);
        project.setReleaseTime(releaseTime);
        project.setDisplay(display);

        lsProjects.add(project);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsProjects;
  }

  /**
   * list all project names;
   * 
   * @return all names
   */
  public List<String> listAllProjectNames() {
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
   * Delete project from database
   * 
   * @param name project name
   */
  public void deleteProject(String name) {
    String sql = "DELETE FROM Assignment WHERE name='" + name + "'";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Edit project from database
   * 
   * @param deadline new deadline
   * @param readMe   new readMe
   * @param name     project name
   */
  public void editProject(String deadline, String readMe, String releaseTime, String name) {
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
   * Edit project checksum
   * 
   * @param name     project name
   * @param checksum new checksum
   */
  public void updateProjectChecksum(String name, String checksum) {
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
   * @param name assignment name
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
          typeId = rs.getInt("typeId");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typeId;
  }

  /**
   * get assignment type Id
   * 
   * @param name Assignment_Type name
   * @return type assignment type
   */
  public int getAssignmentTypeId(String name) {
    int id = 0;
    String sql = "SELECT * FROM Assignment_Type WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
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
   * get assignment type name
   * 
   * @param id Assignment_Type id
   * @return name assignment name
   */
  public String getAssignmentTypeName(int id) {
    String typename = null;
    String sql = "SELECT * FROM Assignment_Type WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          typename = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typename;
  }
}
