package fcu.selab.progedu.db.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.AssignmentTypeDbManager;
import fcu.selab.progedu.db.CommitStatusDbManager;
import fcu.selab.progedu.db.GroupDbManager;
import fcu.selab.progedu.db.ProjectCommitRecordDbManager;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.db.ProjectGroupDbManager;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.status.StatusEnum;

public class ProjectDbService {
  private static ProjectDbService dbService = new ProjectDbService();

  public static ProjectDbService getInstance() {
    return dbService;
  }

  private ProjectDbManager pdb = ProjectDbManager.getInstance();
  private ProjectCommitRecordDbManager pcrdb = ProjectCommitRecordDbManager.getInstance();
  private ProjectGroupDbManager pgdb = ProjectGroupDbManager.getInstance();
  private GroupDbManager gdb = GroupDbManager.getInstance();
//  private GroupUserDbManager gudb = GroupUserDbManager.getInstance();
  private AssignmentTypeDbManager atdb = AssignmentTypeDbManager.getInstance();
  private CommitStatusDbManager ctdb = CommitStatusDbManager.getInstance();

  /**
   * import project info to database
   * 
   * @param project   group project
   * @param groupName group name
   */
  public void addProject(GroupProject project, String groupName) {
    pdb.addProject(project);

    int pid = pdb.getId(project.getName());
    int gid = gdb.getId(groupName);
    pgdb.addProjectGroup(pid, gid);
  }

  public void removeProject(int id) {
    pdb.deleteProject(id);
  }

  /**
   * get all project names
   * 
   * @param groupName group name
   * @return all project names
   */
  public List<String> getProjectNames(String groupName) {
    List<String> projectNames = new ArrayList<>();
    int gid = gdb.getId(groupName);
    List<Integer> pids = pgdb.getPids(gid);

    for (int pid : pids) {
      String projectName = pdb.getProjectName(pid);
      projectNames.add(projectName);
    }

    return projectNames;
  }

  /**
   * get last commit record
   * 
   * @param pgid project_group id
   * @return last commit record
   */
  public CommitRecord getCommitResult(int pgid) {
    return pcrdb.getLastProjectCommitRecord(pgid);
  }

  /**
   * 
   * @param pgid Project_Group id
   * @param num  commit number
   * @return commitRecord
   */
  public StatusEnum getCommitRecordStatus(int pgid, int num) {
    int statusId = pcrdb.getProjectCommitRecordStatus(pgid, num);
    return ctdb.getStatusNameById(statusId);
  }
  
  /**
   * get part commit records
   * 
   * @param pgid project_group id
   * @return commit count
   */
  public int getCommitCount(int pgid) {
    return pcrdb.getProjectCommitCount(pgid);
  }

  /**
   * get commit records
   * 
   * @param pgid project_group id
   * @return commit records
   */
  public List<CommitRecord> getCommitRecords(int pgid) {
    return pcrdb.getProjectCommitRecords(pgid);
  }

  /**
   * get part commit records
   * 
   * @param pgid project_group id
   * @param currentPage current page
   * @return part commit records
   */
  public List<CommitRecord> getPartCommitRecords(int pgid,int currentPage) {
    return pcrdb.getPartProjectCommitRecords(pgid,currentPage);
  }

  /**
   * get all commit record id
   * 
   * @param pgid project_group id
   * @return all commit record id
   */
  public List<Integer> getCommitRecordId(int pgid) {
    return pcrdb.getProjectCommitRecordId(pgid);
  }

  /**
   * get commit record id
   * 
   * @param pgid project_group id
   * @param num  commit number
   * @return commit record id
   */
  public int getCommitRecordId(int pgid, int num) {
    return pcrdb.getProjectCommitRecordId(pgid, num);
  }

  /**
   * get group project
   * 
   * @param pgid project_group id
   * @return group project
   */
  public GroupProject getProject(int pgid) {
    int pid = pgdb.getPid(pgid);
    return pdb.getGroupProject(pid);
  }

  /**
   * get pgid
   * 
   * @param groupName   group name
   * @param projectName project name
   * @return project_group id
   */
  public int getPgid(String groupName, String projectName) {
    int gid = gdb.getId(groupName);
    int pid = pdb.getId(projectName);
    return pgdb.getId(gid, pid);
  }

  /**
   * get pgids
   * 
   * @param groupName group name
   * @return pgids
   */
  public List<Integer> getPgids(String groupName) {
    int gid = gdb.getId(groupName);
    return getPgids(gid);
  }

  private List<Integer> getPgids(int gid) {
    return pgdb.getPgids(gid);
  }

  /**
   * get project type
   * 
   * @param projectName project name
   * @return project type
   */
  public ProjectTypeEnum getProjectType(String projectName) {
    int typeId = pdb.getProjectType(projectName);
    return atdb.getTypeNameById(typeId);
  }

  /**
   * insert project commit record
   * 
   * @param pgId         project_group id
   * @param commitNumber commit number
   * @param status       status
   * @param time         time
   * @param committer    committer
   */
  public void insertProjectCommitRecord(int pgId, int commitNumber, StatusEnum status, Date time,
      String committer) {
    pcrdb.insertProjectCommitRecord(pgId, commitNumber, status, time, committer);
  }

//
//public List<GroupProject> getProjects(String groupName) {
//  List<GroupProject> gps = new ArrayList<>();
//  int gid = gdb.getId(groupName);
//  List<Integer> pids = pgdb.getPids(gid);
//
//  for (int pid : pids) {
//    pdb.getGroupProject(pid);
//  }
//
//  return null;
//}
}
