package fcu.selab.progedu.data;

public class Student {
  private String team;
  private boolean teamLeader;
  private String studentID;
  private String name;

  public void setTeam(String team) {
    this.team = team;
  }

  public void setTeamLeader(boolean teamLeader) {
    this.teamLeader = teamLeader;
  }

  public void setStudentID(String studentID) {
    this.studentID = studentID;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeam() {
    return team;
  }

  public boolean getTeamLeader() {
    return teamLeader;
  }

  public String getStudentID() {
    return studentID;
  }

  public String getName() {
    return name;
  }

}
