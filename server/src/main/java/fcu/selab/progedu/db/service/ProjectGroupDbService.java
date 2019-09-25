package fcu.selab.progedu.db.service;

import java.util.List;

import fcu.selab.progedu.db.ProjectGroupDbManager;

public class ProjectGroupDbService {
  private static ProjectGroupDbService instance = new ProjectGroupDbService();

  public static ProjectGroupDbService getInstance() {
    return instance;
  }

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

}
