package fcu.selab.progedu.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.db.service.GroupDbService;

@Path("group/commits/")
public class GroupCommitRecordService {
//  private CommitRecordDbManager db = CommitRecordDbManager.getInstance();
//  private AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
//  private UserDbManager userDb = UserDbManager.getInstance();
//  private AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
//  private AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();
//  private CommitStatusDbManager csdb = CommitStatusDbManager.getInstance();
//  private JenkinsService js = JenkinsService.getInstance();
//  private GitlabService gs = GitlabService.getInstance();

  private GroupDbService gdb = GroupDbService.getInstance();

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllGroupCommitRecord() {
    JSONObject result = new JSONObject();
    List<Group> groups = gdb.getGroups();

//    JSONArray array = new JSONArray();

//    List<User> users = getStudents();
//    for (User user : users) {
//      String username = user.getUsername();
//      Response userCommitRecord = getOneUserCommitRecord(username);
//      JSONObject ob = new JSONObject();
//      ob.put("name", user.getName());
//      ob.put("username", user.getUsername());
//      ob.put("display", user.getDisplay());
//      ob.put("commitRecord", new JSONArray(userCommitRecord.getEntity().toString()));
//      array.put(ob);
//    }
//    result.put("allUsersCommitRecord", array);
//
//    return Response.ok().entity(result.toString()).build();
    return Response.ok().entity(result.toString()).build();
  }

  /**
   * get all commit record of one student.
   *
   * @return homework, commit status, commit number
   */
  @GET
  @Path("single")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOneUserCommitRecord(@QueryParam("name") String name) {
    int id = gdb.getId(name);

//    for (GroupProject groupProject : gpdb.get)
//    int userId = userDb.getUserIdByUsername(username);
//    JSONArray array = new JSONArray();
//    for (Assignment assignment : assignmentDb.getAllAssignment()) {
//      int auId = auDb.getAuid(assignment.getId(), userId);
//      JSONObject ob = new JSONObject();
//      ob.put("assignmentName", assignment.getName());
//      ob.put("releaseTime", assignment.getReleaseTime());
//      ob.put("commitRecord", db.getLastCommitRecord(auId));
//      array.put(ob);
//    }
    return Response.ok(array.toString()).build();
  }
//
//  /**
//   * get student build detail info
//   * 
//   * @param username       student id
//   * @param assignmentName assignment name
//   * @return build detail
//   */
//  @GET
//  @Path("commitRecords")
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response getCommitRecord(@QueryParam("username") String username,
//      @QueryParam("assignmentName") String assignmentName) {
//    JSONArray array = new JSONArray();
//    String jobName = username + "_" + assignmentName;
//    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
//        userDb.getUserIdByUsername(username));
//    List<CommitRecord> commitRecords = db.getCommitRecord(auId);
//    for (CommitRecord commitRecord : commitRecords) {
//      int number = commitRecord.getNumber();
//      String message = js.getCommitMessage(jobName, number);
//      Date time = commitRecord.getTime();
//      String status = commitRecord.getStatus().getType();
//      JSONObject ob = new JSONObject();
//
//      ob.put("number", number);
//      ob.put("status", status.toUpperCase());
//      ob.put("time", time);
//      ob.put("message", message);
//      array.put(ob);
//    }
//
//    return Response.ok(array.toString()).build();
//  }
//
//  /**
//   * update user assignment commit record to DB.
//   * 
//   * @param username       username
//   * @param assignmentName assignment name
//   * @throws ParseException (to do)
//   */
//  @POST
//  @Path("update")
//  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response updateCommitResult(@FormParam("user") String username,
//      @FormParam("proName") String assignmentName) throws ParseException {
//
//    JSONObject ob = new JSONObject();
//    AssignmentType assignmentType = AssignmentFactory.getAssignmentType(
//        atDb.getTypeNameById(assignmentDb.getAssignmentTypeId(assignmentName)).getTypeName());
//
//    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
//        userDb.getUserIdByUsername(username));
//    int commitNumber = db.getCommitCount(auId) + 1;
//    Date date = new Date();
//    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
//    date = time.parse(time.format(Calendar.getInstance().getTime()));
//
//    StatusEnum statusEnum = assignmentType.checkStatusType(commitNumber, username, assignmentName);
//    db.insertCommitRecord(auId, commitNumber, statusEnum, date);
//
//    ob.put("auId", auId);
//    ob.put("commitNumber", commitNumber);
//    ob.put("time", time);
//    ob.put("status", statusEnum.getType());
//
//    return Response.ok().entity(ob.toString()).build();
//  }
//
//  /**
//   * (to do)
//   * 
//   * @param assignmentName (to do)
//   */
//  public void deleteRecord(String assignmentName) {
//    int aid = assignmentDb.getAssignmentIdByName(assignmentName);
//    List<Integer> uids = auDb.getUids(aid);
//
//    for (int uid : uids) {
//      int auId = auDb.getAuid(aid, uid);
//      db.deleteRecord(auId);
//    }
//  }
//
//  /**
//   * update user assignment commit record to DB.
//   * 
//   * @param username       username
//   * @param assignmentName assignment name
//   * @throws ParseException (to do)
//   */
//  @GET
//  @Path("feedback")
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response getFeedback(@QueryParam("username") String username,
//      @QueryParam("assignmentName") String assignmentName, @QueryParam("number") int number) {
//    JenkinsService js = JenkinsService.getInstance();
//    JSONObject ob = new JSONObject();
//    AssignmentType assignmentType = getAssignmentType(assignmentName);
//    String jobName = username + "_" + assignmentName;
//    String console = js.getConsole(jobName, number);
//    int auId = getAuid(username, assignmentName);
//    String statusType = getStatusTypeName(auId, number);
//    String message = assignmentType.getStatus(statusType).extractFailureMsg(console);
//    ob.put("message", message);
//
//    return Response.ok().entity(ob.toString()).build();
//  }
//
//  /**
//   * get GitLab project url
//   * 
//   * @param username       username
//   * @param assignmentName assignmentName
//   */
//  @GET
//  @Path("gitLab")
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response getGitLabProjectUrl(@QueryParam("username") String username,
//      @QueryParam("assignmentName") String assignmentName) {
//    JSONObject ob = new JSONObject();
//    String projectUrl = gs.getProjectUrl(username, assignmentName);
//    ob.put("url", projectUrl);
//
//    return Response.ok().entity(ob.toString()).build();
//  }
//
//  private AssignmentType getAssignmentType(String assignmentName) {
//    AssignmentDbManager adb = AssignmentDbManager.getInstance();
//    AssignmentTypeDbManager atdb = AssignmentTypeDbManager.getInstance();
//    int typeId = adb.getAssignmentTypeId(assignmentName);
//    ProjectTypeEnum type = atdb.getTypeNameById(typeId);
//    return AssignmentFactory.getAssignmentType(type.getTypeName());
//  }
//
//  private int getAuid(String username, String assignmentName) {
//    return auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
//        userDb.getUserIdByUsername(username));
//  }
//
//  private String getStatusTypeName(int auId, int number) {
//    int statusId = db.getCommitRecordStatus(auId, number);
//    return csdb.getStatusNameById(statusId).getType();
//  }
//
//  /**
//   * Get all user which role is student
//   * 
//   * @return all GitLab users
//   */
//  public List<User> getStudents() {
//    List<User> studentUsers = new ArrayList<>();
//    List<User> users = userDb.getAllUsers();
//
//    for (User user : users) {
//      if (user.getRole().contains(RoleEnum.STUDENT)) {
//        studentUsers.add(user);
//      }
//    }
//    return studentUsers;
//  }

}