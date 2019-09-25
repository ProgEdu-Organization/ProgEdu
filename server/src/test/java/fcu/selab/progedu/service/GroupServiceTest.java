package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.db.service.GroupDbService;

public class GroupServiceTest {
  private GroupService gs = new GroupService();
  private static final String GROUP_NAME = "group2";

  @Test
  public void createGroup() {

    String leader = "test01";
    List<String> members = new ArrayList<>();
    String m1 = "test03";
    String m2 = "test05";
    String m3 = "test06";

    String projectType = "web";
    String projectName = "project2";
    members.add(m1);
    members.add(m2);
    members.add(m3);
    gs.createGroup(GROUP_NAME, leader, members, projectType, projectName);
  }

  @Test
  public void removeGroup() {
    GroupDbService gdb = GroupDbService.getInstance();
    List<Group> groups = gdb.getGroups();
    for (Group group : groups) {
      gs.removeGroup(group.getGroupName());
    }

  }

}
