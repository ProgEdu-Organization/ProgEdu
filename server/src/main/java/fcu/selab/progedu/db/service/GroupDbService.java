package fcu.selab.progedu.db.service;

import java.util.List;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.db.GroupDbManager;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.ProjectCommitRecordDbManager;
import fcu.selab.progedu.db.ProjectScreenshotRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;

public class GroupDbService {
  private static GroupDbService dbService = new GroupDbService();

  public static GroupDbService getInstance() {
    return dbService;
  }

  private GroupDbManager gdb = GroupDbManager.getInstance();

  private ProjectDbService pdb = ProjectDbService.getInstance();
  private ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();

  private ProjectCommitRecordDbManager pcrdb = ProjectCommitRecordDbManager.getInstance();
  private ProjectScreenshotRecordDbManager psrdb = ProjectScreenshotRecordDbManager.getInstance();
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

  /**
   * get all groups
   * 
   * @return all groups
   */
  public List<Group> getGroups() {
    return gdb.getGroups();
  }

  /**
   * get team leader username
   * 
   * @param groupName group name
   * @return leader username
   */
  public String getLeader(String groupName) {
    int leaderId = gdb.getLeader(groupName);
    return udb.getUsername(leaderId);

  }

  /**
   * delete Assignment from Database by name
   * 
   * @param groupName group name
   */
  public void removeGroup(String name) {
    int gid = gdb.getId(name);
    List<Integer> pgids = pgdb.getPgids(gid);

    for (int pgid : pgids) {
      List<Integer> pcrids = pdb.getCommitRecordId(pgid);
      for (int pcrid : pcrids) { // Project_ScreenShot
        psrdb.deleteProjectScreenshot(pcrid);
      }
      pcrdb.deleteProjectRecord(pgid);// Project_Commit_Record
    }
    List<Integer> pids = pgdb.getPids(gid);
    pgdb.remove(gid);// Project_Group

    for (int pid : pids) {
      pdb.removeProject(pid);// Project
    }
    gudb.remove(gid);// Group_User
    gdb.remove(gid);// Group

  }

}
