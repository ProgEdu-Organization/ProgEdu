package fcu.selab.progedu.data;

import java.util.Date;

public class ReviewSetting {

  private int id;

  private int aid;

  private int amount;

  private Date releaseTime;

  private Date deadline;

  private int round;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getaId() {
    return aid;
  }

  public void setaId(int aid) {
    this.aid = aid;
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

  public int getRound() {
    return round;
  }

  public void setRound(int round) {
    this.round = round;
  }
}
