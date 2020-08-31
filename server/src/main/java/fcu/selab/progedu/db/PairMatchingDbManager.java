package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.service.ReviewStatusEnum;
import org.json.JSONArray;
import org.json.JSONObject;

public class PairMatchingDbManager {

  private static PairMatchingDbManager dbManager = new PairMatchingDbManager();

  private static ReviewStatusDbManager rsDb = ReviewStatusDbManager.getInstance();

  public static PairMatchingDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * Insert pair matching into db
   *
   * @param auId     assignment Id
   * @param reviewId user Id
   * @param status   status
   */
  public void insertPairMatching(int auId, int reviewId, ReviewStatusEnum status)
      throws SQLException {
    String query = "INSERT INTO Pair_Matching(auId, reviewId, status) VALUES(?,?,?)";
    int statusId = rsDb.getReviewStatusIdByStatus(status.getTypeName());

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      preStmt.setInt(2, reviewId);
      preStmt.setInt(3, statusId);
      preStmt.executeUpdate();
    }
  }

  /**
   * Insert pair matching list into db
   *
   * @param pairMatchingList     pair matching list
   *
   */
  public void insertPairMatchingList(List<PairMatching> pairMatchingList) throws SQLException {
    for (PairMatching pairMatching: pairMatchingList) {
      insertPairMatching(pairMatching.getAuId(), pairMatching.getReviewId(),
          pairMatching.getReviewStatusEnum());
    }
  }

  /**
   * Get all pair matching
   *
   * @return pair matching details
   */
  public List<PairMatching> getAllPairMatching() throws SQLException {
    String query = "SELECT * FROM Pair_Matching";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int auId = rs.getInt("auId");
          int reviewId = rs.getInt("reviewId");
          ReviewStatusEnum status = rsDb.getReviewStatusById(rs.getInt("status"));
          PairMatching pairMatching = new PairMatching();
          pairMatching.setId(id);
          pairMatching.setAuId(auId);
          pairMatching.setReviewId(reviewId);
          pairMatching.setReviewStatusEnum(status);
          pairMatchingList.add(pairMatching);
        }
      }
    }
    return pairMatchingList;
  }

  /**
   * Get pair matching By id
   *
   * @param id pair matching id
   * @return pair matching details
   */
  public PairMatching getPairMatchingById(int id) throws SQLException {
    String query = "SELECT * FROM Pair_Matching WHERE id = ?";
    PairMatching pairMatching = new PairMatching();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int auId = rs.getInt("auId");
          int reviewId = rs.getInt("reviewId");
          ReviewStatusEnum status = rsDb.getReviewStatusById(rs.getInt("status"));
          pairMatching.setId(id);
          pairMatching.setAuId(auId);
          pairMatching.setReviewId(reviewId);
          pairMatching.setReviewStatusEnum(status);
        }
      }
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
  public List<PairMatching> getPairMatchingByAuId(int auId) throws SQLException {
    String query = "SELECT * FROM Pair_Matching WHERE auId = ?";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int reviewId = rs.getInt("reviewId");
          ReviewStatusEnum status = rsDb.getReviewStatusById(rs.getInt("status"));
          PairMatching pairMatching = new PairMatching();
          pairMatching.setId(id);
          pairMatching.setAuId(auId);
          pairMatching.setReviewId(reviewId);
          pairMatching.setReviewStatusEnum(status);
          pairMatchingList.add(pairMatching);
        }
      }
    }
    return pairMatchingList;
  }

  /**
   * Get reviewId which had existed in pair matching by specific assignment user id
   *
   * @param auId assignment user id
   * @return integer list
   */
  public List<Integer> getReviewListByAuId(int auId) throws SQLException {
    String query = "SELECT reviewId FROM Pair_Matching WHERE auId = ?";
    List<Integer> integerList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          integerList.add(rs.getInt("reviewId"));
        }
      }
    }
    return integerList;
  }

  /**
   * Get pair matching By assignment id and reviewer id
   * Know the status by specific assignment and reviewer
   *
   * @param aid assignment id
   * @param reviewId user id
   */
  public List<PairMatching> getPairMatchingByAidAndReviewId(int aid, int reviewId)
      throws SQLException {
    String query = "SELECT pm.id, pm.auId, pm.reviewId, pm.status FROM "
        + "Pair_Matching AS pm, Assignment_User AS au "
        + "WHERE au.id = pm.auId AND au.aId = ? AND pm.reviewId = ?;";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, aid);
      preStmt.setInt(2, reviewId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int auId = rs.getInt("auId");
          ReviewStatusEnum status = rsDb.getReviewStatusById(rs.getInt("status"));
          PairMatching pairMatching = new PairMatching();
          pairMatching.setId(id);
          pairMatching.setAuId(auId);
          pairMatching.setReviewId(reviewId);
          pairMatching.setReviewStatusEnum(status);
          pairMatchingList.add(pairMatching);
        }
      }
    }
    return pairMatchingList;
  }

  /**
   * Get pair matching By reviewer id
   * Know what assignment had been reviewed by specific reviewer
   *
   * @param reviewId user id
   * @return pair matching details
   */
  public List<PairMatching> getPairMatchingByReviewId(int reviewId) throws SQLException {
    String query = "SELECT * FROM Pair_Matching WHERE reviewId = ?";
    List<PairMatching> pairMatchingList = new ArrayList<>();

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, reviewId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int auId = rs.getInt("auId");
          ReviewStatusEnum status = rsDb.getReviewStatusById(rs.getInt("status"));
          PairMatching pairMatching = new PairMatching();
          pairMatching.setId(id);
          pairMatching.setAuId(auId);
          pairMatching.setReviewId(reviewId);
          pairMatching.setReviewStatusEnum(status);
          pairMatchingList.add(pairMatching);
        }
      }
    }
    return pairMatchingList;
  }

  /**
   * Delete pair matching by id
   *
   * @param id pair matching id
   */
  public void deletePairMatchingById(int id) throws SQLException {
    String query = "DELETE FROM Pair_Matching WHERE id = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.executeUpdate();
    }
  }

  /**
   * Delete pair matching by assignment user id
   * Use this method when delete hw
   *
   * @param auId assignment user id
   */
  public void deletePairMatchingByAuId(int auId) throws SQLException {
    String query = "DELETE FROM Pair_Matching WHERE auId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, auId);
      preStmt.executeUpdate();
    }
  }

  /**
   * Delete pair matching by reviewer id
   * Use this method when delete user
   *
   * @param reviewId reviewer id
   */
  public void deletePairMatchingByReviewId(int reviewId) throws SQLException {
    String query = "DELETE FROM Pair_Matching WHERE reviewId = ?";

    try (Connection conn = database.getConnection();
         PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, reviewId);
      preStmt.executeUpdate();
    }
  }

}
