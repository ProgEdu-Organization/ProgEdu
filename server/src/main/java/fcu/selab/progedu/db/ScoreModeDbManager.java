package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.service.ScoreModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class ScoreModeDbManager {

    private static ScoreModeDbManager dbManager = new ScoreModeDbManager();

    public static ScoreModeDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreModeDbManager.class);

    /**
     * Get Score mode Id by score description
     *
     * @param scoreDesc score mode
     * @return scoreModeId score mode Id
     */
    public int getScoreModeIdByDesc(String scoreDesc) {
        String query = "SELECT id FROM Score_Mode WHERE mode = ?";
        int scoreModeId = 0;

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setString(1, scoreDesc);
            try (ResultSet rs = preStmt.executeQuery();) {
                while (rs.next()) {
                    scoreModeId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return scoreModeId;
    }

    /**
     * Get Score description by score mode Id
     *
     * @param scoreModeId score mode
     * @return scoreDesc score mode description
     */
    public ScoreModeEnum getScoreModeDescById(int scoreModeId) {
        String query = "SELECT mode FROM Score_Mode WHERE id = ?";
        String scoreMode = null;

        try (Connection conn = database.getConnection();
             PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, scoreModeId);
            try (ResultSet rs = preStmt.executeQuery();) {
                while (rs.next()) {
                    scoreMode = rs.getString("mode");
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        ScoreModeEnum scoreModeEnum = ScoreModeEnum.getScoreModeEnum(scoreMode);
        return scoreModeEnum;
    }
}
