package fcu.selab.progedu.data;

import java.util.Date;

public class AssignmentTime{
  private int id;

  private int aId;

  private int aaId;

  private int round;

  private Date releaseTime;

  private Date deadline;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAid() {
    return aId;
  }

  public void setAid(int aId) {
    this.aId = aId;
  }

  public int getAaId() {
    return aaId;
  }

  public void steAaId(int aaId) {
    this.aaId = aaId;
  }

  public int getRound() {
    return round;
  }

  public void setRound(int round) {
    this.round = round;
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
