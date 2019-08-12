package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;

@Path("commits/")
public class CommitRecordService {
  CommitRecordDbManager db = CommitRecordDbManager.getInstance();
  AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GET
  @Path("allUsers")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getAllUsersCommitRecord() {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    List<User> users = userDb.listAllUsers();
    for (User user : users) {
      String username = user.getName();
      Response userCommitRecord = getOneUserCommitRecord(username);
      JSONObject ob = new JSONObject();
      ob.put("user", user);
      ob.put("commitRecord", userCommitRecord);
    }
    result.put("allUsersCommitRecord", array);

    return Response.ok().entity(result.toString()).build();
  }

  /**
   * get all commit record of one student.
   *
   * @return homework, commit status, commit number
   */
  @GET
  @Path("oneUser")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getOneUserCommitRecord(@FormParam("user") String username) {
    int userId = userDb.getUserIdByUsername(username);
    List<Integer> aids = auDb.getAIds(userId);
    List<Integer> auIds = new ArrayList<>();

    for (int assignment : aids) {
      int auId = auDb.getAUId(assignment, userId);
      auIds.add(auId);
    }

    int latestAuId = Collections.max(auIds);
    int latestAid = auDb.getAid(latestAuId);
    String assignmentName = assignmentDb.getAssignmentNameById(latestAid);
    JSONArray array = new JSONArray();
    JSONObject ob = new JSONObject();

    ob.put("assignmentName", assignmentName);
    ob.put("commitRecord", db.getLastCommitRecord(latestAuId));

    JSONObject result = new JSONObject();
    result.put("oneUserCommitRecord", ob);

    return Response.ok().entity(result.toString()).build();

  }

  /**
   * get student build detail info
   * 
   * @param username       student id
   * @param assignmentName assignment name
   * @return build detail
   */
  @GET
  @Path("buildDetail")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitRecord(@QueryParam("username") String username,
      @QueryParam("assignmentName") String assignmentName) {
    int auId = auDb.getAUId(userDb.getUserIdByUsername(username),
        assignmentDb.getAssignmentIdByName(assignmentName));
    JSONObject buildDetail = db.getCommitRecord(auId);
    // ob.put("message", commitMessage);
    return Response.ok().entity(buildDetail.toString()).build();
  }

  /**
   * update user assignment commit record to DB.
   * 
   * @param auId assignmentUser id
   */
  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult(@FormParam("auId") int auId) {
    JSONObject ob = new JSONObject();

    int commitNumber = db.getCommitCount(auId) + 1;
    int status = db.getCommitStatusbyAUId(auId);
    String time = db.getCommitTimebyAUId(auId);

    db.insertCommitRecord(auId, commitNumber, status, time);

    ob.put("auId", auId);
    ob.put("commitNumber", commitNumber);
    ob.put("time", time);
    ob.put("status", status);

    return Response.ok().entity(ob.toString()).build();
  }

}
