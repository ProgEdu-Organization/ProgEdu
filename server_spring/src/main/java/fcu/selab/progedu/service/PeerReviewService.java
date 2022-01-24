package fcu.selab.progedu.service;

import javax.ws.rs.QueryParam;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import io.swagger.models.auth.In;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.gitlab.api.models.GitlabProject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import fcu.selab.progedu.data.*;
import fcu.selab.progedu.db.*;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.GitlabService;

@RestController
@RequestMapping(value = "/peerReview")
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
  private ReviewStatusDbManager reviewStatusDbManager = ReviewStatusDbManager.getInstance();
  private ReviewRecordStatusDbManager reviewRecordStatusDbManager = ReviewRecordStatusDbManager.getInstance();
  private AssessmentTimeDbManager assessmentTimeDbManager = AssessmentTimeDbManager.getInstance();


  private static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewService.class);


  /**
   * insert review record by specific user, reviewed and assignment name
   *
   * @param username       user name
   * @param reviewedName   reviewed name
   * @param assignmentName assignment name
   * @param reviewRecord   review record
   */
  @PostMapping("create")
  public ResponseEntity<Object> createReviewRecord(
          @RequestParam("username") String username,
          @RequestParam("reviewedName") String reviewedName,
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("reviewRecord") String reviewRecord,
          @RequestParam("round") int round
  ) {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      int userId = userDbManager.getUserIdByUsername(username);
      int reviewedId = userDbManager.getUserIdByUsername(reviewedName);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
      Date createDate = new Date();
      int reviewSettingId = reviewSetting.getId();
      int auId = assignmentUserDbManager.getAuid(assignmentId, reviewedId);
      int pmId = pairMatchingDbManager.getPairMatchingIdByAuIdReviewId(auId, userId);
      int reviewOrder = 1;
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
      Date currentDate = new Date();
      AssessmentTime assessmentTime = assessmentTimeDbManager.getAssignmentTimeByTimeAndName(assignmentName, currentDate);

      if (assessmentTime == null || assessmentTime.getAssessmentActionEnum().equals(AssessmentActionEnum.DO)) {
        return new ResponseEntity<>("Not allow to review", headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // 1. Check this review record is expired or not,
      //    if it's expired, it won't create new review record

      if (createDate.compareTo(assessmentTime.getEndTime()) >= 0) {
        return new ResponseEntity<>("This review has been expired.", headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // 2. Check this review record has been release or not.
      //    PS. this won't happened, unless the student used this api in correct way
      if (createDate.compareTo(assessmentTime.getStartTime()) < 0) {
        return new ResponseEntity<>("This review hasn't been released.", headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // 3. Upload the status of pair matching
      int status = reviewStatusDbManager
              .getReviewStatusIdByStatus(ReviewStatusEnum.COMPLETED.getTypeName());
//      pairMatchingDbManager.updatePairMatchingById(status, pmId);
      reviewRecordStatusDbManager.updateReviewRecordStatusByPmId(status, pmId, round);

      // 4. Check which time have been reviewed, and upload the review order
      /*if (!reviewRecordDbManager.isFirstTimeReviewRecord(pmId)) {
        reviewOrder = reviewRecordDbManager.getLatestReviewOrder(pmId) + 1;
      }*/

      // 5. Insert new review record onto db
      org.json.JSONObject jsonObject = new org.json.JSONObject(reviewRecord);
      org.json.JSONArray jsonArray = jsonObject.getJSONArray("allReviewRecord");

      for (int i = 0; i < jsonArray.length(); i++) {
        org.json.JSONObject object = jsonArray.getJSONObject(i);
        int id = object.getInt("id");
        int score = object.getInt("score");
        String feedback = object.getString("feedback");
        int rrsId = reviewRecordStatusDbManager.getIdByPmIdAndRound(pmId, round);
        int rsmId = reviewSettingMetricsDbManager
                .getReviewSettingMetricsIdByRsIdRsmId(reviewSettingId, id);

        reviewRecordDbManager
                .insertReviewRecord(rrsId, rsmId, score, createDate, feedback, 0);
      }

      // 6. When create review record check round is pass or fail
      roundCheck(assignmentName, reviewedName, round);

      return new ResponseEntity<>(headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * insert review record by specific user, reviewed and assignment name
   *
   * @param username       user name
   * @param reviewedName   reviewed name
   * @param assignmentName assignment name
   * @param reviewRecord   review record
   */
  @PostMapping("teacher/create")
  public ResponseEntity<Object> createTeacherReviewRecord(
          @RequestParam("username") String username,
          @RequestParam("reviewedName") String reviewedName,
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("reviewRecord") String reviewRecord,
          @RequestParam("round") int round
  ) {

    HttpHeaders headers = new HttpHeaders();
    //
    System.out.println("teacher review record");

    try {
      int userId = userDbManager.getUserIdByUsername(username);
      int reviewedId = userDbManager.getUserIdByUsername(reviewedName);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
      Date createDate = new Date();
      int reviewSettingId = reviewSetting.getId();
      int auId = assignmentUserDbManager.getAuid(assignmentId, reviewedId);
      int pmId = pairMatchingDbManager.getPairMatchingIdByAuIdReviewId(auId, userId);
      int reviewOrder = 1;
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
      Date currentDate = new Date();
      AssessmentTime assessmentTime = assessmentTimeDbManager.getAssignmentTimeByTimeAndName(assignmentName, currentDate);

      // 1. Check this review record is expired or not,
      //    if it's expired, it won't create new review record

      /*if (createDate.compareTo(assessmentTime.getEndTime()) >= 0) {
        return new ResponseEntity<>("This review has been expired.", headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
      }*/

      // 3. Upload the status of pair matching
      int status = reviewStatusDbManager
              .getReviewStatusIdByStatus(ReviewStatusEnum.COMPLETED.getTypeName());
//      pairMatchingDbManager.updatePairMatchingById(status, pmId);
      reviewRecordStatusDbManager.updateReviewRecordStatusByPmId(status, pmId, round);

      // 4. Check which time have been reviewed, and upload the review order
      /*if (!reviewRecordDbManager.isFirstTimeReviewRecord(pmId)) {
        reviewOrder = reviewRecordDbManager.getLatestReviewOrder(pmId) + 1;
      }*/

      // 5. Insert new review record onto db
      org.json.JSONObject jsonObject = new org.json.JSONObject(reviewRecord);
      org.json.JSONArray jsonArray = jsonObject.getJSONArray("allReviewRecord");

      for (int i = 0; i < jsonArray.length(); i++) {
        org.json.JSONObject object = jsonArray.getJSONObject(i);
        int id = object.getInt("id");
        int score = object.getInt("score");
        String feedback = object.getString("feedback");
        int rrsId = reviewRecordStatusDbManager.getIdByPmIdAndRound(pmId, round);
        int rsmId = reviewSettingMetricsDbManager
                .getReviewSettingMetricsIdByRsIdRsmId(reviewSettingId, id);

        reviewRecordDbManager
                .insertReviewRecord(rrsId, rsmId, score, createDate, feedback, 1);
      }

      // 6. When create review record check round is pass or fail
      System.out.println("Round Check");
      roundCheck(assignmentName, username, round);

      return new ResponseEntity<>(headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * get one user's status of reviewing other's hw
   *
   * @param username user name
   */
  //取代
  /*
  @GetMapping("/status/oneUser")
  public ResponseEntity<Object> getReviewStatus(
          @RequestParam("username") String username,
          @RequestParam("round") int round
  ) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //
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
        jsonObject.put("count", getReviewCompletedCount(assignment.getId(), reviewId, round));
        jsonObject.put("status", reviewerStatus(assignment.getId(), reviewId, reviewSetting.getAmount(), round));

				jsonArray.add(jsonObject);
			}
    	return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/
  @GetMapping("/status/round/oneUser")
  public ResponseEntity<Object> getRoundReviewStatus(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName
  ) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.S");

    try {
      JSONArray array = new JSONArray();
      Assignment assignment = assignmentDbManager.getAssignmentByName(assignmentName);
      List<AssessmentTime> assessmentTimeList = assessmentTimeDbManager.getAssessmentTimeByName(assignmentName);
      int reviewId = userDbManager.getUserIdByUsername(username);
      ReviewSetting assignmentSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
      int assignmentRound = assignmentSetting.getRound();
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAidAndReviewId(
              assignment.getId(), reviewId
      );

      for (PairMatching pairMatching : pairMatchingList) {
        JSONObject ob = new JSONObject();
        ob.put("display", assignment.isDisplay());
        //round
        List<ReviewRecordStatus> reviewRecordStatusList = reviewRecordStatusDbManager.getAllReviewRecordStatusByPairMatchingId(pairMatching.getId());
        JSONArray reviewRound = new JSONArray();
        int timeIndex = 1;
        for (ReviewRecordStatus reviewRecordStatus : reviewRecordStatusList) {
          JSONObject roundStatus = new JSONObject();
          if (reviewRecordStatus.getRound() <= assignmentRound) {
            roundStatus.put("status", reviewRecordStatus.getReviewStatusEnum());
            roundStatus.put("startTime", assessmentTimeList.get(timeIndex).getStartTime());
            roundStatus.put("endTime", assessmentTimeList.get(timeIndex).getEndTime());
            reviewRound.add(roundStatus);
            timeIndex += 2;
          }
        }
        ob.put("name", userDbManager.getUsername(assignmentUserDbManager.getUidById(pairMatching.getAuId())));
        ob.put("reviewRoundStatus", reviewRound);
        ob.put("round", assignmentSetting.getRound());
        array.add(ob);
      }
      return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
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
    //

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<User> users = getStudents();
      for (User user : users) {
        String username = user.getUsername();
//        ResponseEntity<Object> reviewStatus = getReviewStatus(username);
        JSONObject ob = new JSONObject();
        ob.put("username", username);
        ob.put("name", user.getName());
        ob.put("display", user.getDisplay());
//        ob.put("reviewStatus", reviewStatus.getBody());

        array.add(ob);
      }
      result.put("allReviewStatus", array);
      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/status/round/allUsers")
  public ResponseEntity<Object> getAllReviewRoundStatus(
          @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.S");

    try {
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      List<AssignmentUser> assignmentUserList = assignmentUserDbManager.getAssignmentUserListByAid(assignmentId);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      JSONArray jsonArray = new JSONArray();

      for (AssignmentUser assignmentUser : assignmentUserList) {
        int reviewId = assignmentUser.getUid();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", userDbManager.getUsername(reviewId));
        JSONArray reviewRound = new JSONArray();
        for (int round = 1; round <= reviewSetting.getRound(); round++) {
          JSONObject roundStatus = new JSONObject();
          roundStatus.put("amount", reviewSetting.getAmount());
          roundStatus.put("count", getReviewCompletedCount(assignmentId, reviewId, round));
          roundStatus.put("status", reviewerStatus(assignmentId, reviewId, reviewSetting.getAmount(), round));
          reviewRound.add(roundStatus);
        }
        jsonObject.put("reviewRound", reviewRound);
        jsonArray.add(jsonObject);
      }
      return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private int getReviewCompletedCount(int aid, int reviewId, int round) throws SQLException {
    List<PairMatching> pairMatchingList =
            pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    int count = 0;

    for (PairMatching pairMatching : pairMatchingList) {
      ReviewRecordStatus reviewRecordStatus = reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), round);

      if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        count++;
      }
    }
    return count;
  }

  @GetMapping("/record/detail")
  public ResponseEntity<Object> getRecordDetail(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName
  ) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int userId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);

      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);

      for (PairMatching pairMatching : pairMatchingList) {
        JSONObject reviewed = new JSONObject();
        JSONArray reviewDetailArray = new JSONArray();
        ReviewRecordStatus latestCompleted = reviewRecordStatusDbManager.getLatestCompletedReview(pairMatching.getId());
        int reviewerId = pairMatching.getReviewId();

        reviewed.put("id", reviewerId);
        reviewed.put("name", userDbManager.getUsername(reviewerId));
        reviewed.put("assessmentTimes", assessmentTimeDbManager.getAssignmentTimeNameById(assignmentId));
        reviewed.put("totalCount", reviewSetting.getAmount());
        reviewed.put("latestCompletedRound", latestCompleted.getRound());  //the latest completed round

        //only get the latest completed round record
        int latestRrsId = latestCompleted.getId();
        List<ReviewRecord> reviewRecordList = reviewRecordDbManager.getReviewRecordByRrsId(latestRrsId);

        if (reviewRecordList.isEmpty()) {
          reviewed.put("status", false);
        } else {
          reviewed.put("status", true);
          for (ReviewRecord reviewRecord : reviewRecordList) {
            JSONObject ob = new JSONObject();
            int metricsId = reviewSettingMetricsDbManager.getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
            int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
            ob.put("score", reviewRecord.getScore());
            ob.put("feedback", reviewRecord.getFeedback());
            ob.put("time", reviewRecord.getTime());
            ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
            ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
            if(reviewRecord.getScore() == 2 && reviewRecord.getReviewScore() != -1) {
              ob.put("feedbackScore", reviewRecord.getReviewScore());
            }
            reviewDetailArray.add(ob);
          }
          reviewed.put("Detail", reviewDetailArray);
        }
        array.add(reviewed);
      }
      result.put("allRecordDetail", array);
      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
  @GetMapping("/record/detail/page")
  public ResponseEntity<Object> getRecordDetailPage(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("reviewId") int reviewId,
          @RequestParam("page") int page
  ) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    try {
      JSONObject ob = new JSONObject();
      int userId = userDbManager.getUserIdByUsername(username);
      int aId = assignmentDbManager.getAssignmentIdByName(assignmentName);

      int auid = assignmentUserDbManager.getAuid(aId, userId);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(aId);
      ob.put("reviewId", reviewId);
      ob.put("reviewName", userDbManager.getUsername(reviewId));
      ob.put("totalCount", reviewSetting.getRound());
      ob.put("pagination", page);

      PairMatching pairMatching = pairMatchingDbManager.getPairMatchingByAuIdReviewId(auid, reviewId);
      ReviewRecordStatus reviewRecordStatus = reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), page);
      List<ReviewRecord> reviewRecordList = reviewRecordDbManager.getReviewRecordByRrsId(reviewRecordStatus.getId());
      ob.put("detail", reviewRecordList);
      return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }*/

  /**
   * check which reviewed status of specific assignment_user
   *
   * @param auId              assignment_user id
   * @param commitRecordCount commit record count
   */
  public String reviewedRecordStatus(int auId, int commitRecordCount)
          throws SQLException {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);
    String resultStatus = "INIT";
    int aid = assignmentUserDbManager.getAidById(auId);
    AssessmentActionEnum currentAction = getAssignmentCurrentAction(aid);
    int currentRound = getAssignmentCurrentRound(aid);

    if(currentAction.equals(AssessmentActionEnum.DO)) {
      if (commitRecordCount <= 1) {
        return resultStatus;
      } else if (commitRecordCount > 1) {
        resultStatus = "DONE";
        return resultStatus;
      }
    }

    int reviewerAmount = reviewSettingDbManager.getReviewSettingAmountByAId(aid);
    int completedCount = 0;
    for(PairMatching pairMatching : pairMatchingList) {
      ReviewRecordStatus reviewRecordStatus =
          reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), currentRound);
      if(reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        completedCount++;
      }
    }

    if(commitRecordCount > 1) {
      resultStatus = "Done";
    }
    if(completedCount > 0 && completedCount < reviewerAmount) {
      resultStatus = "UNDER_REVIEW";
    } else if (completedCount == reviewerAmount) {
      resultStatus = "REVIEWED";
    }

    /*for (PairMatching pairMatching : pairMatchingList) {
      List<ReviewRecordStatus> reviewRecordStatusList = reviewRecordStatusDbManager.getAllReviewRecordStatusByPairMatchingId(pairMatching.getId());
      for (ReviewRecordStatus reviewRecordStatus : reviewRecordStatusList) {
        if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.UNCOMPLETED)) {
          resultStatus.put(String.valueOf(reviewRecordStatus.getPmId() + reviewRecordStatus.getRound()),"DONE");
        } else if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
          resultStatus.put(String.valueOf(reviewRecordStatus.getPmId() + reviewRecordStatus.getRound()),"REVIEWED");
        }
      }
    }*/

    return resultStatus;
  }

  public AssessmentActionEnum getAssignmentCurrentAction(int aid) {
    AssessmentActionEnum action = AssessmentActionEnum.DO;

    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
    Date currentDate = new Date();
    List<AssessmentTime> assessmentTimeList = assessmentTimeDbManager.getAssignmentTimeNameById(aid);
    for(AssessmentTime assessmentTime : assessmentTimeList) {
      if(currentDate.compareTo(assessmentTime.getEndTime()) < 0
          && currentDate.compareTo(assessmentTime.getStartTime()) > 0) {
        action = assessmentTime.getAssessmentActionEnum();
      }
    }
    return action;
  }

  public int getAssignmentCurrentRound(int aid) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
    Date currentDate = new Date();
    List<AssessmentTime> assessmentTimeList = assessmentTimeDbManager.getAssignmentTimeNameById(aid);
    int round = 0;
    for(AssessmentTime assessmentTime : assessmentTimeList) {
      if(assessmentTime.getAssessmentActionEnum().equals(AssessmentActionEnum.DO)) {
        round++;
      }
      if(currentDate.compareTo(assessmentTime.getEndTime()) < 0
          && currentDate.compareTo(assessmentTime.getStartTime()) > 0) {
        break;
      }
    }
    return round;
  }

  /**
   * get one user commit result which is assigned by peer review
   *
   * @param username user name
   */
  @GetMapping("record/oneUser")
  public ResponseEntity<Object> getReviewedRecord(@QueryParam("username") String username) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    try {
      List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
      int userId = userDbManager.getUserIdByUsername(username);
      JSONArray array = new JSONArray();
      for (Assignment assignment : assignmentList) {
        int auId = assignmentUserDbManager.getAuid(assignment.getId(), userId);
        JSONObject ob = new JSONObject();
        int commitRecordCount = commitRecordDbManager.getCommitCount(auId);
        ob.put("assignmentName", assignment.getName());
        ob.put("commitRecordCount", commitRecordCount);
        ob.put("reviewStatus", reviewedRecordStatus(auId, commitRecordCount));
        JSONArray jsonArray = new JSONArray();
        for (AssessmentTime assessmentTime : assignment.getAssessmentTimeList()) {
          JSONObject assessmentTimeObject = new JSONObject();
          assessmentTimeObject.put("assessmentAction", assessmentTime.getAssessmentActionEnum().toString());
          assessmentTimeObject.put("startTime", dateFormat.format(assessmentTime.getStartTime()));
          assessmentTimeObject.put("endTime", dateFormat.format(assessmentTime.getEndTime()));
          jsonArray.add(assessmentTimeObject);
        }
        ob.put("assessmentTimes", jsonArray);
        array.add(ob);
      }
      return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * get review details from specific reviewer and assignment name
   *
   * @param username       user name
   * @param assignmentName assignment name
   */
  @GetMapping("status/detail")
  public ResponseEntity<Object> getReviewStatusDetail(@RequestParam("username") String username,
                                                      @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

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
      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
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
  /*
  @GetMapping("status/detail/page") // Todo 這目前還沒用到 先沒呼叫
  public ResponseEntity<Object> getReviewedStatusDetailPagination(@RequestParam("username") String username,
                                                                  @RequestParam("assignmentName") String assignmentName,
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
  }*/

  /**
   * get user's hw detail which had been reviewed
   *
   * @param username       user name
   * @param assignmentName assignment name
   */
  /*
  @GetMapping("record/detail")
  public ResponseEntity<Object> getReviewedRecordDetail(@QueryParam("username") String username,
                                                        @QueryParam("assignmentName") String assignmentName) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

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

   */

  /**
   * get user's hw detail which had been reviewed
   *
   * @param username       user name
   * @param assignmentName assignment name
   */

  @GetMapping("record/detail/page")
  public ResponseEntity<Object> getReviewedRecordDetailPagination(@RequestParam("username") String username,
                                                                  @RequestParam("assignmentName") String assignmentName,
                                                                  @RequestParam("reviewId") int reviewId,
                                                                  @RequestParam("round") int round) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    ResponseEntity<Object> response = null;

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int userId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);
      PairMatching pairMatching = pairMatchingDbManager.getPairMatchingByAuIdReviewId(auId, reviewId);
      int rrsId = reviewRecordStatusDbManager.getIdByPmIdAndRound(pairMatching.getId(), round);
      List<ReviewRecord> reviewRecordList = reviewRecordDbManager.getReviewRecordByRrsId(rrsId);

      result.put("id", reviewId);
      result.put("name", userDbManager.getUsername(pairMatching.getReviewId()));
      for (ReviewRecord reviewRecord : reviewRecordList) {
        JSONObject ob = new JSONObject();
        int metricsId = reviewSettingMetricsDbManager
                .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
        ob.put("score", reviewRecord.getScore());
        if(reviewRecord.getScore() == 2 && reviewRecord.getReviewScore() != -1) {
          ob.put("feedbackScore", reviewRecord.getReviewScore());
        }
        ob.put("feedback", reviewRecord.getFeedback());
        ob.put("time", dateFormat.format(reviewRecord.getTime()));
        ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
        int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
        ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
        ob.put("teacherReview", reviewRecord.getTeacherReview());
        array.add(ob);
      }
      result.put("round", round);
      result.put("Detail", array);

      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/status/round/detail")
  public ResponseEntity<Object> getRoundStatusDetail(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("round") int round,
          @RequestParam("order") int order
  ) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    JSONObject result = new JSONObject();

    try {
      int reviewId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignmentId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAidAndReviewId(assignmentId, reviewId);

      PairMatching pairMatching = pairMatchingList.get(order - 1);
      JSONObject reviewed = new JSONObject();
      JSONArray array = new JSONArray();
      JSONArray reviewDetailArray = new JSONArray();
      ReviewRecordStatus reviewRecordStatus = reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), round);

      int userId = assignmentUserDbManager.getUidById(pairMatching.getAuId());
      reviewed.put("id", userId);
      reviewed.put("name", userDbManager.getUsername(userId));
      AssessmentTime assessmentTime = assessmentTimeDbManager.getAssignmentReviewTimeNameById(assignmentId).get(round-1);
      reviewed.put("reviewEndTime", assessmentTime.getEndTime());
      List<ReviewRecord> reviewRecordList = reviewRecordDbManager.getReviewRecordByRrsId(reviewRecordStatus.getId());

      if (reviewRecordList.isEmpty()) {
        reviewed.put("status", false);
      } else {
        reviewed.put("status", true);
        for (ReviewRecord reviewRecord : reviewRecordList) {
          int metricsId = reviewSettingMetricsDbManager.getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
          JSONObject ob = new JSONObject();
          ob.put("score", reviewRecord.getScore());
          if (reviewRecord.getScore() == 2 && reviewRecord.getReviewScore() != -1) {
            ob.put("feedbackScore", reviewRecord.getReviewScore());
          }
          ob.put("feedback", reviewRecord.getFeedback());
          ob.put("time", reviewRecord.getTime());
          ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
          int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
          ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
          reviewDetailArray.add(ob);
        }
        reviewed.put("Detail", reviewDetailArray);
      }
      array.add(reviewed);
      result.put("roundStatusDetail", array);
      return new ResponseEntity<Object>(result, HttpStatus.OK);
    } catch (Exception e) {
      ExceptionUtil.getErrorInfoFromException(e);
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * check reviewer status of his/her review job
   *
   * @param aid      assignment id
   * @param reviewId user id
   */
  private ReviewStatusEnum reviewerStatus(int aid, int reviewId, int amount, int round) throws SQLException {
    List<PairMatching> pairMatchingList =
            pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    ReviewStatusEnum resultStatus = ReviewStatusEnum.INIT;
    int initCount = 0;

    for (PairMatching pairMatching : pairMatchingList) {
      ReviewRecordStatus reviewRecordStatus = reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), round);

      if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.UNCOMPLETED)) {
        resultStatus = ReviewStatusEnum.UNCOMPLETED;
        break;
      } else if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        resultStatus = ReviewStatusEnum.COMPLETED;
      } else if (reviewRecordStatus.getReviewStatusEnum().equals(ReviewStatusEnum.INIT)) {
        initCount++;
      }
    }

    if (initCount == amount) {
      resultStatus = ReviewStatusEnum.INIT;
    }

    return resultStatus;
  }

  /**
   * get metrics by specific assignment
   *
   * @param assignmentName assignment name
   */
  @GetMapping("/metrics")
  public ResponseEntity<Object> getReviewMetrics(
          @QueryParam("assignmentName") String assignmentName
  ) {
    HttpHeaders headers = new HttpHeaders();
    //

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

      return new ResponseEntity<>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * return user's hw as a gzip file (.tar.gz)
   *
   * @param username       user name
   * @param assignmentName assignment name
   */
  @GetMapping(path = "sourceCode")
  public ResponseEntity<Object> getSourceCode(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName) {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      GitlabService gitlabService = GitlabService.getInstance();
      GitlabProject gitlabProject = gitlabService.getProject(username, assignmentName);
      GitlabConfig gitlabConfig = GitlabConfig.getInstance();

      int projectId = gitlabProject.getId();
      // Todo 以下有安全性問題 會公開gitlab 的 api
      String test = gitlabConfig.getGitlabHostUrl() + "/api/v4/projects/" + projectId
              + "/repository/archive.zip?private_token=" + gitlabConfig.getGitlabApiToken();

      JSONObject result = new JSONObject();
      result.put("url", test);

      return new ResponseEntity<>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<>(e, headers, HttpStatus.INTERNAL_SERVER_ERROR);
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

  /**
   * get all commit result which is assigned by peer review
   */
  @GetMapping("record/allUsers")
  public ResponseEntity<Object> getAllReviewedRecord() {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<User> users = getStudents();
      for (User user : users) {
        String username = user.getUsername();
        ResponseEntity<Object> userCommitRecord = getReviewedRecord(username);
        JSONObject ob = new JSONObject();
        ob.put("name", user.getName());
        ob.put("username", user.getUsername());
        ob.put("display", user.getDisplay());
        ob.put("commitRecord", userCommitRecord.getBody());
        array.add(ob);
      }
      result.put("allReviewedRecord", array);
      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  public void roundCheck(String assignmentName, String username, int round) {
    System.out.println("Round Check" + assignmentName + username + round);
    try {
      int aId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int uId = userDbManager.getUserIdByUsername(username);

      int auId = assignmentUserDbManager.getAuid(aId, uId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);
      List<Integer> scoreList = new ArrayList<>();

      for (PairMatching pairMatching : pairMatchingList) {
        int rrsId = reviewRecordStatusDbManager.getIdByPmIdAndRound(pairMatching.getId(), round);
        List<Integer> score = reviewRecordDbManager.getScoreListByRrsId(rrsId);
        if (score.isEmpty()) {
          return;
        } else {
          for (int i = 0; i < score.size(); i++) {
            scoreList.add(score.get(i));
            if (score.get(i) == 2) {
              return;
            }
          }
        }
        System.out.println(scoreList);
      }

      int roundCount = reviewSettingDbManager.getReviewRoundByAId(aId);
      for (PairMatching pairMatching : pairMatchingList) {
        for (int i = round + 1; i <= roundCount; i++) {
          int statusId = reviewStatusDbManager.getReviewStatusIdByStatus(ReviewStatusEnum.COMPLETED.toString());
          reviewRecordStatusDbManager.updateReviewRecordStatusByPmId(statusId, pairMatching.getId(), i);
        }
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  @PostMapping("/review/score")
  public void reviewFeedbackScore(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("username") String username,
          @RequestParam("reviewerId") int reviewerId,
          @RequestParam("round") int round,
          @RequestParam("rmId") int rmId,
          @RequestParam("feedbackScore") int feedbackScore) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    try {
      int aId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int uId = userDbManager.getUserIdByUsername(username);
      int auId = assignmentUserDbManager.getAuid(aId, uId);

      PairMatching pairMatching = pairMatchingDbManager.getPairMatchingByAuIdReviewId(auId, reviewerId);
      ReviewRecordStatus reviewRecordStatus = reviewRecordStatusDbManager.getReviewRecordStatusByPairMatchingIdAndRound(pairMatching.getId(), round);

      ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(aId);
      int rsmId = reviewSettingMetricsDbManager.getReviewSettingMetricsIdByRsIdRsmId(reviewSetting.getId(), rmId);

      reviewRecordDbManager.updateReviewScore(reviewRecordStatus.getId(), rsmId, feedbackScore);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}
