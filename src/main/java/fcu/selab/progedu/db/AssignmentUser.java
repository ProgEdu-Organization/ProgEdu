package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignmentUser {
  private static AssignmentUser dbManager = new AssignmentUser();

  public static AssignmentUser getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentUser() {

  }

  /**
   * add AssignmentUser into database
   * 
   * @param aid Assignment Id
   * @param uid User Id
   */
  public void addAssignmentUser(int aid, int uid) {
    String sql = "INSERT INTO Assignment_User(aId, uId)  VALUES( ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, uid);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete AssignmentUser from database
   * 
   * @param aid Assignment Id
   * @param uid User Id
   */
  public void deleteAssignmentUser(int aid, int uid) {
    String sql = "DELETE FROM Assignment_User WHERE aId ='" + aid + "' AND uId = '" + uid + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get aId by AssignmentUser Id
   * 
   * @param id AssignmentUser Id
   * @return aId assignment Id
   */
  public int getAid(int id) {
    int aid = 0;
    String sql = "SELECT * FROM Assignment_User WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          aid = rs.getInt("aId");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return aid;
  }

  /**
   * get aId by AssignmentUser Id
   * 
   * @param id AssignmentUser Id
   * @return aId assignment Id
   */
  public int getUid(int id) {
    int uid = 0;
    String sql = "SELECT * FROM Assignment_User WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          uid = rs.getInt("uId");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return uid;
  }
}
