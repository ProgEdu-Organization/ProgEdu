package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class RoleDbManager {

  private static RoleDbManager dbManager = new RoleDbManager();

  public static RoleDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleDbManager.class);

  /**
   * Get Role Id by role name
   * 
   * @param roleName role name
   * @return roleId role Id
   */
  public int getRoleIdByName(String roleName) {
    String query = "SELECT id FROM Role WHERE role = ?";
    int roleId = 0;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setString(1, roleName);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        roleId = rs.getInt("id");
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return roleId;
  }

  /**
   * Get Role name by status Id
   * 
   * @param roleId Id
   * @return statusEnum statusEnum
   */
  public RoleEnum getRoleNameById(int roleId) {
    String query = "SELECT role FROM Role WHERE id = ?";
    String roleName = null;
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, roleId);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        roleName = rs.getString("role");
      }
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    RoleEnum roleEnum = RoleEnum.getRoleEnum(roleName);
    return roleEnum;
  }

}
