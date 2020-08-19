package fcu.selab.progedu.service;

import java.util.List;
import fcu.selab.progedu.data.ReviewMetrics;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import org.junit.Test;

public class ReviewMetricsDbManagerTest {

  ReviewMetricsDbManager reviewMetricsDbManager = new ReviewMetricsDbManager();

  @Test
  public void insertReviewMetricsTest() {
//        reviewMetricsDbManager.insertReviewMetrics(1, 1,
//                "符合題目要求",
//                "這次的提交是否達到開發者的預期目的?也就是學生是否達到老師題目要求",
//                "https://www.google.com/");
//        reviewMetricsDbManager.insertReviewMetrics(2, 1,
//                "排版是否整齊",
//                "不規則的排版會造成審查人員在閱讀上會有困難，因此會花更多的時間去理解造成效率低",
//                "https://www.google.com/");
//        reviewMetricsDbManager.insertReviewMetrics(2, 1,
//                "符合變數名稱格式",
//                "審查人員需要找到一個對應的、真實的實質資料時，好的命名能減少審查人員的理解時間並提高效率",
//                "https://www.google.com/");
//        reviewMetricsDbManager.insertReviewMetrics(3, 1,
//                "函式標示註解",
//                "註解是\"解釋函式為什麼有時是有用的\"，而不是解釋函式裡面在做什麼，註解像是決策背後的原因",
//                "https://www.google.com/");
//        reviewMetricsDbManager.insertReviewMetrics(4, 1,
//                "是否過度設計",
//                "太複雜通常表示\"審查人員很難立即理解\"，這也表示\"開發人員在引用或修改此程式碼可能引入錯誤\"",
//                "https://www.google.com/");
  }

  @Test
  public void getReviewMetricsTest() {
    List<ReviewMetrics> reviewMetricsList = reviewMetricsDbManager.getReviewMetrics(4);

    for (ReviewMetrics reviewMetrics : reviewMetricsList) {
      System.out.println(reviewMetrics.getId());
      System.out.println(reviewMetrics.getCategory());
      System.out.println(reviewMetrics.getMode());
      System.out.println(reviewMetrics.getMetrics());
      System.out.println(reviewMetrics.getDescription());
      System.out.println(reviewMetrics.getLink());
    }
  }

  @Test
  public void editReviewMetricsByIdTest() {
    reviewMetricsDbManager.editReviewMetricsById(8, 1, "editTest", "wwwwwwww");
  }

  @Test
  public void deleteReviewMetricsTest() {
    reviewMetricsDbManager.deleteReviewMetrics(9);
  }
}
