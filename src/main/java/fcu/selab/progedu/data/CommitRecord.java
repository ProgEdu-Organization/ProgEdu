package fcu.selab.progedu.data;

public class CommitRecord {

  private int id;

  private int auId;

  private int status;

  private int commitNumber;

  public int getStuId() {
    return id;
  }

  public void id(int id) {
    this.id = id;
  }

  public int getAuId() {
    return auId;
  }

  public void setAuId(int auId) {
    this.auId = auId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getCommitNumber() {
    return commitNumber;
  }

  public void setCommitNumber(int commitNumber) {
    this.commitNumber = commitNumber;
  }
}
