package fcu.selab.progedu.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDbManagerTest {

  @Test
  void getUserIdByUsername() {
    UserDbManager userDbManager = UserDbManager.getInstance();
    int id = userDbManager.getUserIdByUsername("sss3");
    System.out.println(id);
  }
}