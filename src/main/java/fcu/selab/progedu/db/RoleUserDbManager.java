package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleUserDbManager {
  private static RoleUserDbManager dbManager = new RoleUserDbManager();

  public static RoleUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * Add RoleUser to database
   * 
   * @param rid Role Id
   * @param uid User Id
   */
  public void addRoleUser(int rid, int uid) {
    String sql = "INSERT INTO RoleUser(rId, uId)  VALUES(?, ?)";

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
   * get rids by User Id
   * 
   * @return lsRids role id
   */
  public List<Integer> getRids(int uid) {
    List<Integer> lsRids = new ArrayList<>();
    String sql = "SELECT rId FROM Role_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int rid = rs.getInt("rId");
          lsRids.add(rid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsRids;
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