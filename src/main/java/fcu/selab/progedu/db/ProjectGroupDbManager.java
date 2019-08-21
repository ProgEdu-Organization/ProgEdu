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
   * Add ProjectGroup to database
   * 
   * @param pid Project Id
   * @param gid Group Id
   */
  public void addProjectGroup(int pid, int gid) {
    String sql = "INSERT INTO AssignmentUser(pId, gId)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pid);
      preStmt.setInt(2, gid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get pgId by project Id and group Id
   * 
   * @param pid Project Id
   * @param gid Group Id
   * @return pgId projectGroup Id
   */
  public int getPgid(int pid, int gid) {
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
   * get pids by Group Id
   * 
   * @return lsPids project Id
   */
  public List<Integer> getPIds(int gid) {
    List<Integer> lsPids = new ArrayList<>();
    String sql = "SELECT pId FROM Project_Group WHERE gId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
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
  public List<Integer> getUids(int pid) {
    List<Integer> lsGids = new ArrayList<>();
    String sql = "SELECT gId FROM Project_Group WHERE pId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pid);
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
