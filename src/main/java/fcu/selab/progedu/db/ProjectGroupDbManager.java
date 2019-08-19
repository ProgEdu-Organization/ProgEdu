package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectGroupDbManager {
  private static ProjectGroupDbManager dbManager = new ProjectGroupDbManager();

  public static ProjectGroupDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private ProjectGroupDbManager() {

  }

  /**
   * get pgId by project Id and group Id
   * 
   * @param pid Project Id
   * @param gid Group Id
   * @return pgId projectGroup Id
   */
  public int getPGId(int pid, int gid) {
    int pgid = 0;
    String sql = "SELECT id FROM Project_Group WHERE pId=? AND gId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pid);
      preStmt.setInt(2, gid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          pgid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return pgid;
  }

  /**
   * get pIds by Group Id
   * 
   * @return lsPids project Id
   */
  public List<Integer> getPIds(int gId) {
    List<Integer> lsPids = new ArrayList<>();
    String sql = "SELECT * FROM Project_Group WHERE gId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int pid = rs.getInt("pId");
          lsPids.add(pid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsPids;
  }

  /**
   * get gId by Project Id
   *
   * @return lsGids Group Id
   */
  public List<Integer> getUids(int pId) {
    List<Integer> lsGids = new ArrayList<>();
    String sql = "SELECT * FROM Project_Group WHERE pId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pId);
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

}
