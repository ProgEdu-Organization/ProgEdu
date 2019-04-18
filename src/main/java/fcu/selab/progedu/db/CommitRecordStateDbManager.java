package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommitRecordStateDbManager {

  private static CommitRecordStateDbManager dbManager = new CommitRecordStateDbManager();
  private static ProjectDbManager projectDb = ProjectDbManager.getInstance();

  public static CommitRecordStateDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private CommitRecordStateDbManager() {

  }

  /**
   * add each hw CommitRecordState counts
   * 
   * @param hw
   *          hw's number
   * @param success
   *          build success
   * @param csf
   *          check style error
   * @param cpf
   *          build fault
   * @param utf
   *          junit fault
   * @param ini
   *          not build
   */
  public void addCommitRecordState(String hw, int success, int csf, int cpf, int utf, int ini,
      int ccs) {
    String sql = "INSERT INTO " + "Commit_Record_State(hw, success, checkStyleError, compileFailure"
        + ", testFailure, notBuild, commitCounts)  " + "VALUES(?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, hw);
      preStmt.setInt(2, success);
      preStmt.setInt(3, csf);
      preStmt.setInt(4, cpf);
      preStmt.setInt(5, utf);
      preStmt.setInt(6, ini);
      preStmt.setInt(7, ccs);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * add each hw CommitRecordState counts
   * 
   * @param hw
   *          hw's number
   * @param success
   *          build success
   * @param csf
   *          check style error
   * @param cpf
   *          build fault
   * @param utf
   *          junit fault
   * @param ini
   *          not build
   */
  public void updateCommitRecordState(String hw, int success, int csf, int cpf, int utf, int ini,
      int ccs) {
    String sql = "UPDATE " + "Commit_Record_State  SET success = ? ,checkStyleError = ? "
        + ", compileFailure = ? , testFailure = ? , notBuild = ? , commitCounts = ?  "
        + "where hw = ? ";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, success);
      preStmt.setInt(2, csf);
      preStmt.setInt(3, cpf);
      preStmt.setInt(4, utf);
      preStmt.setInt(5, ini);
      preStmt.setInt(6, ccs);
      preStmt.setString(7, hw);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get all state counts
   * 
   * @return state's counts
   */
  public List<Integer> getCommitRecordStateCounts(String state) {
    String query = "SELECT * FROM Commit_Record_State";
    List<Integer> array = new ArrayList<>();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          array.add(rs.getInt(state));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return array;
  }

  /**
   * check if hw exists in Commit_Record_State DB table
   * 
   * @param hw
   *          hw name
   * 
   * @return check result (boolean)
   */
  public boolean checkCommitRecordStatehw(String hw) {
    String query = "SELECT hw FROM Commit_Record_State where hw=?";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, hw);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          check = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * get commits sum group by hw
   * 
   * @return commits sum
   */

  public List<Integer> getCommitSum() {
    String query = "SELECT commitCounts FROM Commit_Record_State";
    List<Integer> array = new ArrayList<>();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          array.add(rs.getInt("commitCounts"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return array;
  }
}
