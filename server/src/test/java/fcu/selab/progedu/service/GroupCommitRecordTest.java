package fcu.selab.progedu.service;

public class GroupCommitRecordTest {
  private static final String GROUP_NAME = "group1";
  private static final String PROJECT_NAME = "p1";

  public static void main(String[] args) {
    update();
  }

  private static void update() {
    GroupCommitRecordService gcr = new GroupCommitRecordService();

    gcr.updateCommitRecord(GROUP_NAME, PROJECT_NAME);
  }

}
