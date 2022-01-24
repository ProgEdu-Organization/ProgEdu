package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Assignment {

  private int id = 0;

  private String name = "";

  private Date createTime = null;

  private Date deadline = null;

  private Date releaseTime = null;

  private String description = "";

  private ProjectTypeEnum type;

  private boolean display = true;

  private List<AssessmentTime> assessmentTimeList = new ArrayList<>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProjectTypeEnum getType() {
    return type;
  }

  public ProjectTypeEnum setType(ProjectTypeEnum type) {
    return this.type = type;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

  public List<AssessmentTime> getAssessmentTimeList() {
    return assessmentTimeList;
  }

  public void setAssessmentTimeList(List<AssessmentTime> assessmentTimeList) {
    this.assessmentTimeList = assessmentTimeList;
  }

}
