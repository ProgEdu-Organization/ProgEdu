package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommitRecordDbManager {
  UserDbManager userDbManager = UserDbManager.getInstance();
  private static final String COUNT_STATUS = "count(status)";
  private static final String FIELD_NAME_STATUS = "status";
  private static CommitRecordDbManager dbManager = new CommitRecordDbManager();
  private UserDbManager udb = UserDbManager.getInstance();
  private static CommitStatusDbManager CSdbManager = new CommitStatusDbManager();

  public static CommitRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * insert student commit records into db
   * 
   * @param auId         auId
   * @param commitNumber commitNumber
   * @param status       status Id
   * @param time         commit time
   */
  public void insertCommitRecord(int auId, int commitNumber, int status, String time) {
    String sql = "INSERT INTO Commit_Record" + "(auId, commitNumber, status, time) "
        + "VALUES(?, ?, ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, commitNumber);
      preStmt.setInt(3, status);
      preStmt.setString(4, time);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get each hw's CommitRecordStateCounts
   *
   * @param auId         Commit_Record auId
   * @param commitNumber commit number
   * @return status
   */
  public int getCommitRecordId(int auId, int commitNumber) {
    String query = "SELECT id FROM Commit_Record where auId = ? and commitNumber = ?";
    int id = 0;
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
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
   * get each hw's CommitRecordStateCounts
   *
   * @param auId Commit_Record auId
   * @param num  num
   * @return status
   */
  public String getCommitRecordStatus(int auId, int num) {
    String status = "";
    String query = "SELECT status FROM Commit_Record where auId = ? and limit ?,1";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, num - 1);

      try (ResultSet rs = preStmt.executeQuery();) {
        if (rs.next()) {
          status = rs.getString(FIELD_NAME_STATUS);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return status;
  }

  /**
   * get commit record details from the homework of a student
   * 
   * 
   * @param auIds auId
   * @return commit record details
   */
  public JSONObject getCommitRecord(int auIds) {
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auIds);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          String status = rs.getString("status");
          int commitNumber = rs.getInt("commitNumber");
          String commitTime = rs.getString("time");
          JSONObject eachHw = new JSONObject();
          eachHw.put("status", status);
          eachHw.put("commitNumber", commitNumber);
          eachHw.put("commitTime", commitTime);
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
   * get last commit record details from assigned homework of one student
   * 
   * 
   * @param auId auId
   * @return last commit record details
   */
  public JSONObject getLastCommitRecord(int auId) {
    String sql = "SELECT * from Commit_Record a where (a.commitNumber = "
        + "(SELECT max(commitNumber) FROM Commit_Record WHERE auId = ?));";
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      try (ResultSet rs = preStmt.executeQuery()) {

        String status = rs.getString("status");
        int commitNumber = rs.getInt("commitNumber");
        String commitTime = rs.getString("time");
        JSONObject eachHw = new JSONObject();
        eachHw.put("status", status);
        eachHw.put("commitNumber", commitNumber);
        eachHw.put("commitTime", commitTime);
        array.put(eachHw);
      }
      ob.put("commits", array);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ob;
  }

  /**
   * get commit count by auId
   * 
   * 
   * @param id auId
   * @return aId assignment Id
   */
  public int getCommitCount(int id) {
    int count = 0;
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          count = rs.getInt("count(auId)");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  /**
   * delete built record of specific auId
   *
   * @param auId auId
   */
  public void deleteRecord(int auId) {
    String sql = "DELETE FROM Commit_Record WHERE auId=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Commit_Status id by auid
   * 
   * @param auid auId
   * @return status status
   */
  public int getCommitStatusbyAUId(int auid) {
    int status = 0;
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          status = rs.getInt("auid");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return status;
  }

}
