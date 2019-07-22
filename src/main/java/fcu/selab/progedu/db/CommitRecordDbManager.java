package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.status.StatusEnum;

public class CommitRecordDbManager {
  UserDbManager userDbManager = UserDbManager.getInstance();
  private static final String COUNT_STATUS = "count(status)";
  private static final String FIELD_NAME_STATUS = "status";
  private static CommitRecordDbManager dbManager = new CommitRecordDbManager();

  public static CommitRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * insert student commit records into db
   *
   * @param stuId  studrnt id
   * @param hw     hw number
   * @param status build result
   * @param time   commit time
   * @return check
   */
  public boolean insertCommitRecord(int stuId, String hw, String status, String date, String time) {
    String sql = "INSERT INTO Commit_Record" + "(stuId, hw, status, date, time) "
        + "VALUES(?, ?, ?, ?, ?)";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, stuId);
      preStmt.setString(2, hw);
      preStmt.setString(3, status);
      preStmt.setString(4, date);
      preStmt.setString(5, time);
      preStmt.executeUpdate();
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * get all counts
   *
   * @return counts
   */
  public List<Integer> getCounts(String status) {
    String query = "SELECT hw,count(status) FROM Commit_Record where status like ? group by hw";
    List<Integer> array = new ArrayList<>();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, status);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          array.add(rs.getInt(COUNT_STATUS));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return array;
  }

  /**
   * get each hw's CommitRecordStateCounts
   * 
   * @param auId Commit_Record auId
   * @return map
   */
  public Map<String, Integer> getCommitRecordStateCounts(int auId) {
    String query = "SELECT auid,status,count(status) FROM Commit_Record where auId = ? "
        + "group by status";
    Map<String, Integer> map = new HashMap<>();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          map.put(dbManager.getCommitStatusName(rs.getInt("status")), rs.getInt(COUNT_STATUS));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return map;
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
   * check if record is in db
   *
   * @param stuId student id
   * @param hw    he number
   * @param date  date
   * @param time  commit time
   * @return boolean
   */
  public boolean checkRecord(int stuId, String hw, String date, String time) {
    String query = "SELECT * FROM Commit_Record where stuId=? and hw=? and date=? and time=?";

    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, stuId);
      preStmt.setString(2, hw);
      preStmt.setString(3, date);
      preStmt.setString(4, time);
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
   * @param stuId  student
   * @param hw     hw
   * @param status status
   * @param date   date
   * @param time   time
   */
  public void updateRecordStatus(int stuId, String hw, String status, String date, String time) {
    String sql = "UPDATE Commit_Record SET status=? where stuId=? and hw=? and date=? and time=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, status);
      preStmt.setInt(2, stuId);
      preStmt.setString(3, hw);
      preStmt.setString(4, date);
      preStmt.setString(5, time);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Count Group By Hw And Time
   * 
   * @param hw hw number
   * @return records
   */
  public JSONArray getCountGroupByHwAndTime(String hw) {
    String status = StatusEnum.INITIALIZATION.getTypeName();
    String query = "select date, time, count(status) from Commit_Record where hw=?  and status!="
        + "'" + status + "'" + "group by date, time";
    JSONArray records = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, hw);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          String date = rs.getString("date");
          String time = rs.getString("time");
          String[] times = time.split(":");
          final double timeValue = Integer.valueOf(times[0]) + (Integer.valueOf(times[1])) * 0.01;

          Timestamp ts = new Timestamp(System.currentTimeMillis());
          ts = Timestamp.valueOf(date + " " + time);

          Timestamp xlabel = new Timestamp(System.currentTimeMillis());
          xlabel = Timestamp.valueOf(date + " " + time);
          xlabel.setHours(0);
          xlabel.setMinutes(0);
          xlabel.setSeconds(0);
          int count = rs.getInt(COUNT_STATUS);
          JSONObject record = new JSONObject();
          record.put("x", xlabel.getTime());
          record.put("y", timeValue);
          record.put("r", count);
          record.put("t", ts.getTime());
          records.put(record);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  /**
   * delete built record of specific hw
   *
   * @param hw hw
   */
  public void deleteRecord(String hw) {
    String sql = "DELETE FROM Commit_Record WHERE hw=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, hw);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
