package fcu.selab.progedu.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDatabaseTest {

  @Test
  void getConnection() {
    MySqlDatabase mySqlDatabase = MySqlDatabase.getInstance();
    Connection conn = null;
    conn = mySqlDatabase.getConnection();
    try { conn.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
  }
}