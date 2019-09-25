package fcu.selab.progedu.db.service;

import java.util.List;

import fcu.selab.progedu.db.GroupDbManager;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.db.ProjectGroupDbManager;

public class ProjectGroupDbService {
  private static ProjectGroupDbService instance = new ProjectGroupDbService();

  public static ProjectGroupDbService getInstance() {
    return instance;
  }

  private ProjectDbManager pdb = ProjectDbManager.getInstance();
  private GroupDbManager gdb = GroupDbManager.getInstance();
  private ProjectGroupDbManager pgdb = ProjectGroupDbManager.getInstance();

  public List<Integer> getPgids(int gid) {
    return pgdb.getPgids(gid);
  }

  public void remove(int gid) {
    pgdb.remove(gid);
  }

  public List<Integer> getPids(int gid) {
    return pgdb.getPids(gid);
  }

  public int getId(int gid, int pid) {
    return pgdb.getId(gid, pid);
  }

  /**
   * get pgid
   * 
   * @param groupName   group name
   * @param projectName project name
   * @return project_group id
   */
  public int getId(String groupName, String projectName) {
    int gid = gdb.getId(groupName);
    int pid = pdb.getId(projectName);
    return pgdb.getId(gid, pid);
  }

}
