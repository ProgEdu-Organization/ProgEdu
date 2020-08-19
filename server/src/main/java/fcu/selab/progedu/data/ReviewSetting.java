package fcu.selab.progedu.data;

import java.util.Date;

public class ReviewSetting {

  private int id;

  private int aId;

  private int amount;

  private Date releaseTime;

  private Date deadline;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getaId() {
    return aId;
  }

  public void setaId(int aId) {
    this.aId = aId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public Date getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(Date releaseTime) {
    this.releaseTime = releaseTime;
  }
}
