package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class ReviewStatusDbManager {

    private static ReviewStatusDbManager dbManager = new ReviewStatusDbManager();

    public static ReviewStatusDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewStatusDbManager.class);

}
