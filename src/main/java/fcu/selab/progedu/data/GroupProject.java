package fcu.selab.progedu.data;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class GroupProject {
  private int id = 0;

  private String createTime = "";

  private String name = "";

  private String deadline = "";

  private String description = "";

  private ProjectTypeEnum type;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeadline() {
    return deadline;
  }

  public void setDeadline(String deadline) {
    this.deadline = deadline;
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

}
