package fcu.selab.progedu.data;

import java.util.Date;

public class AssignmentTime{
  private int id;

  private int aId;

  private int aaId;

  private Date releaseTime;

  private Date deadline;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAId() {
    return aId;
  }

  public void setAId(int aId) {
    this.aId = aId;
  }

  public int getAaId() {
    return aaId;
  }

  public void setAaId(int aaId) {
    this.aaId = aaId;
  }

  public Date getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(Date releaseTime) {
    this.releaseTime = releaseTime;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

}
