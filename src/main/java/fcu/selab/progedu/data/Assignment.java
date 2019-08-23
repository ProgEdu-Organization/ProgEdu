package fcu.selab.progedu.data;

import java.util.Date;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class Assignment {

  private int id = 0;

  private String name = "";

  private Date createTime = null;

  private Date deadline = null;

  private Date releaseTime = null;

  private String description = "";

  private boolean hasTemplate = false;

  private ProjectTypeEnum type;

  private String gitLabUrl = "";

  private String jenkinsUrl = "";

  private long testZipChecksum = 0;

  private String testZipUrl = "";

  private boolean display = true;

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

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
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

  public long getTestZipChecksum() {
    return testZipChecksum;
  }

  public void setTestZipChecksum(long testZipChecksum) {
    this.testZipChecksum = testZipChecksum;
  }

  public String getTestZipUrl() {
    return testZipUrl;
  }

  public void setTestZipUrl(String testZipUrl) {
    this.testZipUrl = testZipUrl;
  }

  public Date getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(Date releaseTime) {
    this.releaseTime = releaseTime;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }
}
