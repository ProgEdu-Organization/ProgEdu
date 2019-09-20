package fcu.selab.progedu.service;

public class GroupProjectTest {

  public static void main(String[] args) {
    GroupProjectService gs = new GroupProjectService();
    String groupName = "group1";
    String projectName = "p1";
//    String leader = "test01";
    String projectType = "maven";
    gs.createGroupProject(groupName, projectName, projectType);

  }

}
