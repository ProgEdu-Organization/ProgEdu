package fcu.selab.progedu.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.PairMatching;
import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;

import fcu.selab.progedu.db.PairMatchingDbManager;
import fcu.selab.progedu.db.ReviewSettingDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("peerReview")
public class PeerReviewService {
  private CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private ReviewSettingDbManager reviewSettingDbManager = ReviewSettingDbManager.getInstance();
  private PairMatchingDbManager pairMatchingDbManager = PairMatchingDbManager.getInstance();
  private UserDbManager userDbManager = UserDbManager.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewService.class);

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
   *  get one user commit result which is assigned by peer review
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
        JSONObject ob = new JSONObject();
        int commitRecordCount = commitRecordDbManager.getCommitCount(auId);
        ob.put("assignmentName", assignment.getName());
        ob.put("releaseTime", assignment.getReleaseTime());
        ob.put("commitRecordCount", commitRecordCount);
        ob.put("reviewStatus", reviewedRecordStatus(auId, commitRecordCount).getTypeName());
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

  public ReviewStatusEnum reviewedRecordStatus(int auId, int commitRecordCount)
      throws SQLException {
    List<PairMatching> pairMatchingList = pairMatchingDbManager.getPairMatchingByAuId(auId);
    ReviewStatusEnum resultStatus = ReviewStatusEnum.INIT;

    if (commitRecordCount > 1) {
      return resultStatus;
    }

    for (PairMatching pairMatching: pairMatchingList) {
      if (pairMatching.getScoreModeEnum().equals(ReviewStatusEnum.UNCOMPLETED)) {
        resultStatus = ReviewStatusEnum.UNCOMPLETED;
        break;
      } else if (pairMatching.getScoreModeEnum().equals(ReviewStatusEnum.COMPLETED)) {
        resultStatus = ReviewStatusEnum.COMPLETED;
      }
    }

    return resultStatus;
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
