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
   * @param auId
   *          Commit_Record auId
   * @param commitNumber
   *          commit number
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
   * @param auId
   *          Commit_Record auId
   * @param num
   *          num
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
   * check if record is in db
   *
   * @param id   student id
   * @param auId auId
   * @param time commit time
   * @return boolean
   */
  public boolean checkRecord(int id, int auId, String time) {
    String query = "SELECT * FROM Commit_Record where id=? and auId=? and time=?";

    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.setInt(2, auId);
      preStmt.setString(3, time);
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
   * update record status
   * 
   * @param id     student
   * @param auId   auId
   * @param status status
   * @param time   time
   */
  public void updateRecordStatus(int id, int auId, int status, String time) {
    String sql = "UPDATE Commit_Record SET status=? where id=? and auId=? and time=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, status);
      preStmt.setInt(2, id);
      preStmt.setInt(3, auId);
      preStmt.setString(4, time);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get commit record details from the homework of a student
   * 
   * 
   * @param auIds
   *          auId
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
   * get Commit_time id by auid
   * 
   * @param auid auId
   * @return time time
   */
  public String getCommitTimebyAUId(int auid) {
    String time = null;
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          time = rs.getString("time");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return time;
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

  /**
   * get Commit_Status id
   * 
   * @param statusName Commit_Status statusName
   * @return id status id
   */
  public int getCommitStatusId(String statusName) {
    int id = 0;
    String sql = "SELECT * FROM Commit_Status WHERE status=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, statusName);
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
   * get Commit_Status name
   * 
   * @param id Commit_Status id
   * @return name Commit_Status name
   */
  public String getCommitStatusName(int id) {
    String statusName = null;
    String sql = "SELECT * FROM Commit_Status WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          statusName = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return statusName;
  }
}
