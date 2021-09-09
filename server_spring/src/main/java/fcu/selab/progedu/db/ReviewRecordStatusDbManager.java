package fcu.selab.progedu.db;

import fcu.selab.progedu.data.ReviewRecordStatus;
import fcu.selab.progedu.service.ReviewStatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewRecordStatusDbManager {

  private static ReviewRecordStatusDbManager dbManager = new ReviewRecordStatusDbManager();

  public static ReviewRecordStatusDbManager getInstance() {
    return dbManager;
  }

  public IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRecordStatusDbManager.class);

  private ReviewRecordStatusDbManager() {

  }

  private ReviewStatusDbManager rsDbManager = ReviewStatusDbManager.getInstance();

  public ReviewRecordStatus getReviewRecordStatusByPairMatchingIdAndRound(int pmId, int round) {
    String sql = "SELECT RRS.* FROM ProgEdu.Pair_Matching AS PM, ProgEdu.Review_Record_Status AS RRS WHERE PM.id = RRS.pmId AND PM.id = ? AND RRS.round = ?;";
    ReviewRecordStatus reviewRecordStatus = new ReviewRecordStatus();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = database.getConnection();
      stmt = conn.prepareStatement(sql);

      stmt.setInt(1, pmId);
      stmt.setInt(2, round);
      rs = stmt.executeQuery();
      while (rs.next()) {
        reviewRecordStatus.setId(rs.getInt("id"));
        reviewRecordStatus.setPmId(pmId);
        reviewRecordStatus.setReviewStatusEnum(rsDbManager.getReviewStatusById(rs.getInt("status")));
        reviewRecordStatus.setRound(round);
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, stmt, conn);
    }
    return reviewRecordStatus;
  }

}
