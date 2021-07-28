package fcu.selab.progedu.service;

import fcu.selab.progedu.data.*;
import fcu.selab.progedu.db.*;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
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
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
  private CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();
  private ReviewRecordDbManager reviewRecordDbManager = ReviewRecordDbManager.getInstance();
  private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();
  private ReviewSettingMetricsDbManager reviewSettingMetricsDbManager = ReviewSettingMetricsDbManager.getInstance();
  private ScoreModeDbManager scoreModeDbManager = ScoreModeDbManager.getInstance();


  private static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewService.class);

  /**
   * get one user's status of reviewing other's hw
   *
   * @param username user name
   */
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

  /**
   * get all user's status of reviewing other's hw
   */
  @GetMapping("/status/allUsers")
  public ResponseEntity<Object> getAllReviewStatus() {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<User> users = getStudents();
      for (User user : users) {
        String username = user.getUsername();
        ResponseEntity<Object> reviewStatus = getReviewStatus(username);
        JSONObject ob = new JSONObject();
        ob.put("username", username);
        ob.put("name", user.getName());
        ob.put("display", user.getDisplay());
        ob.put("reviewStatus", reviewStatus.getBody());

        array.add(ob);
      }
      result.put("allReviewStatus", array);
      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e){
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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

  public String reviewedRecordStatus(int auId, int commitRecordCount)
          throws SQLException {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);
    String resultStatus = "INIT";

    if (commitRecordCount == 1) {
      return resultStatus;
    }

    for (PairMatching pairMatching : pairMatchingList) {
      if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.UNCOMPLETED)) {
        resultStatus = "DONE";
        break;
      } else if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        resultStatus = "REVIEWED";
      }
    }

    return resultStatus;
  }

  /**
   * get one user commit result which is assigned by peer review
   *
   * @param username user name
   */
  @GetMapping("record/oneUser")
  public ResponseEntity<Object> getReviewedRecord(@RequestParam("username") String username) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    try {
      List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
      int userId = userDbManager.getUserIdByUsername(username);
      JSONArray array = new JSONArray();
      for (Assignment assignment : assignmentList) {
        int auId = assignmentUserDbManager.getAuid(assignment.getId(), userId);
        ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
        JSONObject ob = new JSONObject();
        int commitRecordCount = commitRecordDbManager.getCommitCount(auId);
        ob.put("assignmentName", assignment.getName());
        ob.put("releaseTime", dateFormat.format(assignment.getReleaseTime()));
        ob.put("deadline", dateFormat.format(assignment.getDeadline()));
        ob.put("commitRecordCount", commitRecordCount);
        ob.put("reviewReleaseTime", dateFormat.format(reviewSetting.getReleaseTime()));
        ob.put("reviewDeadline", dateFormat.format(reviewSetting.getDeadline()));
        ob.put("reviewStatus", reviewedRecordStatus(auId, commitRecordCount));
        array.add(ob);
      }
      return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("status/detail")
  public ResponseEntity<Object> getReviewStatusDetail(@RequestParam("username") String username,
                                                      @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int reviewId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager
              .getPairMatchingByAidAndReviewId(assignmentId, reviewId);

      for (PairMatching pairMatching : pairMatchingList) {
        JSONObject reviewed = new JSONObject();
        JSONArray reviewDetailArray = new JSONArray();
        int order = reviewRecordDbManager.getLatestReviewOrder(pairMatching.getId());
        List<ReviewRecord> reviewRecordList =
                reviewRecordDbManager.getReviewRecordByPairMatchingId(pairMatching.getId(), order);

        int userId = assignmentUserDbManager.getUidById(pairMatching.getAuId());
        reviewed.put("id", userId);
        reviewed.put("name", userDbManager.getUsername(userId));
        if (reviewRecordList.isEmpty()) {
          reviewed.put("status", false);
        } else {
          reviewed.put("status", true);
          for (ReviewRecord reviewRecord : reviewRecordList) {
            JSONObject ob = new JSONObject();
            int metricsId = reviewSettingMetricsDbManager
                    .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
            ob.put("score", reviewRecord.getScore());
            ob.put("feedback", reviewRecord.getFeedback());
            ob.put("time", reviewRecord.getTime());
            ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
            int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
            ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
            reviewDetailArray.add(ob);
          }
          reviewed.put("totalCount", order);
          reviewed.put("Detail", reviewDetailArray);
        }
        reviewed.put("reviewDeadline", reviewSetting.getDeadline());

        array.add(reviewed);
      }

      result.put("allStatusDetail", array);
      return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * get review details from specific reviewer, assignment name and page
   *
   * @param username       user name
   * @param assignmentName assignment name
   * @param userId         user id
   * @param page           page
   */
  @GetMapping("status/detail/page")
  public ResponseEntity<Object> getReviewedStatusDetailPagination(@RequestParam("username") String username,
                                                    @RequestParam("assignmentName")
                                                            String assignmentName,
                                                    @RequestParam("userId") int userId,
                                                    @RequestParam("page") int page) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    ResponseEntity<Object> response = null;

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int reviewId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);
      PairMatching pairMatching = pairMatchingDbManager
              .getPairMatchingByAuIdReviewId(auId, reviewId);
      int order = reviewRecordDbManager.getLatestReviewOrder(pairMatching.getId());
      List<ReviewRecord> reviewRecordList = reviewRecordDbManager
              .getReviewRecordByPairMatchingId(pairMatching.getId(), order - page + 1);

      result.put("id", userId);
      result.put("name", userDbManager.getUsername(userId));
      for (ReviewRecord reviewRecord : reviewRecordList) {
        JSONObject ob = new JSONObject();
        int metricsId = reviewSettingMetricsDbManager
                .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
        ob.put("score", reviewRecord.getScore());
        ob.put("feedback", reviewRecord.getFeedback());
        ob.put("time", dateFormat.format(reviewRecord.getTime()));
        ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
        int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
        ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
        array.add(ob);
      }
      result.put("totalCount", order);
      result.put("pagination", page);
      result.put("Detail", array);

      response = new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  /**
   * get user's hw detail which had been reviewed
   *
   * @param username       user name
   * @param assignmentName assignment name
   */
  @GetMapping("record/detail")
  public ResponseEntity<Object> getReviewedRecordDetail(@RequestParam("username") String username,
                                                        @RequestParam("assignmentName") String assignmentName) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    ResponseEntity<Object> response = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    try {
      org.json.JSONObject result = new org.json.JSONObject();
      org.json.JSONArray array = new org.json.JSONArray();
      int userId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);

      for (PairMatching pairMatching : pairMatchingList) {
        org.json.JSONObject reviewer = new org.json.JSONObject();
        org.json.JSONArray reviewDetailArray = new org.json.JSONArray();
        int order = reviewRecordDbManager.getLatestReviewOrder(pairMatching.getId());
        List<ReviewRecord> reviewRecordList =
                reviewRecordDbManager.getReviewRecordByPairMatchingId(pairMatching.getId(), order);

        reviewer.put("id", pairMatching.getReviewId());
        reviewer.put("name", userDbManager.getUsername(pairMatching.getReviewId()));
        if (reviewRecordList.isEmpty()) {
          reviewer.put("status", false);
        } else {
          reviewer.put("status", true);
          for (ReviewRecord reviewRecord : reviewRecordList) {
            org.json.JSONObject ob = new org.json.JSONObject();
            int metricsId = reviewSettingMetricsDbManager
                    .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
            ob.put("score", reviewRecord.getScore());
            ob.put("feedback", reviewRecord.getFeedback());
            ob.put("time", reviewRecord.getTime());
            ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
            int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
            ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
            reviewDetailArray.put(ob);
          }
          reviewer.put("totalCount", order);
          reviewer.put("Detail", reviewDetailArray);
        }
        reviewer.put("reviewReleaseTime", dateFormat.format(reviewSetting.getReleaseTime()));
        reviewer.put("reviewDeadline", dateFormat.format(reviewSetting.getDeadline()));

        array.put(reviewer);
      }

      result.put("allRecordDetail", array);

      response = new ResponseEntity<Object>(result.toString(),headers,HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
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

      return new ResponseEntity<>(result, HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  /**
   * Get all user which role is student
   *
   * @return all GitLab users
   */
  public List<User> getStudents() {
    List<User> studentUsers = new ArrayList<>();
    List<User> users = userDbManager.getAllUsers();

    for (User user : users) {
      if (user.getRole().contains(RoleEnum.STUDENT)) {
        studentUsers.add(user);
      }
    }
    return studentUsers;
  }
}