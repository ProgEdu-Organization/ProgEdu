package fcu.selab.progedu.db.service;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;

public class UserDbService {
  private static UserDbService instance = new UserDbService();

  public static UserDbService getInstance() {
    return instance;
  }

  private UserDbManager udb = UserDbManager.getInstance();

  public int getGitLabId(String username) {
    return udb.getGitLabIdByUsername(username);
  }

  public int getId(String username) {
    return udb.getUserIdByUsername(username);
  }
}
