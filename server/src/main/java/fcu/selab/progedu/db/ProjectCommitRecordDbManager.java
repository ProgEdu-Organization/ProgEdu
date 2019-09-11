package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.status.StatusEnum;

public class ProjectCommitRecordDbManager {
  private static ProjectCommitRecordDbManager dbManager = new ProjectCommitRecordDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance();

  public static ProjectCommitRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * insert project commit records into db
   * 
   * @param pgId          auId
   * @param commitNumber  commitNumber
   * @param status        status Id
   * @param time          commit time
   * @param commitStudent commit Student
   */
  public void insertProjectCommitRecord(int pgId, int commitNumber, StatusEnum status, Date time,
      String commitStudent) {
    String sql = "INSERT INTO Project_Commit_Record"
        + "(pgId, commitNumber, status, time, commitStudent) " + "VALUES(?, ?, ?, ?, ?)";
    int statusId = csDb.getStatusIdByName(status.getType());
    Timestamp date = new Timestamp(time.getTime());
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber);
      preStmt.setInt(3, statusId);
      preStmt.setTimestamp(4, date);
      preStmt.setString(5, commitStudent);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get each project's CommitRecordId
   *
   * @param pgId         Project_Commit_Record pgId
   * @param commitNumber commit number
   * @return id
   */
  public int getProjectCommitRecordId(int pgId, int commitNumber) {
    String query = "SELECT id FROM Project_Commit_Record where pgId = ? and commitNumber = ?";
    int id = 0;
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber);

      try (ResultSet rs = preStmt.executeQuery();) {
        if (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return id;
  }

  /**
   * get each project's CommitRecordStateCounts
   *
   * @param pgId         Project_Commit_Record pgId
   * @param commitNumber num
   * @return status
   */
  public String getProjectCommitRecordStatus(int pgId, int commitNumber) {
    String status = "";
    String query = "SELECT status FROM Project_Commit_Record where pgId = ? and limit ?,1";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber - 1);

      try (ResultSet rs = preStmt.executeQuery();) {
        if (rs.next()) {
          status = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return status;

  }

  /**
   * get project commit record details from the homework of a student
   * 
   * 
   * @param pgId pgId
   * @return project commit record details
   */
  public JSONObject getProjectCommitRecord(int pgId) {
    String sql = "SELECT * FROM Project_Commit_Record WHERE pgId=?";
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int statusId = rs.getInt("status");
          StatusEnum statusEnum = csDb.getStatusNameById(statusId);
          int commitNumber = rs.getInt("commitNumber");
          Date commitTime = rs.getTimestamp("time");
          String commitStudent = rs.getString("commitStudent");
          JSONObject eachHw = new JSONObject();
          eachHw.put("status", statusEnum.getType());
          eachHw.put("commitNumber", commitNumber);
          eachHw.put("commitTime", commitTime);
          eachHw.put("commitStudent", commitStudent);
          array.put(eachHw);
        }
      }
      ob.put("commits", array);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ob;
  }

  /**
   * get last project commit record details from assigned homework of one student
   * 
   * 
   * @param pgId pgId
   * @return last project commit record details
   */
  public JSONObject getLastProjectCommitRecord(int pgId) {
    String sql = "SELECT * from Project_Commit_Record a where (a.commitNumber = "
        + "(SELECT max(commitNumber) FROM Project_Commit_Record WHERE pgId = ?));";
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgId);
      try (ResultSet rs = preStmt.executeQuery()) {

        int statusId = rs.getInt("status");
        StatusEnum statusEnum = csDb.getStatusNameById(statusId);
        int commitNumber = rs.getInt("commitNumber");
        Date commitTime = rs.getTimestamp("time");
        String commitStudent = rs.getString("commitStudent");
        JSONObject eachHw = new JSONObject();
        eachHw.put("status", statusEnum.getType());
        eachHw.put("commitNumber", commitNumber);
        eachHw.put("commitTime", commitTime);
        eachHw.put("commitStudent", commitStudent);
        array.put(eachHw);
      }
      ob.put("commits", array);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ob;
  }

  /**
   * get project commit count by pgid
   * 
   * 
   * @param pgId auId
   * @return commitNumber project commitNumber
   */
  public int getProjectCommitCount(int pgId) {
    int commitNumber = 0;
    String sql = "SELECT commitNumber from Project_Commit_Record a where (a.commitNumber = "
        + "(SELECT max(commitNumber) FROM Project_Commit_Record WHERE auId = ?));";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          commitNumber = rs.getInt("commitNumber");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return commitNumber;
  }

  /**
   * delete built record of specific pgid
   *
   * @param pgId pgid
   */
  public void deleteProjectRecord(int pgId) {
    String sql = "DELETE FROM Project_Commit_Record WHERE pgid=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Project_Commit_Status id by pgId
   * 
   * @param pgid pgid
   * @return status status
   */
  public int getCommitStatusbyPgid(int pgid) {
    int status = 0;
    String sql = "SELECT status FROM Project_Commit_Record WHERE pgId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, pgid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          status = rs.getInt("status");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return status;
  }
}
