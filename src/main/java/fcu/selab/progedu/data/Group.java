package fcu.selab.progedu.data;

import java.util.List;

public class Group {
  private String groupName;

  private String master;

  private List<String> contributor;

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }

  public List<String> getContributor() {
    return contributor;
  }

  public void setContributor(List<String> contributor) {
    this.contributor = contributor;
  }

}
