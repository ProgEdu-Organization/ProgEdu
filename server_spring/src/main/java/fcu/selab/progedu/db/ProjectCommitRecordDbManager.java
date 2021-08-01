package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class ProjectCommitRecordDbManager {
  private static ProjectCommitRecordDbManager dbManager = new ProjectCommitRecordDbManager();
  private static CommitStatusDbManager csDb = CommitStatusDbManager.getInstance();

  public static ProjectCommitRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCommitRecordDbManager.class);

  /**
   * insert project commit records into db
   *
   * @param pgId auId
   * @param commitNumber commitNumber
   * @param status status Id
   * @param time commit time
   * @param commitStudent commit Student
   */
  public void insertProjectCommitRecord(
      int pgId, int commitNumber, StatusEnum status, Date time, String commitStudent) {
    String sql =
        "INSERT INTO Project_Commit_Record"
            + "(pgId, commitNumber, status, time, commitStudent) "
            + "VALUES(?, ?, ?, ?, ?)";
    int statusId = csDb.getStatusIdByName(status.getType());
    Timestamp date = new Timestamp(time.getTime());

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber);
      preStmt.setInt(3, statusId);
      preStmt.setTimestamp(4, date);
      preStmt.setString(5, commitStudent);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * get project's CommitRecordId
   *
   * @param pgId Project_Commit_Record pgId
   * @param commitNumber commit number
   * @return id
   */
  public int getProjectCommitRecordId(int pgId, int commitNumber) {
    String query = "SELECT id FROM Project_Commit_Record where pgId = ? and commitNumber = ?";
    int id = 0;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber);

      rs = preStmt.executeQuery();
        if (rs.next()) {
          id = rs.getInt("id");
        }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return id;
  }

  /**
   * get each project's CommitRecordId
   *
   * @param pgId Project_Commit_Record pgId
   * @return ids
   */
  public List<Integer> getProjectCommitRecordId(int pgId) {
    String query = "SELECT id FROM Project_Commit_Record where pgId = ?";
    List<Integer> ids = new ArrayList<>();

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pgId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        ids.add(id);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return ids;
  }

  /**
   * get each project's CommitRecordStateCounts
   *
   * @param pgId Project_Commit_Record pgId
   * @param commitNumber num
   * @return status
   */
  public int getProjectCommitRecordStatus(int pgId, int commitNumber) {
    int status = 0;
    String query = "SELECT status FROM Project_Commit_Record where pgId = ? and commitNumber = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, pgId);
      preStmt.setInt(2, commitNumber);

      rs = preStmt.executeQuery();
      if (rs.next()) {
        status = rs.getInt("status");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return status;
  }

  /**
   * get project commit record details from the homework of a student
   *
   * @param pgId pgId
   * @return project commit record details
   */
  public List<CommitRecord> getProjectCommitRecords(int pgId) {
    String sql = "SELECT * FROM Project_Commit_Record WHERE pgId=?";
    List<CommitRecord> crs = new ArrayList<>();

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);


      preStmt.setInt(1, pgId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int statusId = rs.getInt("status");
        StatusEnum statusEnum = csDb.getStatusNameById(statusId);
        int commitNumber = rs.getInt("commitNumber");
        Date commitTime = rs.getTimestamp("time");
        String committer = rs.getString("commitStudent");

        CommitRecord cr = new CommitRecord();
        cr.setCommitter(committer);
        cr.setNumber(commitNumber);
        cr.setStatus(statusEnum);
        cr.setTime(commitTime);
        crs.add(cr);
      }


    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return crs;
  }

  /**
   * get part project commit record details from the homework of a student
   *
   * @param pgId pgId
   * @param currentPage current page
   * @return project commit record details
   */
  public List<CommitRecord> getPartProjectCommitRecords(int pgId, int currentPage) {
    String sql = "SELECT * FROM ProgEdu.Project_Commit_Record WHERE pgId = ? "
        + "AND commitNumber IN (SELECT commitNumber FROM "
        + "ProgEdu.Project_Commit_Record WHERE commitNumber BETWEEN ? AND ?)";
    List<CommitRecord> crs = new ArrayList<>();
    int totalCommitNumber = getProjectCommitCount(pgId);
    int startSearchNumber = totalCommitNumber - (currentPage - 1) * 5;
    int endSearchNumber = startSearchNumber - 4;
    if (endSearchNumber <= 0) {
      endSearchNumber = 1;
    }

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgId);
      preStmt.setInt(2, endSearchNumber);
      preStmt.setInt(3, startSearchNumber);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int statusId = rs.getInt("status");
        StatusEnum statusEnum = csDb.getStatusNameById(statusId);
        int commitNumber = rs.getInt("commitNumber");
        Date commitTime = rs.getTimestamp("time");
        String committer = rs.getString("commitStudent");

        CommitRecord cr = new CommitRecord();
        cr.setCommitter(committer);
        cr.setNumber(commitNumber);
        cr.setStatus(statusEnum);
        cr.setTime(commitTime);
        crs.add(cr);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return crs;
  }

  /**
   * get last project commit record details from assigned homework of one student
   *
   * @param pgId pgId
   * @return last project commit record details
   */
  public CommitRecord getLastProjectCommitRecord(int pgId) {
    String sql =
        "SELECT * from Project_Commit_Record a where (a.commitNumber = "
            + "(SELECT max(commitNumber) FROM Project_Commit_Record WHERE pgId = ?)) AND pgId = ?;";
    CommitRecord cr = null;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgId);
      preStmt.setInt(2, pgId);
      rs = preStmt.executeQuery();

      if (rs.next()) {
        cr = new CommitRecord();
        int statusId = rs.getInt("status");
        StatusEnum statusEnum = csDb.getStatusNameById(statusId);
        int commitNumber = rs.getInt("commitNumber");
        Date commitTime = rs.getTimestamp("time");
        String committer = rs.getString("commitStudent");
        int id = rs.getInt("id");
        cr.setId(id);
        cr.setStatus(statusEnum);
        cr.setCommitter(committer);
        cr.setNumber(commitNumber);
        cr.setTime(commitTime);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return cr;
  }

  /**
   * get project commit count by pgid
   *
   * @param pgId auId
   * @return commitNumber project commitNumber
   */
  public int getProjectCommitCount(int pgId) {
    int commitNumber = 0;
    String sql =
        "SELECT max(commitNumber) AS commitNumber FROM Project_Commit_Record WHERE pgId = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgId);
      rs = preStmt.executeQuery();
      if (rs.next()) {
        commitNumber = rs.getInt("commitNumber");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;


    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;


    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pgid);

      while (rs.next()) {
        status = rs.getInt("status");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return status;
  }

}
