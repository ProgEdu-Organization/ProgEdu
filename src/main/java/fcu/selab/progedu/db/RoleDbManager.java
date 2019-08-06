package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDbManager {

  private static RoleDbManager dbManager = new RoleDbManager();

  public static RoleDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * add Role into database
   * 
   * @param role Role
   */
  public void addRole(String role) {
    String sql = "INSERT INTO Role(role)  VALUES( ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, role);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete Role from database
   * 
   * @param role Role
   */
  public void deleteAssignmentType(String role) {
    String sql = "DELETE FROM Role WHERE role ='" + role + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, role);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Role id by Role name
   * 
   * @param role Role Name
   * @return id Role id
   */
  public int getRoleId(String role) {
    int id = 0;
    String sql = "SELECT * FROM Role WHERE role=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, role);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * get Role name by Role id
   * 
   * @param id Role Id
   * @return role Role name
   */
  public String getRoleName(int id) {
    String role = null;
    String sql = "SELECT * FROM Role WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          role = rs.getString("role");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return role;
  }

}
