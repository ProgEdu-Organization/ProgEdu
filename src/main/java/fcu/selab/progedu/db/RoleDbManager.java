package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.service.RoleEnum;

public class RoleDbManager {

  private static RoleDbManager dbManager = new RoleDbManager();

  public static RoleDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * Get Role Id by role name
   * 
   * @param roleName role name
   * @return roleId role Id
   */
  public int getRoleIdByName(String roleName) {
    String query = "SELECT id FROM Role WHERE role = ?";
    int roleId = 0;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, roleName);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          roleId = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
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

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, roleId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          roleName = rs.getString("role");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    RoleEnum roleEnum = RoleEnum.getRoleEnum(roleName);
    return roleEnum;
  }

}
