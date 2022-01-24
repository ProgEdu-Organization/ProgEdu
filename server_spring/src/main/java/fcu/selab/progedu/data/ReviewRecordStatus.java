package fcu.selab.progedu.data;

import fcu.selab.progedu.service.ReviewStatusEnum;

public class ReviewRecordStatus {
  private int id;

  private int pmId;

  private ReviewStatusEnum reviewStatusEnum;

  private int round;

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

  public ReviewStatusEnum getReviewStatusEnum() {
    return reviewStatusEnum;
  }

  public void setReviewStatusEnum(ReviewStatusEnum reviewStatusEnum) {
    this.reviewStatusEnum = reviewStatusEnum;
  }

  public int getRound() {
    return round;
  }

  public void setRound(int round) {
    this.round = round;
  }
}
