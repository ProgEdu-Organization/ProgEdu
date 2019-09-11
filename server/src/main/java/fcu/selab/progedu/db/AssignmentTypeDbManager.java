package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class AssignmentTypeDbManager {

  private static AssignmentTypeDbManager dbManager = new AssignmentTypeDbManager();

  public static AssignmentTypeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentTypeDbManager() {

  }

  /**
   * Get assignment type Id by type name
   * 
   * @param typeName type name
   * @return typeId type id
   */
  public int getTypeIdByName(String typeName) {
    String query = "SELECT id FROM Assignment_Type WHERE name = ?";
    int typeId = 0;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, typeName);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          typeId = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typeId;
  }

  /**
   * Get assignment type name by type Id
   * 
   * @param typeId type name
   * @return type id
   */
  public ProjectTypeEnum getTypeNameById(int typeId) {
    String query = "SELECT name FROM Assignment_Type WHERE id = ?";
    String typeName = null;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, typeId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          typeName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ProjectTypeEnum typeEnum = ProjectTypeEnum.getProjectTypeEnum(typeName);
    return typeEnum;
  }

}
