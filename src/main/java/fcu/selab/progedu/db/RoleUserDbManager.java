package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleUserDbManager {
  private static RoleUserDbManager dbManager = new RoleUserDbManager();

  public static RoleUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * add RoleUser into database
   * 
   * @param uid User Id
   * @param rid Role Id
   */
  public void addRoleUser(int uid, int rid) {
    String sql = "INSERT INTO Role_User(uId, rId)  VALUES( ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      preStmt.setInt(2, rid);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete RoleUser from database
   * 
   * @param uid User Id
   */
  public void deleteRoleUser(int uid) {
    String sql = "DELETE FROM Role_User WHERE uId = '" + uid + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get rId by uId
   * 
   * @param uid User Id
   * @return rid assignment Id
   */
  public int getRid(int uid) {
    int rid = 0;
    String sql = "SELECT * FROM Role_User WHERE uid=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          rid = rs.getInt("uId");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rid;
  }

}
