package fcu.selab.progedu.service;

public class CommitRecordTest {
  private static CommitRecordService CR = new CommitRecordService();

  public static void main(String[] args) {
    getAllUsersCommitRecord();
//    CommitRecordService crs = new CommitRecordService();
//    String username = "test02";
//    String assignmentName = "82905";
////    int number = 5;
////    for (int index = 1; index <= number; index++) {
////      if (index != 4) {
////        Response r = crs.getFeedback(username, assignmentName, index);
////        System.out.println(r.getEntity());
////      }
////
////    }
//
//    Response r = crs.getGitLabProjectUrl(username, assignmentName);
//    System.out.println(r.getEntity());
  }

  private static void getAllUsersCommitRecord() {
    System.out.println(CR.getAllUsersCommitRecord().getEntity());
  }

}
