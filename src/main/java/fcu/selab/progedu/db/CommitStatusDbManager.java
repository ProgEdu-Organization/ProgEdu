package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommitStatusDbManager {
  private static CommitStatusDbManager dbManager = new CommitStatusDbManager();

  public static CommitStatusDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * add CommitStatus into database
   * 
   * @param status Commit_Status status
   */
  public void addAssignmentType(String status) {
    String sql = "INSERT INTO Commit_Status(status)  VALUES( ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, status);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete CommitStatus from database
   * 
   * @param status Commit_Status status
   */
  public void deleteAssignmentType(String status) {
    String sql = "DELETE FROM Commit_Status WHERE status ='" + status + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, status);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get CommitStatus id by status
   * 
   * @param status Commit_Status statusName
   * @return id status id
   */
  public int getCommitStatusId(String status) {
    int id = 0;
    String sql = "SELECT * FROM Commit_Status WHERE status=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, status);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * get CommitStatus status by id
   * 
   * @param id Commit_Status id
   * @return status Commit_Status name
   */
  public String getCommitStatusName(int id) {
    String status = null;
    String sql = "SELECT * FROM Commit_Status WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          status = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return status;
  }

}
