package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.status.StatusEnum;

public class CommitRecordDbManager {
  UserDbManager userDbManager = UserDbManager.getInstance();
  private static final String FIELD_NAME_STATUS = "status";
  private static CommitRecordDbManager dbManager = new CommitRecordDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance();

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
  public void insertCommitRecord(int auId, int commitNumber, StatusEnum status, Date time) {
    String sql = "INSERT INTO Commit_Record" + "(auId, commitNumber, status, time) "
        + "VALUES(?, ?, ?, ?)";
    int statusId = csDb.getStatusIdByName(status.getType());
    Timestamp date = new Timestamp(time.getTime());
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, commitNumber);
      preStmt.setInt(3, statusId);
      preStmt.setTimestamp(4, date);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get each hw's CommitRecordId
   *
   * @param auId         Commit_Record auId
   * @param commitNumber commit number
   * @return id
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
   * get all hw's CommitRecordId by auid
   *
   * @param auId Commit_Record auId
   * @return lsCRid list of CRid
   */
  public List<Integer> getCommitRecordId(int auId) {
    String query = "SELECT id FROM Commit_Record where auId = ?";
    List<Integer> lsCRid = new ArrayList<>();
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);

      try (ResultSet rs = preStmt.executeQuery();) {
        if (rs.next()) {
          int id = rs.getInt("id");
          lsCRid.add(id);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return lsCRid;
  }

  /**
   * get each hw's CommitRecordStateCounts
   *
   * @param auId Commit_Record auId
   * @param num  num
   * @return status
   */
  public int getCommitRecordStatus(int auId, int num) {
    int status = -1;
    String query = "SELECT status FROM Commit_Record where auId = ? and commitNumber = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, num);

      try (ResultSet rs = preStmt.executeQuery();) {
        if (rs.next()) {
          status = rs.getInt(FIELD_NAME_STATUS);
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
   * @param auId auId
   * @return commit record details
   */
  public List<CommitRecord> getCommitRecord(int auId) {
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    List<CommitRecord> commitRecords = new ArrayList<>();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int statusId = rs.getInt("status");
          StatusEnum statusEnum = csDb.getStatusNameById(statusId);
          int commitNumber = rs.getInt("commitNumber");
          Date commitTime = rs.getTimestamp("time");
          CommitRecord commitRecord = new CommitRecord();
          commitRecord.setNumber(commitNumber);
          commitRecord.setStatus(statusEnum);
          commitRecord.setTime(commitTime);
          commitRecords.add(commitRecord);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return commitRecords;
  }

  /**
   * get last commit record details from assigned homework of one student
   * 
   * 
   * @param auId auId
   * @return last commit record details
   */
  public JSONObject getLastCommitRecord(int auId) {
    String sql = "SELECT * from Commit_Record as a WHERE (a.commitNumber = "
        + "(SELECT max(commitNumber) FROM Commit_Record WHERE auId = ?) AND auId = ?);";
    JSONObject ob = new JSONObject();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, auId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int statusId = rs.getInt("status");
          StatusEnum statusEnum = csDb.getStatusNameById(statusId);
          int commitNumber = rs.getInt("commitNumber");
          Date commitTime = rs.getTimestamp("time");
          ob.put("status", statusEnum.getType());
          ob.put("commitNumber", commitNumber);
          ob.put("commitTime", commitTime);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ob;
  }

  /**
   * get commit count by auId
   * 
   * 
   * @param auid auId
   * @return aId assignment Id
   */
  public int getCommitCount(int auid) {
    int commitNumber = 0;
    String sql = "SELECT commitNumber from Commit_Record a where (a.commitNumber = "
        + "(SELECT max(commitNumber) FROM Commit_Record WHERE auId = ?));";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auid);
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
  public int getCommitStatusbyAuid(int auid) {
    int status = 0;
    String sql = "SELECT status FROM Commit_Record WHERE auId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auid);
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
