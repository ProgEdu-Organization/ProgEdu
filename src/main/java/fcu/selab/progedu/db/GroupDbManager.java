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
   * add group member into db
   * 
   * @param groupName group name
   * @param username  username
   * @param isLeader  whether current member is leader or not
   */
  public void addGroup(String groupName, String username, boolean isLeader) {
    String sql = "INSERT INTO Team(name, sId, isLeader) " + "VALUES(?, ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      int id = -1;
      id = udb.getUserIdByUsername(username);
      preStmt.setString(1, groupName);
      preStmt.setInt(2, id);
      preStmt.setBoolean(3, isLeader);
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
            group.setLeaderUsername(udb.getName(leaderId));
          } else {
            int memberId = rs.getInt("sId");
            members.add(udb.getName(memberId));
            group.setContributor(members);
          }
        } else {
          group = new Group();
          members = new ArrayList<>();

          groupName = rs.getString("name");
          group.setGroupName(groupName);
          boolean isLeader = rs.getBoolean("isLeader");
          if (isLeader) {
            int leaderId = rs.getInt("sId");
            group.setLeaderUsername(udb.getName(leaderId));
          } else {
            int memberId = rs.getInt("sId");
            members.add(udb.getName(memberId));
            group.setContributor(members);
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
