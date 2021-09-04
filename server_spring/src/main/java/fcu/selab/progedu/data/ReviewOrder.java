package fcu.selab.progedu.data;

import fcu.selab.progedu.service.ReviewStatusEnum;

/**
 * Review Order.
 */
public class ReviewOrder {
  private int id;

  private int pmId;

  private ReviewStatusEnum reviewStatusEnum;

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

  public ReviewStatusEnum getReviewStatusEnum() {
    return reviewStatusEnum;
  }

  public void setReviewStatusEnum(ReviewStatusEnum reviewStatusEnum) {
    this.reviewStatusEnum = reviewStatusEnum;
  }

  public int getReviewOrder() {
    return reviewOrder;
  }

  public void setReviewOrder(int reviewOrder) {
    this.reviewOrder = reviewOrder;
  }
}
