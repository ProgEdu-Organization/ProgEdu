package fcu.selab.progedu.db.service;

import java.util.List;

import fcu.selab.progedu.data.Group;
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

  /**
   * add group information to db
   * 
   * @param gitlabId       gitlab id of group
   * @param groupName      group name
   * @param leaderUsername leader's username
   */
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

  /**
   * get gitlab id
   * 
   * @param name group name
   * @return gitlab id
   */
  public int getGitlabId(String name) {
    return gdb.getGitlabId(name);
  }

  /**
   * get id
   * 
   * @param name group name
   * @return id
   */
  public int getId(String name) {
    return gdb.getId(name);
  }

  public List<Group> getGroups() {
    return gdb.getGroups();
  }

}
