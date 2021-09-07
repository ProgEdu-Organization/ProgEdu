package fcu.selab.progedu.data;

import fcu.selab.progedu.service.AssessmentActionEnum;

import java.util.Date;

public class AssessmentTime {
  private int id;

  private int aId;

  private AssessmentActionEnum assessmentActionEnum;

  private Date startTime;

  private Date endTime;

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

  public AssessmentActionEnum getAssessmentActionEnum() {
    return assessmentActionEnum;
  }

  public void setAssessmentActionEnum(AssessmentActionEnum actionEnum) {
    this.assessmentActionEnum = actionEnum;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
