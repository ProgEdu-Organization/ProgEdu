package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
   * get all groups
   * 
   * @return all group on gitlab
   */

  public List<Group> listGroups() {
    List<Group> lsGroups = new ArrayList<>();
    List<String> members = new ArrayList<>();
    Group group = new Group();
    String groupName = "";
    String sql = "SELECT * FROM Team";

    try (Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        if (groupName.equals(rs.getString("name"))) {
          boolean isLeader = rs.getBoolean("isLeader");

          if (isLeader) {
            int leaderId = rs.getInt("sId");
            group.setLeaderUsername(udb.getUsername(leaderId));
          } else {
            int memberId = rs.getInt("sId");
            members.add(udb.getUsername(memberId));
            group.setContributors(members);
          }
        } else {
          group = new Group();
          members = new ArrayList<>();

          groupName = rs.getString("name");
          group.setGroupName(groupName);
          boolean isLeader = rs.getBoolean("isLeader");
          if (isLeader) {
            int leaderId = rs.getInt("sId");
            group.setLeaderUsername(udb.getUsername(leaderId));
          } else {
            int memberId = rs.getInt("sId");
            members.add(udb.getUsername(memberId));
            group.setContributors(members);
          }
          lsGroups.add(group);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsGroups;
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
