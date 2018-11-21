package fcu.selab.progedu.db;

import org.junit.Test;

public class TestCommitRecordDb {
  CommitRecordDbManager crdm = new CommitRecordDbManager();

  @Test
  public void testCommitRecordDb() {
    crdm.getCommitRecordStateCounts("OOP-HW1");
    crdm.getCommitRecordStateCounts("OOP-HW2");
    crdm.getCommitRecordStateCounts("OOP-HW3");
    crdm.getCommitRecordStateCounts("OOP-HW6");

  }
}
