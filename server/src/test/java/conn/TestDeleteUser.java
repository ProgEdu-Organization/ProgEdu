package conn;

import org.junit.Test;

import fcu.selab.progedu.conn.GitlabService;

public class TestDeleteUser {
  GitlabService conn = GitlabService.getInstance();

  public void testDeleteUser() {
    for (int i = 183; i <= 188; i++) {
      conn.deleteUser(i);
    }
  }
}
