package fcu.selab.progedu.db.service;

import fcu.selab.progedu.db.GroupDbManager;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.ProjectDbManager;

public class GroupProjectDbService {
  private static GroupProjectDbService dbService = new GroupProjectDbService();

  public static GroupProjectDbService getInstance() {
    return dbService;
  }

  private GroupDbManager gdb = GroupDbManager.getInstance();
  private GroupUserDbManager gudb = GroupUserDbManager.getInstance();
  private ProjectDbManager udb = ProjectDbManager.getInstance();
}
