package fcu.selab.progedu.db.service;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.ScreenshotRecordDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import java.util.ArrayList;
import java.util.List;


public class UserDbService {
  private static UserDbService instance = new UserDbService();

  public static UserDbService getInstance() {
    return instance;
  }

  private UserDbManager udb = UserDbManager.getInstance();

  public int getGitLabId(String username) {
    return udb.getGitLabIdByUsername(username);
  }

  public int getGitLabId(int userId) {
    return getGitLabId( getName(userId) );
  }

  public int getId(String username) {
    return udb.getUserIdByUsername(username);
  }

  public String getName(int userId) {
    return udb.getUsername(userId);
  }

  /**
   * get that user's all groups
   *
   * @param userId user id
   */
  public List<Integer> getUserGroups(int userId) {
    GroupUserDbManager groupUserDbManager = GroupUserDbManager.getInstance();
    return groupUserDbManager.getGIds(userId);
  }

  /**
   * get that user's all AssignmentNames
   *
   * @param userId user id
   */
  public List<String> getUserAssignmentNames(int userId) {
    AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
    List<Integer> assignmentIds = assignmentUserDbManager.getAIds(userId);

    List<String> assignmentNames = new ArrayList<>();
    AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();

    //change id to name
    for ( int assignmentId : assignmentIds ) {
      String name = assignmentDbManager.getAssignmentNameById(assignmentId);
      assignmentNames.add(name);
    }

    return assignmentNames;
  }

  /**
   * Delete user
   *
   * @param userId user id
   */
  public void deleteUser(int userId) {

    // User -> Assignment_User -> Commit_Record -> Screenshot_Record
    AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
    CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();
    ScreenshotRecordDbManager srDbManager = ScreenshotRecordDbManager.getInstance();

    List<Integer> assignmentIds = assignmentUserDbManager.getIdListByUid(userId);
    for (int assignmentId : assignmentIds) {
      List<Integer> commitRecordIds = commitRecordDbManager.getCommitRecordId(assignmentId);
      for (int commitRecordId : commitRecordIds) {
        srDbManager.deleteScreenshotByCrid(commitRecordId);
      }
      commitRecordDbManager.deleteRecord(assignmentId);
    }
    assignmentUserDbManager.deleteAssignmentUserByUid(userId);


    // User -> Group_User
    GroupUserDbManager groupUserDb = GroupUserDbManager.getInstance();
    groupUserDb.removeByUserId(userId);


    // User -> Role_User
    RoleUserDbManager roleUserDb = RoleUserDbManager.getInstance();
    roleUserDb.deleteRoleUserByUserId(userId);


    udb.deleteUser(userId);
  }

}
