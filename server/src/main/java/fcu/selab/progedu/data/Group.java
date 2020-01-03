package fcu.selab.progedu.data;

import java.util.List;

public class Group {
  private String groupName;
  private int id;
  private int gitlabId;
  private int leader;
  private List<User> members;
  private List<GroupProject> projects;

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

  public List<User> getMembers() {
    return members;
  }

  public void setMembers(List<User> members) {
    this.members = members;
  }

  public List<GroupProject> getProjects() {
    return projects;
  }

  public void setProjects(List<GroupProject> projects) {
    this.projects = projects;
  }

  public int getNumberOfUsers() {
    return members.size();
  }

  public boolean isNotMoreThanOneUser() {
    return (getNumberOfUsers() <= 1);
  }

}
