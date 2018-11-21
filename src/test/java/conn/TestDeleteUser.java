package conn;

import org.junit.Test;

import fcu.selab.progedu.conn.Conn;

public class TestDeleteUser {
  Conn conn = Conn.getInstance();

  public void testDeleteUser() {
    for (int i = 183; i <= 188; i++) {
      conn.deleteUser(i);
    }
  }
}
