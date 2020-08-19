package fcu.selab.progedu.service;

import fcu.selab.progedu.db.ReviewStatusDbManager;
import org.junit.Test;

public class ReviewStatusDbManagerTest {
  ReviewStatusDbManager reviewStatusDbManager = new ReviewStatusDbManager();

  @Test
  public void getReviewStatusIdByStatusTest() {
    int id = reviewStatusDbManager.getReviewStatusIdByStatus("uncompleted");
    System.out.println(id);
  }

  @Test
  public void getReviewStatusByIdTest() {
    ReviewStatusEnum reviewStatusEnum = reviewStatusDbManager.getReviewStatusById(3);
    System.out.println(reviewStatusEnum);
  }
}
