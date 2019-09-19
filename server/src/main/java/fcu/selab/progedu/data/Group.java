package fcu.selab.progedu.data;

public class Group {
  private String groupName;
  private int id;
  private int gitlabId;
  private int leader;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public int getLeader() {
    return leader;
  }

  public void setLeader(int leader) {
    this.leader = leader;
  }

  public int getGitlabId() {
    return gitlabId;
  }

  public void setGitlabId(int gitlabId) {
    this.gitlabId = gitlabId;
  }

}
