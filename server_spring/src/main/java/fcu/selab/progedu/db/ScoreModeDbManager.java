package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.service.ScoreModeEnum;

public class ScoreModeDbManager {

  private static ScoreModeDbManager dbManager = new ScoreModeDbManager();

  public static ScoreModeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  /**
   * Get Score mode Id by score description
   *
   * @param scoreDesc score mode
   * @return scoreModeId score mode Id
   */
  public int getScoreModeIdByDesc(String scoreDesc) throws SQLException {
    String query = "SELECT id FROM Score_Mode WHERE mode = ?";
    int scoreModeId = 0;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, scoreDesc);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        scoreModeId = rs.getInt("id");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return scoreModeId;
  }

  /**
   * Get Score description by score mode Id
   *
   * @param scoreModeId score mode
   * @return scoreDesc score mode description
   */
  public ScoreModeEnum getScoreModeDescById(int scoreModeId) throws SQLException {
    String query = "SELECT mode FROM Score_Mode WHERE id = ?";
    String scoreMode = null;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, scoreModeId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        scoreMode = rs.getString("mode");
      }
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    ScoreModeEnum scoreModeEnum = ScoreModeEnum.getScoreModeEnum(scoreMode);
    return scoreModeEnum;
  }
}
