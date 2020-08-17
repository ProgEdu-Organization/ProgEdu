package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class PairMatchingDbManager {

    private static PairMatchingDbManager dbManager = new PairMatchingDbManager();

    public static PairMatchingDbManager getInstance() { return dbManager; }

    private IDatabase database = new MySqlDatabase();

    private static final Logger LOGGER = LoggerFactory.getLogger(PairMatchingDbManager.class);

}
