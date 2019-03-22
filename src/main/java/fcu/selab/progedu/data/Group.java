package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.List;

public class Group {
  private String groupName;

  private String masterId;

  private List<String> contributorId;

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getMaster() {
    return masterId;
  }

  public void setMaster(String master) {
    this.masterId = master;
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
