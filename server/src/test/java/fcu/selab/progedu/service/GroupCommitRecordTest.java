package fcu.selab.progedu.service;

public class GroupCommitRecordTest {
  private static final String GROUP_NAME = "group1";
  private static final String PROJECT_NAME = "p1";
  private static final GroupCommitRecordService GCR = new GroupCommitRecordService();

  public static void main(String[] args) {
//    update();
//    getResult();
    getAllGroupCommitRecord();
  }

  private static void update() {

    GCR.updateCommitRecord(GROUP_NAME, PROJECT_NAME);
  }

  private static void getResult() {
    System.out.println(GCR.getResult(GROUP_NAME).getEntity());
  }

  private static void getAllGroupCommitRecord() {
    System.out.println(GCR.getAllGroupCommitRecord().getEntity());
  }

}
