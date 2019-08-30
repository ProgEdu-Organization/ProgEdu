package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.List;

public class Group {
  private String groupName;
  private String projectName;

  private int groupId;
  private String leaderUsername;
  private List<String> contributors;

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

  public void setLeaderUsername(String leaderUsername) {
    this.leaderUsername = leaderUsername;
  }

  public List<String> getContributors() {
    return contributors;
  }

  public void setContributors(List<String> contributor) {
    this.contributors = contributor;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * add contirbutor to list
   * 
   * @param contributor contirbutor's username
   */
  public void addContributor(String contributor) {
    if (this.contributors == null) {
      this.contributors = new ArrayList<>();
    }

    this.contributors.add(contributor);
  }

}
