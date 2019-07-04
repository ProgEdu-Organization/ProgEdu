package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.List;

public class Group {
  private String groupName;
  private String projectName;
  private int groupId;
  private String leaderUsername;
  private List<String> contributorId;

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getLeaderUsername() {
    return leaderUsername;
  }

  public void setLeaderUsername(String leaderUid) {
    this.leaderUsername = leaderUid;
  }

  public List<String> getContributor() {
    return contributorId;
  }

  public void setContributor(List<String> contributorId) {
    this.contributorId = contributorId;
  }

  /**
   * add contirbutor to list
   * 
   * @param contributorId contirbutor's username
   */
  public void addContributor(String contributorId) {
    if (this.contributorId == null) {
      this.contributorId = new ArrayList<>();
    }

    this.contributorId.add(contributorId);
  }

}
