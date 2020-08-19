package fcu.selab.progedu.service;

import fcu.selab.progedu.db.ReviewSettingMetricsDbManager;
import org.junit.Test;

import java.util.List;

public class ReviewSettingMetricsDbManagerTest {

    ReviewSettingMetricsDbManager reviewSettingMetricsDbManager = new ReviewSettingMetricsDbManager();

    @Test
    public void insertReviewSettingMetricsTest() {
        reviewSettingMetricsDbManager.insertReviewSettingMetrics(8, 2);
        reviewSettingMetricsDbManager.insertReviewSettingMetrics(8, 4);
        reviewSettingMetricsDbManager.insertReviewSettingMetrics(8, 6);
        reviewSettingMetricsDbManager.insertReviewSettingMetrics(8, 7);
        reviewSettingMetricsDbManager.insertReviewSettingMetrics(8, 8);
    }

    @Test
    public void getReviewSettingMetricsByAssignmentIdTest() {
        List<Integer> integerList = reviewSettingMetricsDbManager.getReviewSettingMetricsByAssignmentId(9);

        for(Integer integer: integerList) {
            System.out.println(integer);
        }
    }

    @Test
    public void deleteReviewSettingMetricsByAssignmentIdTest() {
        reviewSettingMetricsDbManager.deleteReviewSettingMetricsByAssignmentId(9);
    }
}
