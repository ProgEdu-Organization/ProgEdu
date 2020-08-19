package fcu.selab.progedu.service;

import java.util.List;
import fcu.selab.progedu.data.ReviewRecord;
import fcu.selab.progedu.db.ReviewRecordDbManager;
import org.junit.Test;

public class ReviewRecordDbManagerTest {

  ReviewRecordDbManager reviewRecordDbManager = new ReviewRecordDbManager();

  @Test
  public void insertReviewRecordTest() {
    reviewRecordDbManager.insertReviewRecord(41, 17, 1, null, "feedback test", 1);
    reviewRecordDbManager.insertReviewRecord(41, 18, 0, null, "feedback test", 1);
    reviewRecordDbManager.insertReviewRecord(41, 19, 1, null, "feedback test", 1);
    reviewRecordDbManager.insertReviewRecord(41, 20, 0, null, "feedback test", 1);
    reviewRecordDbManager.insertReviewRecord(41, 21, 1, null, "feedback test", 1);
  }

  @Test
  public void getReviewRecordByIdTest() {
    ReviewRecord reviewRecord = reviewRecordDbManager.getReviewRecordById(11);
    System.out.println(reviewRecord.getId());
    System.out.println(reviewRecord.getPmId());
    System.out.println(reviewRecord.getRsmId());
    System.out.println(reviewRecord.getScore());
    System.out.println(reviewRecord.getTime());
    System.out.println(reviewRecord.getFeedback());
    System.out.println(reviewRecord.getReviewOrder());
  }

  @Test
  public void getReviewRecordByPairMatchingIdTest() {
    List<ReviewRecord> reviewRecordList = reviewRecordDbManager.getReviewRecordByPairMatchingId(52);

    for(ReviewRecord reviewRecord: reviewRecordList) {
      System.out.println(reviewRecord.getId());
      System.out.println(reviewRecord.getPmId());
      System.out.println(reviewRecord.getRsmId());
      System.out.println(reviewRecord.getScore());
      System.out.println(reviewRecord.getTime());
      System.out.println(reviewRecord.getFeedback());
      System.out.println(reviewRecord.getReviewOrder());
    }
  }
}
