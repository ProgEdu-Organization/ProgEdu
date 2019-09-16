package fcu.selab.progedu.data;

public class Group {
  private String groupName;
  private int groupId;
  private int groupGitLabId;
  private String leaderId;

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public int getGroupGitLabId() {
    return groupGitLabId;
  }

  public void setGroupGitLabId(int groupGitLabId) {
    this.groupGitLabId = groupGitLabId;
  }

  public String getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(String leaderId) {
    this.leaderId = leaderId;
  }
}
