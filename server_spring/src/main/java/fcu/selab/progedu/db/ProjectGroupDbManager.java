package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class ProjectGroupDbManager {
  private static ProjectGroupDbManager dbManager = new ProjectGroupDbManager();

  public static ProjectGroupDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = MySqlDatabase.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectGroupDbManager.class);

  private ProjectGroupDbManager() {

  }

  /**
   * Add ProjectGroup to database
   * 
   * @param pid Project Id
   * @param gid Group Id
   */
  public void addProjectGroup(int pid, int gid) {
    String sql = "INSERT INTO Project_Group(pId, gId)  VALUES(?, ?)";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);


      preStmt.setInt(1, pid);
      preStmt.setInt(2, gid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

  /**
   * get pgId by project Id and group Id
   * 
   * @param pid Project Id
   * @param gid Group Id
   * @return pgId projectGroup Id
   */
  public int getId(int gid, int pid) {
    int pgid = 0;
    String sql = "SELECT id FROM Project_Group WHERE pId=? AND gId=?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pid);
      preStmt.setInt(2, gid);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        pgid = rs.getInt("id");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return pgid;
  }

  /**
   * get pgIds by group Id
   * 
   * @param gid Group Id
   * @return pgIds projectGroup Id
   */
  public List<Integer> getPgids(int gid) {
    List<Integer> pgids = new ArrayList<>();
    String sql = "SELECT id FROM Project_Group WHERE gId=?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;


    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, gid);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int pgid = rs.getInt("id");
        pgids.add(pgid);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }

    return pgids;
  }

  /**
   * get pids by Group Id
   * 
   * @return lsPids project Id
   */
  public List<Integer> getPids(int gid) {
    List<Integer> lsPids = new ArrayList<>();
    String sql = "SELECT pId FROM Project_Group WHERE gId = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {

      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, gid);

      rs = preStmt.executeQuery();
      while (rs.next()) {
        int pid = rs.getInt("pId");
        lsPids.add(pid);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return lsPids;
  }

  /**
   * get gId by Project Id
   *
   * @return lsGids Group Id
   */
  public List<Integer> getGids(int pid) {
    List<Integer> lsGids = new ArrayList<>();
    String sql = "SELECT gId FROM Project_Group WHERE pId = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, pid);
      rs = preStmt.executeQuery();
      while (rs.next()) {
        int gid = rs.getInt("gId");
        lsGids.add(gid);
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return lsGids;
  }

  /**
   * get pid by id
   * 
   * @param id id
   * @return pid
   */
  public int getPid(int id) {
    int gid = 0;
    String sql = "SELECT pId FROM Project_Group WHERE id = ?";

    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet rs = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, id);
      rs = preStmt.executeQuery();
      if (rs.next()) {
        gid = rs.getInt("pId");
      }

    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(rs, preStmt, conn);
    }
    return gid;
  }

  /**
   * remove Project_Group by gid
   * 
   * @param gid group id
   */
  public void remove(int gid) {
    String sql = "DELETE FROM Project_Group WHERE gId=?";

    Connection conn = null;
    PreparedStatement preStmt = null;

    try {
      conn = database.getConnection();
      preStmt = conn.prepareStatement(sql);

      preStmt.setInt(1, gid);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } finally {
      CloseDBUtil.closeAll(preStmt, conn);
    }
  }

}
