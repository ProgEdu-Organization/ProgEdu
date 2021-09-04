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
import fcu.selab.progedu.service.AssignmentActionEnum;

public class AssignmentActionDbManager {
  private static AssignmentActionDbManager dbManager = new AssignmentActionDbManager();

  public static AssignmentActionDbManager getInstance() {
      return dbManager;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentActionDbManager.class);

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   *
   * @param id
   * @return
   * @throws SQLException
   */
  public AssignmentActionEnum getAssignmentActionById(int id) throws SQLException {
    String query = "SELECT action FROM Assignment_Action WHERE id = ?";
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
    AssignmentActionEnum assignmentActionEnum = AssignmentActionEnum.getAssignmentActionEnum(assignmentAction);
    return assignmentActionEnum;
  }


  /**
   *
   * @param action
   * @return
   * @throws SQLException
   */
  public int getAssignmentActionIdByAction(String action) {
    String query = "SELECT id FROM Assignment_Action WHERE action = ?";
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
