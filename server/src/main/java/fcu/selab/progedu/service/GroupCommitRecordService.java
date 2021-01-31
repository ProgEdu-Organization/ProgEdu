package fcu.selab.progedu.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJob2StatusFactory;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJobStatus;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusAnalysisFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

@Path("groups")
public class GroupCommitRecordService {
  private JenkinsService js = JenkinsService.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private GroupDbService gdb = GroupDbService.getInstance();
  private ProjectDbService gpdb = ProjectDbService.getInstance();
  private ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupCommitRecordService.class);

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GET
  @Path("/commits")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllGroupCommitRecord() {
    JSONArray array = new JSONArray();
    List<Group> groups = gdb.getGroups();
    for (Group group : groups) {
      Response response = getResult(group.getGroupName());

      JSONObject ob = new JSONObject();
      ob.put("groupName", group.getGroupName());
      ob.put("commitRecord", new JSONArray(response.getEntity().toString()));
      array.put(ob);
    }
    return Response.ok().entity(array.toString()).build();
  }

  /**
   * Display
   *
   * @param username username
   */
  @GET
  @Path("/{username}/commits")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitRecordByUsername(@PathParam("username") String username) {
    GroupService gs = new GroupService();
    int uid = dbManager.getUserIdByUsername(username);
    List<String> groupNames = gdb.getGroupNames(uid);
    JSONArray array = new JSONArray();
    for (String groupName : groupNames) {
      Response response = getResult(groupName);
      JSONObject ob = new JSONObject();
      ob.put("groupName", groupName);
      ob.put("commitRecord", new JSONArray(response.getEntity().toString()));
      array.put(ob);
    }
    return Response.ok().entity(array.toString()).build();
  }

  /**
   * get all commit record of one student.
   *
   * @param groupName group name
   * @return homework, commit status, commit number
   */
  @GET
  @Path("/{name}/commits/result")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getResult(@PathParam("name") String groupName) {
    JSONArray array = new JSONArray();
    JSONObject ob = new JSONObject();
    List<Integer> pgids = gpdb.getPgids(groupName);
    for (int pgid : pgids) {
      GroupProject project = gpdb.getProject(pgid);
      ob.put("name", project.getName());
      CommitRecord cr = gpdb.getCommitResult(pgid);
      ob.put("releaseTime", project.getReleaseTime());
      if (cr != null) {
        ob.put("number", cr.getNumber());
        ob.put("status", cr.getStatus().getType());
      }
      array.put(ob);
    }
    return Response.ok(array.toString()).build();

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
    //    return Response.ok(array.toString()).build();
  }

  /**
   * get student build detail info
   *
   * @param groupName student id
   * @param projectName assignment name
   * @return build detail
   */
  @GET
  @Path("/{name}/projects/{projectName}/commits")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitRecord(
      @PathParam("name") String groupName, @PathParam("projectName") String projectName) {
    JenkinsService js = JenkinsService.getInstance();
    JSONArray array = new JSONArray();
    int pgid = gpdb.getPgid(groupName, projectName);
    List<CommitRecord> commitRecords = gpdb.getCommitRecords(pgid);
    String jobName = groupName + "_" + projectName;
    for (CommitRecord commitRecord : commitRecords) {
      int number = commitRecord.getNumber();
      String message = js.getCommitMessage(jobName, number);
      Date time = commitRecord.getTime();
      String status = commitRecord.getStatus().getType();
      String committer = commitRecord.getCommitter();

      JSONObject ob = new JSONObject();

      ob.put("number", number);
      ob.put("status", status.toUpperCase());
      ob.put("time", time);
      ob.put("message", message);
      ob.put("committer", committer);
      array.put(ob);
    }

    return Response.ok(array.toString()).build();
  }

  /**
   * get student build detail info
   *
   * @param groupName student id
   * @param projectName assignment name
   * @param currentPage current page
   * @return build detail
   */
  @GET
  @Path("/{name}/projects/{projectName}/partCommits/{currentPage}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPartCommitRecord(
      @PathParam("name") String groupName, @PathParam("projectName") String projectName, 
      @PathParam("currentPage") int currentPage) {
    JenkinsService js = JenkinsService.getInstance();
    JSONArray array = new JSONArray();
    int pgid = gpdb.getPgid(groupName, projectName);
    List<CommitRecord> commitRecords = gpdb.getPartCommitRecords(pgid,currentPage);
    String jobName = groupName + "_" + projectName;
    int totalCommit = gpdb.getCommitCount(pgid);

    for (CommitRecord commitRecord : commitRecords) {
      int number = commitRecord.getNumber();
      String message = js.getCommitMessage(jobName, number);
      Date time = commitRecord.getTime();
      String status = commitRecord.getStatus().getType();
      String committer = commitRecord.getCommitter();
      JSONObject ob = new JSONObject();

      ob.put("totalCommit", totalCommit);
      ob.put("number", number);
      ob.put("status", status.toUpperCase());
      ob.put("time", time);
      ob.put("message", message);
      ob.put("committer", committer);
      array.put(ob);
    }

    return Response.ok(array.toString()).build();
  }

  /**
   * update user assignment commit record to DB.
   *
   * @param groupName username
   * @param projectName assignment name
   * @throws ParseException (to do)
   */
  @POST
  @Path("/commits/update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateCommitRecord(
      @FormParam("user") String groupName, @FormParam("proName") String projectName) {
    ProjectTypeEnum projectTypeEnum = gpdb.getProjectType(projectName);
    int pgid = pgdb.getId(groupName, projectName);
    CommitRecord cr = gpdb.getCommitResult(pgid);
    int commitNumber = 1;
    if (cr != null) {
      commitNumber = cr.getNumber() + 1;
    }
    Date date = new Date();
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    try {
      date = time.parse(time.format(Calendar.getInstance().getTime()));
    } catch (ParseException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    JenkinsJobStatus jobStatus = JenkinsJob2StatusFactory.createJenkinsJobStatus(projectTypeEnum);
    String jobName = groupName + "_" + projectName;
    StatusEnum statusEnum = jobStatus.getStatus(jobName, commitNumber);

    String committer = js.getCommitter(jobName, commitNumber);
    gpdb.insertProjectCommitRecord(pgid, commitNumber, statusEnum, date, committer);
    JSONObject ob = new JSONObject();
    ob.put("pgid", pgid);
    ob.put("commitNumber", commitNumber);
    ob.put("time", time);
    ob.put("status", statusEnum.getType());
    ob.put("committer", committer);

    return Response.ok().entity(ob.toString()).build();
  }
  
  /**
   * update user assignment commit record to DB.
   *
   * @param groupName username
   * @param projectName assignment name
   * @param number commit number
   * @throws ParseException (to do)
   */
  @GET
  @Path("/{name}/projects/{projectName}/feedback/{num}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFeedback(
      @PathParam("name") String groupName,
      @PathParam("projectName") String projectName,
      @PathParam("num") int number) {
    JenkinsService js = JenkinsService.getInstance();
    ProjectTypeEnum projectTypeEnum = gpdb.getProjectType(projectName);

    String jobName = js.getJobName(groupName, projectName);
    String console = js.getConsole(jobName, number);
    int pgid = pgdb.getId(groupName, projectName);

    StatusEnum statusType = gpdb.getCommitRecordStatus(pgid, number);

    Status statusAnalysis = StatusAnalysisFactory.getStatusAnalysis(projectTypeEnum,
                                                                    statusType.getType());

    String message = statusAnalysis.extractFailureMsg(console);
    ArrayList feedBacks = statusAnalysis.formatExamineMsg(message);
    String feedBackMessage = statusAnalysis.tojsonArray(feedBacks);

    return Response.ok().entity(feedBackMessage).build();
  }
}
