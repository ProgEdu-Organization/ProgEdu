package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

public class AssignmentTypeDbManager {

  private static AssignmentTypeDbManager dbManager = new AssignmentTypeDbManager();

  public static AssignmentTypeDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentTypeDbManager.class);

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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);


      preStmt.setString(1, typeName);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        typeId = rs.getInt("id");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
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

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(query);

      preStmt.setInt(1, typeId);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        typeName = rs.getString("name");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    ProjectTypeEnum typeEnum = ProjectTypeEnum.getProjectTypeEnum(typeName);
    return typeEnum;
  }

}
