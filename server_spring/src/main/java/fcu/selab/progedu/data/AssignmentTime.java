package fcu.selab.progedu.data;

import fcu.selab.progedu.service.AssignmentActionEnum;

import java.util.Date;

public class AssignmentTime{
  private int id;

  private int aId;

  private int aaId;

  private Date releaseTime;

  private Date deadline;

  private AssignmentActionEnum actionEnum;

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

  public AssignmentActionEnum getActionEnum() {
    return actionEnum;
  }

  public void setActionEnum(AssignmentActionEnum actionEnum) {
    this.actionEnum = actionEnum;
  }
}
