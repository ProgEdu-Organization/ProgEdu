package fcu.selab.progedu.data;

import java.util.Date;

public class ReviewRecord {

  private int id;

  private int rrsId;

  private int rsmId;

  private int score;

  private Date time;

  private String feedback;

  private int teacherReview;

  private int reviewScore;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getRrsId() {
    return rrsId;
  }

  public void setRrsId(int rrsId) {
    this.rrsId = rrsId;
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

  public int getTeacherReview() {
    return this.teacherReview;
  }

  public void setTeacherReview(int teacherReview) {
    this.teacherReview = teacherReview;
  }

  public int getReviewScore() {
    return this.reviewScore;
  }

  public void setReviewScore(int reviewScore) {
    this.reviewScore = reviewScore;
  }

}
