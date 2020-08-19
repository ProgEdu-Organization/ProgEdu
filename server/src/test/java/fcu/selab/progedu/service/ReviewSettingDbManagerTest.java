package fcu.selab.progedu.service;

import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.db.ReviewSettingDbManager;
import org.junit.Test;

import java.util.Date;

public class ReviewSettingDbManagerTest {
    ReviewSettingDbManager reviewSettingDbManager = new ReviewSettingDbManager();

    @Test
    public void insertReviewSettingTest() {
        Date releaseTime = new Date();
        reviewSettingDbManager.insertReviewSetting(4, 3, null, null);
    }

    @Test
    public void getReviewSettingTest() {
        ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(1);
        System.out.println(reviewSetting.getId());
        System.out.println(reviewSetting.getaId());
        System.out.println(reviewSetting.getAmount());
        System.out.println(reviewSetting.getReleaseTime());
        System.out.println(reviewSetting.getDeadline());
    }

    @Test
    public void getReviewSettingAmountByAIdTest() {
        int amount = reviewSettingDbManager.getReviewSettingAmountByAId(1);
        System.out.println(amount);
    }

    @Test
    public void deleteReviewSettingByAIdTest() {
        reviewSettingDbManager.deleteReviewSettingById(2);
    }

    @Test
    public void deleteReviewSettingByIdTest() {
        reviewSettingDbManager.deleteReviewSettingByAId(3);
    }
}
