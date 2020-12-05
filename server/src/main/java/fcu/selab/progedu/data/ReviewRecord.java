package fcu.selab.progedu.data;

import java.util.Date;

public class ReviewRecord {

  private int id;

  private int pmId;

  private int rsmId;

  private int score;

  private Date time;

  private String feedback;

  private int reviewOrder;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPmId() {
    return pmId;
  }

  public void setPmId(int pmId) {
    this.pmId = pmId;
  }

  public int getRsmId() {
    return rsmId;
  }

  public void setRsmId(int rsmId) {
    this.rsmId = rsmId;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public int getReviewOrder() {
    return reviewOrder;
  }

  public void setReviewOrder(int reviewOrder) {
    this.reviewOrder = reviewOrder;
  }
}
