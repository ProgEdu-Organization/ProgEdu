package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.service.RoleEnum;

public class RoleUserDbManager {
  private static RoleUserDbManager dbManager = new RoleUserDbManager();

  public static RoleUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();
  RoleDbManager rdb = RoleDbManager.getInstance();
  UserDbManager udb = UserDbManager.getInstance();

  /**
   * Add RoleUser to database by User
   * 
   * @param user User
   */
  public void addRoleUser(User user) {
    try {
      String username = user.getUsername();
      int uid = udb.getUserIdByUsername(username);
      for (RoleEnum roleEnum : user.getRole()) {
        int rid = rdb.getRoleIdByName(roleEnum.getTypeName());
        addRoleUser(rid, uid);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Add RoleUser to database by rid and uid
   * 
   * @param rid Role Id
   * @param uid User Id
   */
  public void addRoleUser(int rid, int uid) {
    String sql = "INSERT INTO Role_User(rId, uId)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, rid);
      preStmt.setInt(2, uid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Top Role by user Id
   * 
   * @param uid User Id
   * @return topRid Top Role Id
   */
  public int getTopRid(int uid) {
    int topRid = 0;
    String sql = "SELECT rid from Role_User a where uId = ? "
        + "AND (a.rId =(SELECT min(rId) FROM Role_User WHERE uId = ?));";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          topRid = rs.getInt("rid");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return topRid;
  }

  /**
   * get ruid by Role Id and user Id
   * 
   * @param rid Role Id
   * @param uid User Id
   * @return ruId RoleUser Id
   */
  public int getRuid(int rid, int uid) {
    int ruid = 0;
    String sql = "SELECT id FROM Role_User WHERE rId=? AND uId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, rid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          ruid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ruid;
  }

  /**
   * get RoleList by User Id
   * 
   * @return lsRids List RoleEnum
   */
  public List<RoleEnum> getRoleList(int uid) {
    List<RoleEnum> lsRole = new ArrayList<>();
    String sql = "SELECT rId FROM Role_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int rid = rs.getInt("rId");
          RoleEnum role = rdb.getRoleNameById(rid);
          lsRole.add(role);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsRole;
  }

  /**
   * get uids by role Id
   * 
   * @return lsUids role id
   */
  public List<Integer> getUids(int rid) {
    List<Integer> lsUids = new ArrayList<>();
    String sql = "SELECT uId FROM Role_User WHERE rId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, rid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int uid = rs.getInt("uId");
          lsUids.add(uid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsUids;
  }

}