package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.data.User;

public class GroupDbManager {

  private static GroupDbManager dbManager = new GroupDbManager();

  public static GroupDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupDbManager.class);

  private UserDbManager udb = UserDbManager.getInstance();

  private GroupDbManager() {
  }

  /**
   * add groupinto db
   *
   * @param gitlabId  gitlabId
   * @param groupName groupName
   * @param leaderId  leaderId
   */
  public void addGroup(int gitlabId, String groupName, int leaderId) {
    String sql = "INSERT INTO ProgEdu.Group(gitLabId, name, leader) " + "VALUES(?, ?, ?)";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gitlabId);
      preStmt.setString(2, groupName);
      preStmt.setInt(3, leaderId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get id
   *
   * @param name name
   */
  public int getId(String name) {
    int id = -1;
    String statement = "SELECT id FROM ProgEdu.Group WHERE name = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        if (rs.next()) {
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
   * get group name
   *
   * @param id group id
   */
  public String getName(int id) {
    String groupName = "";
    String statement = "SELECT name FROM ProgEdu.Group WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        if (rs.next()) {
          groupName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return groupName;
  }

  /**
   * get gitlab id
   *
   * @param name name
   */
  public int getGitlabId(String name) {
    int id = -1;
    String statement = "SELECT gitLabId FROM ProgEdu.Group WHERE name = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        if (rs.next()) {
          id = rs.getInt("gitLabId");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return id;
  }

  /**
   * get gitlab id
   *
   * @param name name
   */
  public int getLeader(String name) {
    int id = -1;
    String statement = "SELECT leader FROM ProgEdu.Group WHERE name = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        if (rs.next()) {
          id = rs.getInt("leader");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return id;
  }

  /**
   * update leader
   *
   * @param id     group id
   * @param leader leader uid
   */
  public void updateLeader(int id, int leader) {
    String sql = "UPDATE ProgEdu.Group SET leader = ? WHERE id = ?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, leader);
      preStmt.setInt(2, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get all groups
   *
   * @return all group on gitlab
   */
  public List<Group> getGroups() {
    String statement = "SELECT name FROM ProgEdu.Group";
    List<Group> groups = new ArrayList<>();
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          String name = rs.getString("name");
          Group group = getGroup(name);
          groups.add(group);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return groups;
  }

  /**
   * get group info by group name
   *
   * @param name group name
   * @return group info
   */
  public Group getGroup(String name) {
    String statement = "SELECT * FROM ProgEdu.Group WHERE name=?";
    Group group = new Group();
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(statement)) {
      preStmt.setString(1, name);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          group.setGroupName(rs.getString("name"));
          group.setId(rs.getInt("id"));
          group.setGitlabId(rs.getInt("gitLabId"));
          group.setLeader(Integer.parseInt(rs.getString("leader")));
        }

        // setMembers
        List<User> members = new ArrayList<>();
        List<Integer> userIds = GroupUserDbManager.getInstance().getUids(group.getId());
        for (int userId : userIds) {
          members.add(UserDbManager.getInstance().getUser(userId));
        }
        group.setMembers(members);

        // setProjects
        List<GroupProject> groupProjectList = new ArrayList<>();
        List<Integer> projectIds = ProjectGroupDbManager.getInstance().getPids(group.getId());
        for (int projectId : projectIds) {
          groupProjectList.add(ProjectDbManager.getInstance().getGroupProjectById(projectId));
        }
        group.setProjects(groupProjectList);

      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return group;
  }

  /**
   * get group info by id
   *
   * @param id group id
   * @return group info
   */
  public Group getGroup(int id) {
    return getGroup(getName(id));
  }

  /**
   * remove Group by gid
   *
   * @param id group id
   */
  public void remove(int id) {
    String sql = "DELETE FROM ProgEdu.Group WHERE id=?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}
