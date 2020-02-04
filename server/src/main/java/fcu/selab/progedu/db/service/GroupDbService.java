package fcu.selab.progedu.db.service;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.data.User;
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
   * get group name
   *
   * @param id group id
   * @return name
   */
  public String getName(int id) {
    return gdb.getName(id);
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
   * get groups info by uid
   *
   * @param uid user id
   * @return groups
   */
  public List<Group> getGroups(int uid) {
    List<Integer> gids = gudb.getGIds(uid);
    List<Group> groups = new ArrayList<>();
    for (int gid : gids) {
      groups.add(gdb.getGroup(gid));
    }
    return groups;
  }

  /**
   * get group
   *
   * @param name group name
   * @return Group
   */
  public Group getGroup(String name) {
    Group group = gdb.getGroup(name);
    int gid = group.getId();
    List<User> members = new ArrayList<>();
    List<GroupProject> projects = new ArrayList<>();
    List<Integer> uids = gudb.getUids(gid);
    List<Integer> pgids = pgdb.getPgids(gid);

    for (int uid : uids) {
      members.add(udb.getUser(uid));
    }
    for (int pgid : pgids) {
      projects.add(pdb.getProject(pgid));
    }
    group.setMembers(members);
    group.setProjects(projects);
    return group;
  }

  /**
   * get group names
   *
   * @param uid user id
   * @return group names
   */
  public List<String> getGroupNames(int uid) {
    List<Integer> gids = gudb.getGIds(uid);
    List<String> groupNames = new ArrayList<>();
    for (int gid : gids) {
      String groupName = gdb.getGroup(gid).getGroupName();
      groupNames.add(groupName);
    }
    return groupNames;
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
   * remove group by name
   *
   * @param name group name
   */
  public void removeGroup(String name) {
    int gid = getId(name);
    List<Integer> pgids = pgdb.getPgids(gid);

    for (int pgid : pgids) {
      List<Integer> pcrids = pdb.getCommitRecordId(pgid);
      for (int pcrid : pcrids) { // Project_ScreenShot
        psrdb.deleteProjectScreenshot(pcrid);
      }
      pcrdb.deleteProjectRecord(pgid); // Project_Commit_Record
    }
    List<Integer> pids = pgdb.getPids(gid);
    pgdb.remove(gid); // Project_Group

    for (int pid : pids) {
      pdb.removeProject(pid); // Project
    }
    gudb.remove(gid); // Group_User
    gdb.remove(gid); // Group
  }

  /**
   * remove group by group id
   *
   * @param groupId group id
   */
  public void removeGroup(int groupId) {
    String groupName = getName(groupId);
    removeGroup(groupName);
  }


  /**
   * remove member
   *
   * @param name   group name
   * @param member remove member
   */
  public void removeMember(String name, String member) {
    int gid = gdb.getId(name);
    int uid = udb.getUserIdByUsername(member);
    gudb.remove(gid, uid);
  }

  /**
   * update leader
   *
   * @param name   group name
   * @param leader leader username
   */
  public void updateLeader(String name, String leader) {
    int uid = udb.getUserIdByUsername(leader);
    int gid = gdb.getId(name);
    gdb.updateLeader(gid, uid);
  }

  /**
   * update leader
   *
   * @param group group
   */
  public void updateLeader(Group group) {
    gdb.updateLeader(group.getId(), group.getLeader());
  }

}
