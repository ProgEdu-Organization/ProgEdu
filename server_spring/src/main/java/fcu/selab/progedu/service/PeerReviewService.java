package fcu.selab.progedu.service;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.data.ReviewMetrics;
import fcu.selab.progedu.data.ReviewRecord;
import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.PairMatchingDbManager;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import fcu.selab.progedu.db.ReviewRecordDbManager;
import fcu.selab.progedu.db.ReviewSettingDbManager;
import fcu.selab.progedu.db.ReviewSettingMetricsDbManager;
import fcu.selab.progedu.db.ReviewStatusDbManager;
import fcu.selab.progedu.db.ScoreModeDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.utils.ExceptionUtil;

@RestController
@RequestMapping(value ="/peerReview")
public class PeerReviewService {

    private static PeerReviewService instance = new PeerReviewService();
    public static PeerReviewService getInstance() {
        return instance;
    }

    private ReviewSettingDbManager reviewSettingDbManager = ReviewSettingDbManager.getInstance();
    private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();
    private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
    private ReviewSettingMetricsDbManager reviewSettingMetricsDbManager = ReviewSettingMetricsDbManager.getInstance();
    private ScoreModeDbManager scoreModeDbManager = ScoreModeDbManager.getInstance();

    @GetMapping("/metrics")
    public ResponseEntity<Object> getReviewMetrics(
            @RequestParam("assignmentName") String assignmentName
    ) {
        try {
            JSONArray array = new JSONArray();
            JSONObject result = new JSONObject();
            int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
            int reviewSettingId = reviewSettingDbManager.getReviewSettingIdByAid(assignmentId);
            List<Integer> metricsList = reviewSettingMetricsDbManager
                .getReviewSettingMetricsByAssignmentId(reviewSettingId);
            for (Integer integer : metricsList) {
                JSONObject entity = new JSONObject();
                ReviewMetrics reviewMetrics = reviewMetricsDbManager.getReviewMetrics(integer);
                entity.put("id", integer);
                entity.put("mode", scoreModeDbManager.getScoreModeDescById(reviewMetrics.getMode()));
                entity.put("metrics", reviewMetrics.getMetrics());
                entity.put("description", reviewMetrics.getDescription());
                entity.put("link", reviewMetrics.getLink());
                array.add(entity);
                }
                result.put("allMetrics", array);

                return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
