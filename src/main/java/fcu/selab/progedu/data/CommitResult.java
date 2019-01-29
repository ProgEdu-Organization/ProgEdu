package fcu.selab.progedu.data;

public class CommitResult {

  private int stuId;

  private String hw;

  private String status;

  private int commit;

  public int getStuId() {
    return stuId;
  }

  public void setStuId(int stuId) {
    this.stuId = stuId;
  }

  public String getHw() {
    return hw;
  }

  public void setHw(String hw) {
    this.hw = hw;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getCommit() {
    return commit;
  }

  public void setCommit(int commit) {
    this.commit = commit;
  }
}
