package fcu.selab.progedu.data;

public class Project {

  private String createTime = "";

  private String name = "";

  private String deadline = "";

  private String description = "";

  private boolean hasTemplate = false;

  private String type = "";

  private String gitLabUrl = "";

  private String jenkinsUrl = "";

  private String testZipChecksum = "";

  private String testZipUrl = "";

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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
}
