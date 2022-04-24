package fcu.selab.progedu.db;

import fcu.selab.progedu.data.AssignmentScore;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AssignmentScoreDbManager {

  private static AssignmentScoreDbManager dbManager = new AssignmentScoreDbManager();

  public static AssignmentScoreDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentScoreDbManager.class);

  /**
   * Add Assignment Score to database
   *
   * @param auid Assignment user id
   * @param score Score
   */
  public void addAssignmentScore(AssignmentScore assignmentScore) {
    String sql = "INSERT INTO ProgEdu.Assignment_Score (`auId`, `score`)  VALUES(?, ?)";
    Connection connection = null;
    PreparedStatement preStmt = null;

    try {
      connection = database.getConnection();
      preStmt = connection.prepareStatement(sql);

      preStmt.setInt(1, assignmentScore.getAuid());
      preStmt.setInt(2, assignmentScore.getScore());
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, connection);
    }
  }

}
