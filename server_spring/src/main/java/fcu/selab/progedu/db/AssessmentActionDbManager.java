package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.service.AssessmentActionEnum;


public class AssessmentActionDbManager {
  private static AssessmentActionDbManager dbManager = new AssessmentActionDbManager();

  public static AssessmentActionDbManager getInstance() {
    return dbManager;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentActionDbManager.class);

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   *
   * @param id
   * @return
   * @throws SQLException
   */
  public AssessmentActionEnum getAssessmentActionById(int id) {
    String query = "SELECT action FROM Assessment_Action WHERE `id` = ?";
    String assignmentAction = null;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, id);
      rs = preStmt.executeQuery();

      while (rs.next()) {
        assignmentAction = rs.getString("action");
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    AssessmentActionEnum assignmentActionEnum = AssessmentActionEnum.getActionEnum(assignmentAction);
    return assignmentActionEnum;
  }

  /**
   *
   * @param action
   * @return assessment action id
   */
  public int getAssessmentActionIdByAction(String action) {
    String query = "SELECT id FROM Assessment_Action WHERE `action` = ?";
    int id = 0;

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);
      preStmt.setString(1, action);

      rs = preStmt.executeQuery();
      while (rs.next()) {
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
}
