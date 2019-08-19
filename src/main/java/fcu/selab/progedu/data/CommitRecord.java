package fcu.selab.progedu.data;

import fcu.selab.progedu.status.StatusEnum;

public class CommitRecord {

  private int id;

  private int auId;

  private StatusEnum status;

  private int commitNumber;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAuId() {
    return auId;
  }

  public void setAuId(int auId) {
    this.auId = auId;
  }

  public int getCommitNumber() {
    return commitNumber;
  }

  public void setCommitNumber(int commitNumber) {
    this.commitNumber = commitNumber;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }
}
