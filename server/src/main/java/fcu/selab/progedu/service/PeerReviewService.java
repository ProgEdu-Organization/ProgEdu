package fcu.selab.progedu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.data.ReviewMetrics;
import fcu.selab.progedu.data.ReviewRecord;
import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.PairMatchingDbManager;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import fcu.selab.progedu.db.ReviewRecordDbManager;
import fcu.selab.progedu.db.ReviewSettingDbManager;
import fcu.selab.progedu.db.ReviewSettingMetricsDbManager;
import fcu.selab.progedu.db.ScoreModeDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.apache.commons.io.FileUtils;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("peerReview")
public class PeerReviewService {
  private GitlabService gitlabService = GitlabService.getInstance();
  private CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private ReviewSettingDbManager reviewSettingDbManager = ReviewSettingDbManager.getInstance();
  private PairMatchingDbManager pairMatchingDbManager = PairMatchingDbManager.getInstance();
  private ReviewRecordDbManager reviewRecordDbManager = ReviewRecordDbManager.getInstance();
  private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();
  private ReviewSettingMetricsDbManager reviewSettingMetricsDbManager =
      ReviewSettingMetricsDbManager.getInstance();
  private ScoreModeDbManager scoreModeDbManager = ScoreModeDbManager.getInstance();
  private UserDbManager userDbManager = UserDbManager.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewService.class);

  /**
   * create new review record
   */

  /**
   * get metrics by specific assignment
   *
   * @param assignmentName assignment name
   */
  @GET
  @Path("metrics")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewMetrics(@QueryParam("assignmentName") String assignmentName) {
    Response response = null;

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int reviewSettingId = reviewSettingDbManager.getReviewSettingIdByAid(assignmentId);
      List<Integer> metricsList = reviewSettingMetricsDbManager
          .getReviewSettingMetricsByAssignmentId(reviewSettingId);

      for (Integer integer: metricsList) {
        JSONObject ob = new JSONObject();
        ReviewMetrics reviewMetrics = reviewMetricsDbManager.getReviewMetrics(integer);
        ob.put("id", integer);
        ob.put("mode", scoreModeDbManager.getScoreModeDescById(reviewMetrics.getMode()));
        ob.put("metrics", reviewMetrics.getMetrics());
        ob.put("description", reviewMetrics.getDescription());
        ob.put("link", reviewMetrics.getLink());
        array.put(ob);
      }
      result.put("allMetrics", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      response = Response.serverError().build();
    }
    return response;
  }

  /**
   * get all commit result which is assigned by peer review
   */
  @GET
  @Path("record/allUsers")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllReviewedRecord() {
    Response response = null;
    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<User> users = getStudents();
      for (User user: users) {
        String username = user.getUsername();
        Response userCommitRecord = getReviewedRecord(username);
        JSONObject ob = new JSONObject();
        ob.put("name", user.getName());
        ob.put("username", user.getUsername());
        ob.put("display", user.getDisplay());
        ob.put("commitRecord", new JSONArray(userCommitRecord.getEntity().toString()));
        array.put(ob);
      }
      result.put("allReviewedRecord", array);

      response = Response.ok(result.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().build();
    }

    return response;
  }

  /**
   * get one user commit result which is assigned by peer review
   *
   * @param username user name
   */
  @GET
  @Path("record/oneUser")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewedRecord(@QueryParam("username") String username) {
    Response response = null;
    try {
      List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
      int userId = userDbManager.getUserIdByUsername(username);
      JSONArray array = new JSONArray();

      for (Assignment assignment: assignmentList) {
        int auId = assignmentUserDbManager.getAuid(assignment.getId(), userId);
        ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
        JSONObject ob = new JSONObject();
        int commitRecordCount = commitRecordDbManager.getCommitCount(auId);
        ob.put("assignmentName", assignment.getName());
        ob.put("releaseTime", assignment.getReleaseTime());
        ob.put("deadline", assignment.getDeadline());
        ob.put("commitRecordCount", commitRecordCount);
        ob.put("reviewReleaseTime", reviewSetting.getReleaseTime());
        ob.put("reviewDeadline", reviewSetting.getDeadline());
        ob.put("reviewStatus", reviewedRecordStatus(auId, commitRecordCount));
        array.put(ob);
      }
      response = Response.ok(array.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
  }

  /**
   * get user's hw detail which had been reviewed
   *
   * @param username user name
   * @param assignmentName assignment name
   */
  @GET
  @Path("record/detail")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewedRecordDetail(@QueryParam("username") String username,
                                          @QueryParam("assignmentName") String assignmentName) {
    Response response = null;

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int userId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);
      List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);

      for (PairMatching pairMatching: pairMatchingList) {
        JSONObject reviewer = new JSONObject();
        JSONArray reviewDetailArray = new JSONArray();
        int order = reviewRecordDbManager.getLatestReviewOrder(pairMatching.getId());
        List<ReviewRecord> reviewRecordList =
            reviewRecordDbManager.getReviewRecordByPairMatchingId(pairMatching.getId(), order);

        reviewer.put("id", pairMatching.getReviewId());
        reviewer.put("name", userDbManager.getUsername(pairMatching.getReviewId()));
        if (reviewRecordList.isEmpty()) {
          reviewer.put("status", false);
        } else {
          reviewer.put("status", true);
          for (ReviewRecord reviewRecord: reviewRecordList) {
            JSONObject ob = new JSONObject();
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

        array.put(reviewer);
      }

      result.put("allRecordDetail", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().build();
    }
    return response;
  }

  /**
   * get user's hw detail which had been reviewed
   *
   * @param username user name
   * @param assignmentName assignment name
   * @param reviewId review id
   * @param page page
   */
  @GET
  @Path("record/detail/page")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewedRecordDetailPagination(@QueryParam("username") String username,
                                                    @QueryParam("assignmentName")
                                                        String assignmentName,
                                                    @QueryParam("reviewId") int reviewId,
                                                    @QueryParam("page") int page) {
    Response response = null;

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int userId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int auId = assignmentUserDbManager.getAuid(assignmentId, userId);
      PairMatching pairMatching = pairMatchingDbManager
          .getPairMatchingByAuIdReviewId(auId, reviewId);
      int order = reviewRecordDbManager.getLatestReviewOrder(pairMatching.getId());
      List<ReviewRecord> reviewRecordList = reviewRecordDbManager
          .getReviewRecordByPairMatchingId(pairMatching.getId(), order - page + 1);

      result.put("id", pairMatching.getReviewId());
      result.put("name", userDbManager.getUsername(pairMatching.getReviewId()));
      for (ReviewRecord reviewRecord: reviewRecordList) {
        JSONObject ob = new JSONObject();
        int metricsId = reviewSettingMetricsDbManager
            .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
        ob.put("score", reviewRecord.getScore());
        ob.put("feedback", reviewRecord.getFeedback());
        ob.put("time", reviewRecord.getTime());
        ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
        int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
        ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
        array.put(ob);
      }
      result.put("totalCount", order);
      result.put("pagination", page);
      result.put("Detail", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      response = Response.serverError().build();
    }
    return response;
  }

  /**
   * get all user's status of reviewing other's hw
   */
  @GET
  @Path("status/allUsers")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllReviewStatus() {
    Response response = null;

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<User> users = getStudents();
      for (User user: users) {
        String username = user.getUsername();
        Response reviewStatus = getReviewStatus(username);
        JSONObject ob = new JSONObject();
        ob.put("username", username);
        ob.put("name", user.getName());
        ob.put("display", user.getDisplay());
        ob.put("reviewStatus", new JSONArray(reviewStatus.getEntity().toString()));
        array.put(ob);
      }

      result.put("allReviewStatus", array);
      response = Response.ok(result.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
  }

  /**
   * get one user's status of reviewing other's hw
   *
   * @param username user name
   */
  @GET
  @Path("status/oneUser")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewStatus(@QueryParam("username") String username) {
    Response response = null;
    try {
      List<Assignment> assignmentList = assignmentDbManager.getAllReviewAssignment();
      int reviewId = userDbManager.getUserIdByUsername(username);
      JSONArray array = new JSONArray();

      for (Assignment assignment: assignmentList) {
        ReviewSetting reviewSetting = reviewSettingDbManager.getReviewSetting(assignment.getId());
        JSONObject ob = new JSONObject();
        ob.put("assignmentName", assignment.getName());
        ob.put("amount", reviewSetting.getAmount());
        ob.put("releaseTime", assignment.getReleaseTime());
        ob.put("deadline", assignment.getDeadline());
        ob.put("reviewReleaseTime", reviewSetting.getReleaseTime());
        ob.put("reviewDeadline", reviewSetting.getDeadline());
        ob.put("count", getReviewCompletedCount(assignment.getId(), reviewId));
        ob.put("status", reviewerStatus(assignment.getId(),
            reviewId, reviewSetting.getAmount()).getTypeName());
        array.put(ob);
      }

      response = Response.ok(array.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
  }

  /**
   * get review details from specific reviewer and assignment name
   *
   * @param username user name
   * @param assignmentName assignment name
   */
  @GET
  @Path("status/detail")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewStatusDetail(@QueryParam("username") String username,
                                        @QueryParam("assignmentName") String assignmentName) {
    Response response = null;

    try {
      JSONObject result = new JSONObject();
      JSONArray array = new JSONArray();
      int reviewId = userDbManager.getUserIdByUsername(username);
      int assignmentId = assignmentDbManager.getAssignmentIdByName(assignmentName);
      List<PairMatching> pairMatchingList = pairMatchingDbManager
          .getPairMatchingByAidAndReviewId(assignmentId, reviewId);

      for (PairMatching pairMatching: pairMatchingList) {
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
          for (ReviewRecord reviewRecord: reviewRecordList) {
            JSONObject ob = new JSONObject();
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
          reviewed.put("totalCount", order);
          reviewed.put("Detail", reviewDetailArray);
        }

        array.put(reviewed);
      }

      result.put("allStatusDetail", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      response = Response.serverError().build();
    }

    return response;
  }

  /**
   * get review details from specific reviewer, assignment name and page
   *
   * @param username user name
   * @param assignmentName assignment name
   * @param userId user id
   * @param page page
   */
  @GET
  @Path("status/detail/page")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviewedStatusDetailPagination(@QueryParam("username") String username,
                                                    @QueryParam("assignmentName")
                                                        String assignmentName,
                                                    @QueryParam("userId") int userId,
                                                    @QueryParam("page") int page) {
    Response response = null;

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
      for (ReviewRecord reviewRecord: reviewRecordList) {
        JSONObject ob = new JSONObject();
        int metricsId = reviewSettingMetricsDbManager
            .getReviewMetricsIdByRsmId(reviewRecord.getRsmId());
        ob.put("score", reviewRecord.getScore());
        ob.put("feedback", reviewRecord.getFeedback());
        ob.put("time", reviewRecord.getTime());
        ob.put("metrics", reviewMetricsDbManager.getReviewMetricsById(metricsId));
        int scoreModeId = reviewMetricsDbManager.getScoreModeIdById(metricsId);
        ob.put("scoreMode", scoreModeDbManager.getScoreModeDescById(scoreModeId).getTypeName());
        array.put(ob);
      }
      result.put("totalCount", order);
      result.put("pagination", page);
      result.put("Detail", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      response = Response.serverError().build();
    }
    return response;
  }

  /**
   *
   */
  @GET
  @Path("sourceCode")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getSourceCode(@QueryParam("username") String username,
                                @QueryParam("assignmentName") String assignmentName) {
    Response response = null;

    try {
      GitlabProject gitlabProject = gitlabService.getProject(username, assignmentName);
      GitlabAPI gitlabApi = gitlabService.getGitlab();
      byte[] buffer = gitlabApi.getFileArchive(gitlabProject);
      byte[] test = {65,66,67,68,69};
//      String filePath = "E://test.zip";
//      File file = new File(filePath);
//      OutputStream os = new FileOutputStream(file);
//      os.write(buffer);
//      os.close();
//      FileUtils.writeByteArrayToFile(new File("E://test"), buffer);

      response = Response.ok().entity(buffer).type("application/zip")
          .header("Content-Disposition", "inline; filename=\"test.zip\"").build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      System.out.println(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
  }

  /**
   * check which reviewed status of specific assignment_user
   *
   * @param auId assignment_user id
   * @param commitRecordCount commit record count
   */
  public String reviewedRecordStatus(int auId, int commitRecordCount)
      throws SQLException {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);
    String resultStatus = "INIT";

    if (commitRecordCount == 1) {
      return resultStatus;
    }

    for (PairMatching pairMatching: pairMatchingList) {
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
   *  check reviewer status of his/her review job
   *
   * @param aid assignment id
   * @param reviewId user id
   */
  public ReviewStatusEnum reviewerStatus(int aid, int reviewId, int amount) throws SQLException {
    List<PairMatching> pairMatchingList =
        pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    ReviewStatusEnum resultStatus = ReviewStatusEnum.INIT;
    int initCount = 0;

    for (PairMatching pairMatching: pairMatchingList) {
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

  /**
   * get count about how many hw have reviewer reviewed
   *
   * @param aid assignment id
   * @param reviewId user id
   */
  public int getReviewCompletedCount(int aid, int reviewId) throws SQLException {
    List<PairMatching> pairMatchingList =
        pairMatchingDbManager.getPairMatchingByAidAndReviewId(aid, reviewId);
    int count = 0;

    for (PairMatching pairMatching: pairMatchingList) {
      if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.COMPLETED)) {
        count++;
      }
    }

    return count;
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
