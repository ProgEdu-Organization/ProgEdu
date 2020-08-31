package fcu.selab.progedu.service;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.ReviewSetting;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;

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
      response = Response.ok().build();
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
  public Response getReviewRecord(@QueryParam("username") String username) {
    Response response = null;
    try {
      List<Assignment> assignmentList = assignmentDbManager.getAllAssignment();
      List<ReviewSetting> reviewSettingList = reviewSettingDbManager.getAllReviewSetting();
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().build();
    }

    return response;
  }
}
