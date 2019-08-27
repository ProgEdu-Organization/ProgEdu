package fcu.selab.progedu.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import fcu.selab.progedu.db.AssignmentTypeDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.CommitStatusDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.project.AssignmentFactory;
import fcu.selab.progedu.project.AssignmentType;
import fcu.selab.progedu.status.StatusEnum;

@Path("commits/")
public class CommitRecordService {
  CommitRecordDbManager db = CommitRecordDbManager.getInstance();
  AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
  AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();
  CommitStatusDbManager csdb = CommitStatusDbManager.getInstance();

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GET
  @Path("allUsers")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllUsersCommitRecord() {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    List<User> users = userDb.getAllUsers();
    for (User user : users) {
      String username = user.getUsername();
      Response userCommitRecord = getOneUserCommitRecord(username);
      JSONObject ob = new JSONObject();
      
      ob.put("name", user.getName());
      ob.put("username", user.getUsername());
      ob.put("commitRecord",new JSONObject(userCommitRecord.getEntity().toString()));
      array.put(ob);
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
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOneUserCommitRecord(@QueryParam("username") String username) {
    System.out.println(username);
    JSONArray array = new JSONArray();
    int userId = userDb.getUserIdByUsername(username);
    List<Integer> aids = auDb.getAIds(userId);

    for (int assignment : aids) {
      int auId = auDb.getAuid(assignment, userId);
      String assignmentName = assignmentDb.getAssignmentNameById(assignment);
      JSONObject ob = new JSONObject();
      ob.put("assignmentName", assignmentName);
      ob.put("commitRecord", db.getLastCommitRecord(auId));
      array.put(ob);
    }
    JSONObject result = new JSONObject();
    result.put("oneUserCommitRecord", array);
    return Response.ok(result.toString()).build();
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
    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
        userDb.getUserIdByUsername(username));
    JSONObject buildDetail = db.getCommitRecord(auId);
    // ob.put("message", commitMessage);
    return Response.ok().entity(buildDetail.toString()).build();
  }

  /**
   * update user assignment commit record to DB.
   * 
   * @param username       username
   * @param assignmentName assignment name
   * @throws ParseException (to do)
   */
  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult(@FormParam("user") String username,
      @FormParam("proName") String assignmentName) throws ParseException {

    JSONObject ob = new JSONObject();
    AssignmentType assignmentType = AssignmentFactory.getAssignmentType(
        atDb.getTypeNameById(assignmentDb.getAssignmentType(assignmentName)).getTypeName());

    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
        userDb.getUserIdByUsername(username));
    int commitNumber = db.getCommitCount(auId) + 1;
    Date date = new Date();
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    date = time.parse(time.format(Calendar.getInstance().getTime()));

    StatusEnum statusEnum = assignmentType.checkStatusType(commitNumber, username, assignmentName);
    db.insertCommitRecord(auId, commitNumber, statusEnum, date);

    ob.put("auId", auId);
    ob.put("commitNumber", commitNumber);
    ob.put("time", time);
    ob.put("status", statusEnum.getType());

    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * update user assignment commit record to DB.
   * 
   * @throws ParseException (to do)
   */
  @POST
  @Path("update2")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult2() throws ParseException {
    String username = "d0381908";
    String assignmentName = "errr";
    JSONObject ob = new JSONObject();
    AssignmentType assignmentType = AssignmentFactory.getAssignmentType(
        atDb.getTypeNameById(assignmentDb.getAssignmentType(assignmentName)).getTypeName());

    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
        userDb.getUserIdByUsername(username));
    int commitNumber = db.getCommitCount(auId) + 1;
    Date date = new Date();
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    date = time.parse(time.format(Calendar.getInstance().getTime()));

    StatusEnum statusEnum = assignmentType.checkStatusType(commitNumber, username, assignmentName);
    db.insertCommitRecord(auId, commitNumber, statusEnum, date);

    ob.put("auId", auId);
    ob.put("commitNumber", commitNumber);
    ob.put("time", time);
    ob.put("status", statusEnum.getType());

    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * (to do)
   * 
   * @param assignmentName (to do)
   */
  public void deleteRecord(String assignmentName) {
    int aid = assignmentDb.getAssignmentIdByName(assignmentName);
    List<Integer> uids = auDb.getUids(aid);

    for (int uid : uids) {
      int auId = auDb.getAuid(aid, uid);
      db.deleteRecord(auId);
    }
  }
}
