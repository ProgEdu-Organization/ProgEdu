package fcu.selab.progedu.service;

import java.util.List;
import fcu.selab.progedu.data.AssignmentUser;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import org.junit.Test;

public class AssignmentServiceTest {
  AssignmentService assignmentService = AssignmentService.getInstance();
  AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();

  @Test
  public void test() {
    assignmentService.randomPairMatching(2, "MAVENTEST");
//    List<AssignmentUser> assignmentUserList = assignmentUserDbManager.getAssignmentUserListByAid(1);

//    for (AssignmentUser assignmentUser: assignmentUserList) {
//      System.out.println(assignmentUser.getId());
//    }
  }
}
