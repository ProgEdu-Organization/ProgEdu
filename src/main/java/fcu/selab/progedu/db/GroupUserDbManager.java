package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupUserDbManager {
  private static GroupUserDbManager dbManager = new GroupUserDbManager();

  public static GroupUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private GroupUserDbManager() {

  }

  /**
   * get guId by group Id and user Id
   * 
   * @param gid group Id
   * @param uid User Id
   * @return guId GroupUser Id
   */
  public int getGUId(int gid, int uid) {
    int guid = 0;
    String sql = "SELECT id FROM Group_User WHERE gId=? AND uId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          guid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return guid;
  }

  /**
   * get gId by User Id
   * 
   * @return lsGids group Id
   */
  public List<Integer> getGIds(int uId) {
    List<Integer> lsGids = new ArrayList<>();
    String sql = "SELECT * FROM Group_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int gid = rs.getInt("gId");
          lsGids.add(gid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsGids;
  }

  /**
   * get uId by Group Id
   * 
   * @return lsUids User Id
   */
  public List<Integer> getUIds(int gId) {
    List<Integer> lsUids = new ArrayList<>();
    String sql = "SELECT * FROM Group_User WHERE gId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gId);
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
