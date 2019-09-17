package fcu.selab.progedu.db.service;

import fcu.selab.progedu.db.GroupDbManager;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.UserDbManager;

public class GroupDbService {
  private static GroupDbService dbService = new GroupDbService();

  public static GroupDbService getInstance() {
    return dbService;
  }

  private GroupDbManager gdb = GroupDbManager.getInstance();
  private GroupUserDbManager gudb = GroupUserDbManager.getInstance();
  private UserDbManager udb = UserDbManager.getInstance();

  public void addGroup(int gitlabId, String groupName, String leaderUsername) {
    int leaderId = udb.getUserIdByUsername(leaderUsername);
    gdb.addGroup(gitlabId, groupName, leaderId);
  }

  /**
   * add member to group
   * 
   * @param username  username
   * @param groupName groupName
   */
  public void addMember(String username, String groupName) {
    int userId = udb.getUserIdByUsername(username);
    int groupId = gdb.getId(groupName);
    gudb.addGroupUser(groupId, userId);
  }

}
