package fcu.selab.progedu.service;

import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.ReviewSettingDbManager;
import fcu.selab.progedu.db.PairMatchingDbManager;
import fcu.selab.progedu.db.UserDbManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(value ="/peerReview")
public class PeerReviewService {

  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private ReviewSettingDbManager reviewSettingDbManager = ReviewSettingDbManager.getInstance();
  private PairMatchingDbManager pairMatchingDbManager = PairMatchingDbManager.getInstance();
  private UserDbManager userDbManager = UserDbManager.getInstance();

	@GetMapping("/status/oneUser")
	public ResponseEntity<Object> getReviewStatus(
					@RequestParam("username") String username) {

		HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss.S");
		
		try {
			List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
			int reviewId = userDbManager.getUserIdByUsername(username);
			JSONArray jsonArray = new JSONArray();

			for(Assignment assignment : assignmentList) {
				ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("assignmentName", assignment.getName());
        jsonObject.put("amount", reviewSetting.getAmount());
        jsonObject.put("releaseTime", dateFormat.format(assignment.getReleaseTime()));
        jsonObject.put("deadline", dateFormat.format(assignment.getDeadline()));
        jsonObject.put("reviewReleaseTime", dateFormat.format(reviewSetting.getReleaseTime()));
        jsonObject.put("reviewDeadline", dateFormat.format(reviewSetting.getDeadline()));
        jsonObject.put("count", getReviewCompletedCount(assignment.getId(), reviewId));
        jsonObject.put("status", reviewerStatus(assignment.getId(),
            reviewId, reviewSetting.getAmount()).getTypeName());
				
				jsonArray.add(jsonObject);
			}
    	return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private int getReviewCompletedCount(int aid, int reviewId) throws SQLException {
    List<PairMatching> pairMatchingList =
        pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    int count = 0;

    for (PairMatching pairMatching : pairMatchingList) {
      if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        count++;
      }
    }

    return count;
  }

	/**
   * check reviewer status of his/her review job
   *
   * @param aid      assignment id
   * @param reviewId user id
   */
  private ReviewStatusEnum reviewerStatus(int aid, int reviewId, int amount) throws SQLException {
    List<PairMatching> pairMatchingList =
        pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    ReviewStatusEnum resultStatus = ReviewStatusEnum.INIT;
    int initCount = 0;

    for (PairMatching pairMatching : pairMatchingList) {
      if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.UNCOMPLETED)) {
        resultStatus = ReviewStatusEnum.UNCOMPLETED;
        break;
      } else if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        resultStatus = ReviewStatusEnum.COMPLETED;
      } else if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.INIT)) {
        initCount++;
      }
    }

    if (initCount == amount) {
      resultStatus = ReviewStatusEnum.INIT;
    }

    return resultStatus;
  }
}