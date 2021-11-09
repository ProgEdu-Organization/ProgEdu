package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CloseDBUtil {

  public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {

    try { rs.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
    try { stmt.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
    try { conn.close(); } catch (Exception e) { System.out.println(e.getMessage()); }

  }

  public static void closeAll(Statement stmt, Connection conn) {
    try { stmt.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
    try { conn.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
  }

  public static void closeAll(Connection conn) {
    try { conn.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
  }

}
