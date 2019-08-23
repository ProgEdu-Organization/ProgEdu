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

import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.project.ProjectTypeEnum;

public class ProjectDbManager {
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

  private static ProjectDbManager dbManager = new ProjectDbManager();

  public static ProjectDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private ProjectDbManager() {

  }

  /**
   * Add project to database
   * 
   * @param project Project
   */
  public void addProject(GroupProject project) {
    String sql = "INSERT INTO Project(name, createTime, deadline, description,"
        + " type)  VALUES(?, ?, ?, ?, ?)";
    int typeId = atDb.getTypeIdByName(project.getType().getTypeName());
    Timestamp createtimes = new Timestamp(project.getCreateTime().getTime());
    Timestamp deadline = new Timestamp(project.getDeadline().getTime());

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, project.getName());
      preStmt.setTimestamp(2, createtimes);
      preStmt.setTimestamp(3, deadline);
      preStmt.setString(4, project.getDescription());
      preStmt.setInt(5, typeId);
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
  public GroupProject getGroupProjectByName(String name) {
    GroupProject project = new GroupProject();
    String sql = "SELECT * FROM Project WHERE name = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          Date createTime = rs.getTimestamp("createTime");
          Date deadline = rs.getTimestamp("deadline");
          String description = rs.getString("description");
          int typeId = rs.getInt("type");
          ProjectTypeEnum typeEnum = atDb.getTypeNameById(typeId);

          project.setName(name);
          project.setCreateTime(createTime);
          project.setDescription(description);
          project.setType(typeEnum);
          project.setDeadline(deadline);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return project;
  }

  /**
   * get project name by project id
   * 
   * @param pid project id
   * @return assignment name
   */
  public String getProjectNameById(int pid) {
    String sql = "SELECT name FROM Project WHERE pId = ?";
    String projectName = "";
    try (Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, pid);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          projectName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return projectName;
  }

  /**
   * get project id by project name
   * 
   * @param projectName project name
   * @return id project id
   */
  public int getProjectIdByName(String projectName) {
    String query = "SELECT id FROM Project WHERE name = ?";
    int id = -1;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, projectName);
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
   * get project type by project name
   * 
   * @param projectName project name
   * @return type project type
   */
  public int getAssignmentType(String projectName) {
    int typeId = 0;
    String sql = "SELECT type FROM Project WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, projectName);
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
   * list all project names;
   * 
   * @return all project names
   */
  public List<String> listAllProjectNames() {
    List<String> lsNames = new ArrayList<>();
    String sql = "SELECT name FROM Project";

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
    String sql = "DELETE FROM Project WHERE name='" + name + "'";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
