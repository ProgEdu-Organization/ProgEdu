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
    String sql = "INSERT INTO Group(gitLabId, name, leaderId) " + "VALUES(?, ?, ?)";

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
    String statement = "SELECT id FROM Group WHERE name = ?";

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
    String statement = "SELECT gitLabId FROM Group WHERE name = ?";

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
   * get all groups
   * 
   * @return all group on gitlab
   */

  public List<Group> getGroups() {
    String statement = "SELECT * FROM Group";
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
   * add one or more member into a group
   * 
   * @param groupName the group name
   * @param members   the members will be inserted
   */
  public boolean addGroupMember(String groupName, List<String> members) {
    String sql = "INSERT INTO Team(name, sId, isLeader) VALUES(?, ?, ?)";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      for (String sid : members) {
        int id = udb.getUser(sid).getId();
        preStmt.setString(1, groupName);
        preStmt.setInt(2, id);
        preStmt.setInt(3, 0);
        preStmt.executeUpdate();
      }
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }
}
