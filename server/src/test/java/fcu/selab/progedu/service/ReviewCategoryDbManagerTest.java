package fcu.selab.progedu.service;

import fcu.selab.progedu.data.ReviewCategory;
import fcu.selab.progedu.db.ReviewCategoryDbManager;
import org.junit.Test;

import java.util.List;

public class ReviewCategoryDbManagerTest {

    ReviewCategoryDbManager reviewCategoryDbManager = ReviewCategoryDbManager.getInstance();

    @Test
    public void insertReviewCategoryTest() {
        reviewCategoryDbManager.insertReviewCategory("Test from server 1", "Metrics from server 1");
    }

    @Test
    public void getReviewCategoryTest() {
        List<ReviewCategory> reviewCategories = reviewCategoryDbManager.getReviewCategory();

        for(ReviewCategory reviewCategory: reviewCategories) {
            System.out.println(reviewCategory.getId());
            System.out.println(reviewCategory.getName());
            System.out.println(reviewCategory.getMetrics());
        }
    }

    @Test
    public void getCategoryIdByNameTest() {
        int id = reviewCategoryDbManager.getCategoryIdByName("可讀性Readability");
        System.out.println(id);
    }

    @Test
    public void deleteReviewCategoryByIdTest() {
        reviewCategoryDbManager.deleteReviewCategoryById(9);
    }

    @Test
    public void deleteReviewCategoryByNameTest() {
        reviewCategoryDbManager.deleteReviewCategoryByName("Test from server 1");
    }
}
