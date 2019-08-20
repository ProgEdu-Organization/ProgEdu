package fcu.selab.progedu.data;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class Assignment {

  private int id = 0;

  private String createTime = "";

  private String name = "";

  private String deadline = "";

  private String description = "";

  private boolean hasTemplate = false;

  private ProjectTypeEnum type;

  private String gitLabUrl = "";

  private String jenkinsUrl = "";

  private String testZipChecksum = "";

  private String testZipUrl = "";

  private String releaseTime = "";

  private boolean display = true;

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

  public boolean isHasTemplate() {
    return hasTemplate;
  }

  public void setHasTemplate(boolean hasTemplate) {
    this.hasTemplate = hasTemplate;
  }

  public ProjectTypeEnum getType() {
    return type;
  }

  public ProjectTypeEnum setType(ProjectTypeEnum type) {
    return this.type = type;
  }

  public String getGitLabUrl() {
    return gitLabUrl;
  }

  public void setGitLabUrl(String gitLabUrl) {
    this.gitLabUrl = gitLabUrl;
  }

  public String getJenkinsUrl() {
    return jenkinsUrl;
  }

  public void setJenkinsUrl(String jenkinsUrl) {
    this.jenkinsUrl = jenkinsUrl;
  }

  public String getTestZipChecksum() {
    return testZipChecksum;
  }

  public void setTestZipChecksum(String testZipChecksum) {
    this.testZipChecksum = testZipChecksum;
  }

  public String getTestZipUrl() {
    return testZipUrl;
  }

  public void setTestZipUrl(String testZipUrl) {
    this.testZipUrl = testZipUrl;
  }

  public String getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(String releaseTime) {
    this.releaseTime = releaseTime;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }
}
