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

import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class ProjectDbManager {
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

  private static ProjectDbManager dbManager = new ProjectDbManager();

  public static ProjectDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDbManager.class);

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

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, project.getName());
      preStmt.setTimestamp(2, createtimes);
      preStmt.setTimestamp(3, deadline);
      preStmt.setString(4, project.getDescription());
      preStmt.setInt(5, typeId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * get project info by project name
   *
   * @param id project id
   * @return project
   */
  public GroupProject getGroupProjectById(int id) {
    return getGroupProjectByName( getProjectName(id) );
  }

  /**
   * get project info by project name
   * 
   * @param projectName project name
   * @return project
   */
  public GroupProject getGroupProjectByName(String projectName) {
    return getGroupProject( getId(projectName) );
  }

  /**
   * get project info by project id
   * 
   * @param id project id
   * @return project
   */
  public GroupProject getGroupProject(int id) {
    GroupProject project = new GroupProject();
    String sql = "SELECT * FROM Project WHERE id = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);
      stmt.setInt(1, id);
      rs = stmt.executeQuery();

      while (rs.next()) {

        String name = rs.getString("name");

        Date createTime = rs.getTimestamp("createTime");
        Date deadline = rs.getTimestamp("deadline");
        Date releaseTime = rs.getTimestamp("releaseTime");

        String description = rs.getString("description");
        int typeId = rs.getInt("type");
        ProjectTypeEnum typeEnum = atDb.getTypeNameById(typeId);

        project.setId(id);
        project.setName(name);
        project.setCreateTime(createTime);
        project.setDescription(description);
        project.setType(typeEnum);
        project.setDeadline(deadline);
        project.setReleaseTime(releaseTime);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      project = new GroupProject();
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return project;
  }

  /**
   * get project name by project id
   * 
   * @param pid project id
   * @return assignment name
   */
  public String getProjectName(int pid) {
    String sql = "SELECT name FROM Project WHERE id = ?";
    String projectName = "";

    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setInt(1, pid);
      try (ResultSet rs = stmt.executeQuery();) {
        while (rs.next()) {
          projectName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(stmt, conn);
    }
    return projectName;
  }

  /**
   * get project id by project name
   * 
   * @param projectName project name
   * @return id project id
   */
  public int getId(String projectName) {
    String query = "SELECT id FROM Project WHERE name = ?";
    int id = -1;

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, projectName);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
    return id;
  }

  /**
   * get project type by project name
   * 
   * @param projectName project name
   * @return type project type
   */
  public int getProjectType(String projectName) {
    int typeId = 0;
    String sql = "SELECT type FROM Project WHERE name=?";

    Connection conn = null;
    PreparedStatement preStmt = null;


    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setString(1, projectName);
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
   * list all project names;
   * 
   * @return all project names
   */
  public List<String> listAllProjectNames() {
    List<String> lsNames = new ArrayList<>();
    String sql = "SELECT name FROM Project";

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
   * Delete project
   * 
   * @param id project id
   */
  public void deleteProject(int id) {
    String sql = "DELETE FROM Project WHERE id=?";

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

}
