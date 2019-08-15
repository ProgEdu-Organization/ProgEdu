package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentUserDbManager {
  private static AssignmentUserDbManager dbManager = new AssignmentUserDbManager();

  public static AssignmentUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentUserDbManager() {

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
   * get auId by assignment Id and user Id
   * 
   * @param aid Assignment Id
   * @param uid User Id
   * @return auId assignmentUser Id
   */
  public int getAUId(int aid, int uid) {
    int auid = 0;
    String sql = "SELECT id FROM Assignment_User WHERE aId=? AND uId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          auid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return auid;
  }

  /**
   * get aId by User Id
   * 
   * @return aids assignment Id
   */
  public List<Integer> getAIds(int uId) {
    List<Integer> lsAids = new ArrayList<>();
    String sql = "SELECT * FROM Assignment_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int aid = rs.getInt("aId");
          lsAids.add(aid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsAids;
  }

  /**
   * get aId by Assignment Id
   *
   * @return aids assignment Id
   */
  public List<Integer> getUids(int aId) {
    List<Integer> lsUids = new ArrayList<>();
    String sql = "SELECT * FROM Assignment_User WHERE aId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aId);
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
