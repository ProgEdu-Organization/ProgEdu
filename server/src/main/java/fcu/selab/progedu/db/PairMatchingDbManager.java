package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.service.ReviewStatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PairMatchingDbManager {

  private static PairMatchingDbManager dbManager = new PairMatchingDbManager();

  private static ReviewStatusDbManager rsDb = ReviewStatusDbManager.getInstance();

  public static PairMatchingDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private static final Logger LOGGER = LoggerFactory.getLogger(PairMatchingDbManager.class);

  /**
   * Insert pair matching into db
   *
   * @param auId     assignment Id
   * @param reviewId user Id
   * @param status   status
   */
  public void insertPairMatching(int auId, int reviewId, ReviewStatusEnum status) {
    String query = "INSERT INTO Pair_Matching(auId, reviewId, status) VALUES(?,?,?)";
    int statusId = rsDb.getReviewStatusIdByStatus(status.getTypeName());

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Get all pair matching
   *
   * @return pair matching details
   */
  public List<PairMatching> getAllPairMatching() {
    String query = "";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return pairMatchingList;
  }

  /**
   * Get pair matching By id
   *
   * @param id pair matching id
   * @return pair matching details
   */
  public PairMatching getPairMatchingById(int id) {
    String query = "";
    PairMatching pairMatching = new PairMatching();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return pairMatching;
  }

  /**
   * Get pair matching By assignment user id
   * Know who reviewed this assignment by specific user
   *
   * @param auId assignment user id
   * @return pair matching details
   */
  public List<PairMatching> getPairMatchingByAuId(int auId) {
    String query = "";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return pairMatchingList;
  }

  /**
   * Get pair matching By reviewer id
   * Know what assignment had been reviewed by specific reviewer
   *
   * @param reviewId assignment user id
   * @return pair matching details
   */
  public List<PairMatching> getPairMatchingByReviewId(int reviewId) {
    String query = "";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return pairMatchingList;
  }

  /**
   * Delete pair matching by id
   *
   * @param id pair matching id
   */
  public void deletePairMatchingById(int id) {

  }

  /**
   * Delete pair matching by assignment user id
   * Use this method when delete hw
   *
   * @param auId assignment user id
   */
  public void deletePairMatchingByAuId(int auId) {

  }

  /**
   * Delete pair matching by reviewer id
   * Use this method when delete user
   *
   * @param reviewId reviewer id
   */
  public void deletePairMatchingByReviewId(int reviewId) {

  }

}
