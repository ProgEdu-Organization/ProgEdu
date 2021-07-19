package fcu.selab.progedu.service;

import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.ReviewSettingDbManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping(value ="/peerReview")
public class PeerReviewService {

  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private ReviewSettingDbManager reviewSettingDbManager = ReviewSettingDbManager.getInstance();


	@GetMapping("/status/oneUser")
	public ResponseEntity<Object> getReviewStatus(
					@RequestParam("username") String username) {

		HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
		
		try {
			List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
			int reviewId = userDbManager.getUserIdByUsername(username);
			JSONArray jsonArray = new JSONArray();

			for(Assignment assignmnet : assignmentList) {
				ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("assignmentName", assignment.getName());
        jsonObject.put("amount", reviewSetting.getAmount());
        jsonObject.put("releaseTime", assignment.getReleaseTime());
        jsonObject.put("deadline", assignment.getDeadline());
        jsonObject.put("reviewReleaseTime", reviewSetting.getReleaseTime());
        jsonObject.put("reviewDeadline", reviewSetting.getDeadline());
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
}