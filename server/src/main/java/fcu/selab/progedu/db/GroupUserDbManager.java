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

public class GroupUserDbManager {
  private static GroupUserDbManager dbManager = new GroupUserDbManager();

  public static GroupUserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupUserDbManager.class);

  private GroupUserDbManager() {

  }

  /**
   * Add GroupUser to database
   * 
   * @param gid Group Id
   * @param uid User Id
   */
  public void addGroupUser(int gid, int uid) {
    String sql = "INSERT INTO ProgEdu.Group_User(gId, uId) VALUES(?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
      preStmt.setInt(2, uid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get guId by group Id and user Id
   * 
   * @param gid group Id
   * @param uid User Id
   * @return guId GroupUser Id
   */
  public int getGuid(int gid, int uid) {
    int guid = 0;
    String sql = "SELECT id FROM Group_User WHERE gId=? AND uId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
      preStmt.setInt(2, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          guid = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return guid;
  }

  /**
   * get gId by User Id
   * 
   * @return lsGids group Id
   */
  public List<Integer> getGIds(int uid) {
    List<Integer> lsGids = new ArrayList<>();
    String sql = "SELECT gId FROM Group_User WHERE uId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, uid);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int gid = rs.getInt("gId");
          lsGids.add(gid);
        }
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return lsGids;
  }

  /**
   * get uId by Group Id
   * 
   * @return lsUids User Id
   */
  public List<Integer> getUids(int gid) {
    List<Integer> lsUids = new ArrayList<>();
    String sql = "SELECT uId FROM Group_User WHERE gId = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
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
   * remove Group_User by gid
   * 
   * @param gid group id
   */
  public void remove(int gid) {
    String sql = "DELETE FROM Group_User WHERE gId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * remove Group_User
   * 
   * @param gid group id
   * @param uid user id
   */
  public void remove(int gid, int uid) {
    String sql = "DELETE FROM Group_User WHERE gId=? AND uId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, gid);
      preStmt.setInt(2, uid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * remove Group_User by uid(user_id)
   *
   * @param userId user id
   */
  public void removeByUserId(int userId) {
    String sql = "DELETE FROM Group_User WHERE uid=?";
    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, userId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
