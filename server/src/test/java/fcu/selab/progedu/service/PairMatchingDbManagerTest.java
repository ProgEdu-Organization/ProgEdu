package fcu.selab.progedu.service;

import java.util.List;
import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.db.PairMatchingDbManager;
import org.junit.Test;

public class PairMatchingDbManagerTest {

  PairMatchingDbManager pairMatchingDbManager = new PairMatchingDbManager();

  @Test
  public void insertPairMatchingTest() {
    pairMatchingDbManager.insertPairMatching(1, 2, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(1, 3, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(1, 4, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(7, 5, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(7, 3, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(7, 4, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(6, 1, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(6, 3, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(6, 4, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(4, 5, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(4, 1, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(4, 4, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(17, 1, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(17, 3, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(17, 2, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(18, 2, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(18, 1, ReviewStatusEnum.INIT);
    pairMatchingDbManager.insertPairMatching(18, 4, ReviewStatusEnum.INIT);
  }

  @Test
  public void getAllPairMatchingTest() {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getAllPairMatching();

    for(PairMatching pairMatching: pairMatchingList) {
      System.out.println(pairMatching.getId());
      System.out.println(pairMatching.getAuId());
      System.out.println(pairMatching.getReviewId());
      System.out.println(pairMatching.getScoreModeEnum());
    }
  }

  @Test
  public void getPairMatchingByIdTest() {
    PairMatching pairMatching = pairMatchingDbManager.getPairMatchingById(27);

    System.out.println(pairMatching.getId());
    System.out.println(pairMatching.getAuId());
    System.out.println(pairMatching.getReviewId());
    System.out.println(pairMatching.getScoreModeEnum());
  }

  @Test
  public void getPairMatchingByAuIdTest() {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(6);

    for(PairMatching pairMatching: pairMatchingList) {
      System.out.println(pairMatching.getId());
      System.out.println(pairMatching.getAuId());
      System.out.println(pairMatching.getReviewId());
      System.out.println(pairMatching.getScoreModeEnum());
    }
  }

  @Test
  public void getPairMatchingByReviewIdTest() {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByReviewId(1);

    for(PairMatching pairMatching: pairMatchingList) {
      System.out.println(pairMatching.getId());
      System.out.println(pairMatching.getAuId());
      System.out.println(pairMatching.getReviewId());
      System.out.println(pairMatching.getScoreModeEnum());
    }
  }

  @Test
  public void deletePairMatchingByIdTest() {
    pairMatchingDbManager.deletePairMatchingById(21);
  }

  @Test
  public void deletePairMatchingByAuIdTest() {
    pairMatchingDbManager.deletePairMatchingByAuId(4);
  }

  @Test
  public void deletePairMatchingByReviewIdTest() {
    pairMatchingDbManager.deletePairMatchingByReviewId(2);
  }
}
