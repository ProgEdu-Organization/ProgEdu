package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentUserDbManager {
  private static AssignmentUserDbManager dbManager = new AssignmentUserDbManager();

  public static AssignmentUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentUserDbManager.class);

  private AssignmentUserDbManager() {

  }

  /**
   * Add AssignmentUser to database
   * 
   * @param aid Assignment Id
   * @param uid User Id
   */
  public void addAssignmentUser(int aid, int uid) {
    String sql = "INSERT INTO Assignment_User(aId, uId)  VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, uid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get auId by assignment Id and user Id
   * 
   * @param aid Assignment Id
   * @param uid User Id
   * @return auId assignmentUser Id
   */
  public int getAuid(int aid, int uid) {
    int auid = 0;
    String sql = "SELECT id FROM Assignment_User WHERE aId = ? AND uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          auid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return auid;
  }

  /**
   * get auIds by assignment Id
   * 
   * @param aid Assignment Id
   * @return auId assignmentUser Id
   */
  public List<Integer> getAuids(int aid) {
    List<Integer> lsAUid = new ArrayList<>();
    String sql = "SELECT id FROM Assignment_User WHERE aId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int auid = rs.getInt("id");
          lsAUid.add(auid);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return lsAUid;
  }

  /**
   * get auIds by assignment uid(user_id)
   *
   * @param userId User Id
   * @return auId assignmentUser Id
   */
  public List<Integer> getIdListByUid(int userId) {
    List<Integer> auids = new ArrayList<>();
    String sql = "SELECT id FROM Assignment_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, userId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int auid = rs.getInt("id");

          auids.add(auid);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return auids;
  }


  /**
   * get aids by User Id
   * 
   * @return lsAids assignment id
   */
  public List<Integer> getAIds(int uid) {
    List<Integer> lsAids = new ArrayList<>();
    String sql = "SELECT aId FROM Assignment_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int aid = rs.getInt("aId");
          lsAids.add(aid);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return lsAids;
  }

  /**
   * get uId by Assignment Id
   *
   * @return lsUids assignment Id
   */
  public List<Integer> getUids(int aid) {
    List<Integer> lsUids = new ArrayList<>();
    String sql = "SELECT uId FROM Assignment_User WHERE aId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int uid = rs.getInt("uId");
          lsUids.add(uid);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return lsUids;
  }

  /**
   * Delete assignment_user from database by aid
   * 
   */
  public void deleteAssignmentUserByAid(int aid) {
    String sql = "DELETE FROM Assignment_User WHERE aId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, aid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Delete assignment_user from database by Uid(user_id)
   *
   */
  public void deleteAssignmentUserByUid(int userId) {
    String sql = "DELETE FROM Assignment_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, userId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
