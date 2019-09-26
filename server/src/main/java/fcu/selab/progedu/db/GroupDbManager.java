package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.Group;

public class GroupDbManager {

  private static GroupDbManager dbManager = new GroupDbManager();

  public static GroupDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private GroupDbManager() {

  }

  private UserDbManager udb = UserDbManager.getInstance();

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
      e.printStackTrace();
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
      e.printStackTrace();
    }
    return id;
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
      e.printStackTrace();
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
      e.printStackTrace();
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
      e.printStackTrace();
    }
  }

  /**
   * get all groups
   * 
   * @return all group on gitlab
   */

  public List<Group> getGroups() {
    String statement = "SELECT * FROM ProgEdu.Group";
    List<Group> groups = new ArrayList<>();
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(statement)) {
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          String name = rs.getString("name");
          int id = rs.getInt("id");
          int gitlabId = rs.getInt("gitLabId");
          int leader = rs.getInt("leader");

          Group group = new Group();
          group.setGitlabId(gitlabId);
          group.setGroupName(name);
          group.setId(id);
          group.setLeader(leader);

          groups.add(group);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return groups;
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
      e.printStackTrace();
    }
  }

}
