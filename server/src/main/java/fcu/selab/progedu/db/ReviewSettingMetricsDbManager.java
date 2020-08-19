package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentReviewMetricsDbManager {

    private static AssignmentReviewMetricsDbManager dbManager = new AssignmentReviewMetricsDbManager();

    public static AssignmentReviewMetricsDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentReviewMetricsDbManager.class);

    /**
     * Insert review setting metrics into db
     *
     * @param rsId              review setting Id
     * @param rmId              review metrics Id
     */
    public void insertReviewSettingMetrics(int rsId, int rmId) {

    }

    /**
     * Get review metrics from review setting metrics by specific assignment id
     *
     * @param rsId              review setting Id
     */

    /**
     * Delete review metrics from review setting metrics by specific assignment id
     *
     * @param rsId              review setting Id
     */

}
