package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignmentTypeDbManager {

  private static AssignmentTypeDbManager dbManager = new AssignmentTypeDbManager();

  public static AssignmentTypeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private AssignmentTypeDbManager() {

  }

  /**
   * add assignmentType into database
   * 
   * @param type Assignment_Type name
   */
  public void addAssignmentType(String type) {
    String sql = "INSERT INTO Assignment_Type(name)  VALUES( ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, type);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete assignmentType from database
   * 
   * @param type Assignment_Type name
   */
  public void deleteAssignmentType(String type) {
    String sql = "DELETE FROM Assignment_Type WHERE name ='" + type + "'";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, type);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get assignment type Id by name
   * 
   * @param name Assignment_Type name
   * @return type assignment type
   */
  public int getAssignmentTypeId(String name) {
    int id = 0;
    String sql = "SELECT * FROM Assignment_Type WHERE name=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, name);
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
   * get assignment type name by Id
   * 
   * @param id Assignment_Type id
   * @return name assignment name
   */
  public String getAssignmentTypeName(int id) {
    String typeName = null;
    String sql = "SELECT * FROM Assignment_Type WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          typeName = rs.getString("name");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typeName;
  }

}
