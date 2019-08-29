package fcu.selab.progedu.service;

public class AssignmentServiceTest {

  public static void main(String[] args) {
    AssignmentService as = new AssignmentService();
//    as.createAssignment(assignmentName, releaseTime, deadline, readMe, assignmentType, file,
//        fileDetail);
    String assignmentName = "82904";
    as.getProject(assignmentName);
  }

}
