package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewSettingDbManager {

  private static ReviewSettingDbManager dbManager = new ReviewSettingDbManager();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSettingDbManager.class);

  public static ReviewSettingDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Insert assignment review setting into db
   *
   * @param aid         assignment Id
   * @param amount      reviewer amount
   * @param round       review round
   */
  public void insertReviewSetting(int aid, int amount, int round) throws SQLException {
    String query = "INSERT INTO Review_Setting(aId, amount, round)"
        + " VALUES(?, ?, ?)";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, aid);
      preStmt.setInt(2, amount);
      preStmt.setInt(3, round);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * Get id from review setting by assignment Id
   *
   * @param aid assignment Id
   * @return id
   */
  public int getReviewSettingIdByAid(int aid) throws SQLException {
    String query = "SELECT id FROM Review_Setting WHERE aId = ?";
    int id = 0;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, aid);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        id = rs.getInt("id");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return id;
  }

  /**
   *  Get all review setting
   */
  public List<ReviewSetting> getAllReviewSetting() throws SQLException {
    //TODO reviewSetting 的時間要拔掉
    String query = "SELECT * FROM Review_Setting";
    List<ReviewSetting> reviewSettingList = new ArrayList<>();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      rs = preStmt.executeQuery();

      while (rs.next()) {
        ReviewSetting reviewSetting = new ReviewSetting();
        int id = rs.getInt("id");
        int aid = rs.getInt("aId");
        int amount = rs.getInt("amount");
        reviewSetting.setId(id);
        reviewSetting.setaId(aid);
        reviewSetting.setAmount(amount);
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewSettingList;
  }

  /**
   * Get all from review setting by assignment Id
   *
   * @param aid assignment Id
   * @return review setting details
   */
  public ReviewSetting getReviewSetting(int aid) throws SQLException {
    String query = "SELECT * FROM Review_Setting WHERE aId = ?";
    ReviewSetting reviewSetting = new ReviewSetting();
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, aid);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        int amount = rs.getInt("amount");
        int round = rs.getInt("round");
        reviewSetting.setId(id);
        reviewSetting.setaId(aid);
        reviewSetting.setAmount(amount);
        reviewSetting.setRound(round);
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewSetting;
  }

  public int getReviewRoundByAId(int aId) {
    String sql = "SELECT round FROM Review_Setting WHERE aId = ?";
    int reviewRound = 0;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, aId);
      rs =  preStmt.executeQuery();

      while (rs.next()) {
        reviewRound = rs.getInt("round");
      }
    } catch (SQLException e) {
      LOGGER.error(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.debug(e.toString());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return reviewRound;
  }


  /**
   * Get amount by aId
   *
   * @param aid assignment Id
   * @return amount reviewer amount
   */
  public int getReviewSettingAmountByAId(int aid) throws SQLException {
    String query = "SELECT amount FROM Review_Setting WHERE aId = ?";
    int amount = 0;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, aid);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        amount = rs.getInt("amount");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return amount;
  }

  /**
   * check is the assignment is assign as pair review
   *
   * @param assignmentName assignment name
   */
  public boolean checkAssignmentByAid(String assignmentName) throws SQLException {
    String query = "SELECT COUNT(*) AS count FROM Review_Setting AS rs, Assignment AS assign "
        + "WHERE rs.aId = assign.id AND assign.name = ?;";
    boolean exist = false;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;
    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setString(1, assignmentName);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int count = rs.getInt("count");
        if (count > 0) {
          exist = true;
        }
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return exist;
  }

  /**
   * delete review setting of specific assignment Id
   *
   * @param aid assignment Id
   */
  public void deleteReviewSettingByAId(int aid) throws SQLException {
    String query = "DELETE FROM Review_Setting WHERE aId = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, aid);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * delete review setting of specific Id
   *
   * @param id Id
   */
  public void deleteReviewSettingById(int id) throws SQLException {
    String query = "DELETE FROM Review_Setting WHERE id = ?";
    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
