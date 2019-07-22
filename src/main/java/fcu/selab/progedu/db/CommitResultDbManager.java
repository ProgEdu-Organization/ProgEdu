package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.User;

public class CommitResultDbManager {
  private static final String STATUS = "status";
  private static final String COMMIT = "commit";
  private static CommitResultDbManager dbManager = new CommitResultDbManager();
  private UserDbManager udb = UserDbManager.getInstance();
  private IDatabase database = new MySqlDatabase();

  public static CommitResultDbManager getInstance() {
    return dbManager;
  }

  private CommitResultDbManager() {

  }

  /**
   * aggregate jenkins situation
   *
   * @param id
   *          student id
   * @param hw
   *          hw name
   * @param commit
   *          commit count
   * @param status
   *          build status
   * @return check
   */
  public boolean insertJenkinsCommitCount(int id, String hw, int commit, String status) {
    String sql = "INSERT INTO Commit_Result" + "(stuId, hw, commit, status) "
        + "VALUES(?, ?, ?, ?)";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      preStmt.setString(2, hw);
      preStmt.setInt(3, commit);
      preStmt.setString(4, status);
      preStmt.executeUpdate();
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * insert job last commit time to db
   *
   * @param id
   *          stu id
   * @param hw
   *          hw number
   * @param time
   *          commit time
   * @return check
   */
  public boolean updateJenkinsJobTimestamp(int id, String hw, String time) {
    String sql = "UPDATE Commit_Result SET time=? WHERE stuId=? AND hw=?";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, time);
      preStmt.setInt(2, id);
      preStmt.setString(3, hw);
      preStmt.executeUpdate();
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * check if result is in db
   *
   * @param id
   *          student id
   * @param hw
   *          he number
   * @return boolean
   */
  public boolean checkJenkinsJobTimestamp(int id, String hw) {
    String query = "SELECT * FROM Commit_Result WHERE stuId=? AND hw=?";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.setString(2, hw);
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
   * update jenkins situation
   *
   * @param id
   *          student id
   * @param hw
   *          hw name
   * @param commit
   *          commit count
   * @param status
   *          build status
   * @return check
   */
  public boolean updateJenkinsCommitCount(int id, String hw, int commit, String status) {
    String sql = "UPDATE Commit_Result SET commit=?, status=? WHERE stuId=? AND hw=?";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, commit);
      preStmt.setString(2, status);
      preStmt.setInt(3, id);
      preStmt.setString(4, hw);
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
  public JSONObject getCounts(String status) {
    String query = "SELECT hw,count(status) FROM Commit_Result "
        + "where status like ? group by hw";
    JSONObject ob = new JSONObject();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, status);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          String hw = rs.getString("hw");
          int count = rs.getInt("count(status)");

          ob.put(hw, count);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ob;
  }

  /**
   * get commit result by student
   *
   * @param id
   *          stuId
   * @param hw
   *          hw
   * @return commit result
   */
  public CommitRecord getCommitResultByStudentAndHw(int id, String hw) {
    String query = "SELECT * FROM Commit_Result WHERE stuId=? AND hw=?";
    CommitRecord result = new CommitRecord();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.setString(2, hw);

      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          String status = rs.getString(STATUS);
          int commit = rs.getInt(COMMIT);

          result.setId(id);
          result.setAuId(hw);
          result.setStatus(status);
          result.setCommitNumber(commit);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * get commit result by student
   *
   * @param id
   *          stuId
   * @return commit result
   */
  public JSONObject getCommitResultByStudent(int id) {
    String query = "SELECT * FROM Commit_Result WHERE stuId=?";
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();
    String name = "";
    int gitlabId = -1;

    User user = udb.getUser(id);
    name = user.getStufentId();
    gitlabId = user.getGitLabId();
    ob.put("userName", name);
    ob.put("gitlabId", gitlabId);
    ob.put("name", user.getName());

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);

      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {

          String hw = rs.getString("hw");
          String status = rs.getString(STATUS);
          int commit = rs.getInt(COMMIT);
          JSONObject eachHw = new JSONObject();
          eachHw.put("hw", hw);
          eachHw.put(COMMIT, commit);
          eachHw.put(STATUS, status);
          array.put(eachHw);
        }
      }
      ob.put("commits", array);
    } catch (SQLException e) {
      e.printStackTrace();
      return new JSONObject();
    }
    return ob;
  }

  /**
   * list all commir result
   *
   * @return array
   */
  public JSONArray listAllCommitResult() {
    String query = "SELECT * FROM Commit_Result";
    JSONArray array = new JSONArray();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          JSONObject ob = new JSONObject();
          int id = rs.getInt("stuId");
          String status = rs.getString(STATUS);
          int commit = rs.getInt(COMMIT);

          User user = udb.getUser(id);

          ob.put("user", user.getStufentId());
          ob.put(STATUS, status);
          ob.put(COMMIT, commit + 1);
          array.put(ob);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return array;
  }

  /**
   * get hw commit timestamp
   *
   * @param hw
   *          hw number
   */
  public void getCommitTimestamp(String hw) {
    String query = "SELECT hw, count(commit), time FROM Commit_Result"
        + "where hw=? and commit!=0 group by time";

    String[] csvTitle = { "date", "time", COMMIT };
    StringBuilder build = new StringBuilder();
    for (int i = 0; i < csvTitle.length; i++) {
      build.append(csvTitle[i]);
      if (i == csvTitle.length) {
        break;
      }
      build.append(",");
    }
    build.append("\n");

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, hw);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          int commit = rs.getInt("count(commit)");
          String fullTime = rs.getString("time");
          String date = fullTime.substring(0, 10);
          String time = fullTime.substring(11);

          build.append(date);
          build.append(",");
          build.append(time);
          build.append(",");
          build.append(commit);
          build.append("\n");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete build result of specific hw
   *
   * @param hw
   *          hw
   */
  public void deleteResult(String hw) {
    String sql = "DELETE FROM Commit_Result WHERE hw=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, hw);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
