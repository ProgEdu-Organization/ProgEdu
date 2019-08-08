package fcu.selab.progedu.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import fcu.selab.progedu.conn.StudentDashChoosePro;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.status.StatusEnum;

@Path("commits/")
public class CommitResultService {
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
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    int userId = userDb.getUserIdByUsername(username);
    List<Integer> aIds = auDb.getAIds(userId);

    for (int assignment : aIds) {
      int auIds = auDb.getAUId(assignment, userId);
      String assignmentName = assignmentDb.getAssignmentNameById(assignment);
      JSONObject ob = new JSONObject();
      ob.put("assignmentName", assignmentName);
      ob.put("commitRecord", db.getCommitRecord(auIds));
      array.put(ob);
    }
    result.put("oneUserCommitRecord", array);
    return Response.ok().entity(result.toString()).build();
  }

  /**
   * get student build detail info
   * 
   * @param username
   *          student id
   * @param assignmentName
   *          assignment name
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
   * get commit result by stuId and hw.
   * 
   * @param userName student id
   * @param proName  project name
   * @return color
   */
  public String getCommitResult(String userName, String proName) {
    int id = userDb.getUser(userName).getId();
    return db.getCommitResultByStudentAndHw(id, proName).getStatus();
  }

  /**
   * update stu project commit record.
   * 
   * @param userName stu id
   * @param proName  project name
   */
  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult(@FormParam("user") String userName,
      @FormParam("proName") String proName) {
    JSONObject ob = new JSONObject();
    if (!userName.equals("root")) {

      JenkinsService jenkinsService = new JenkinsService();
      StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();

      int lastCommitNum = jenkinsService.getProjectCommitCount(proName, userName);
      int commitCount = lastCommitNum - 1;
      int proType = assignmentDb.getAssignmentType(proName);
      String buildApiJson = stuDashChoPro.getBuildApiJson(lastCommitNum, userName, proName);
      String strDate = stuDashChoPro.getCommitTime(buildApiJson);
      String[] dates = strDate.split(" ");
      String status = stuDashChoPro.getCommitStatus(lastCommitNum, userName, proName, buildApiJson,
          proType);
      int id = userDb.getUser(userName).getId();

      boolean check = db.checkJenkinsJobTimestamp(id, proName);
      if (check) {
        db.updateJenkinsCommitCount(id, proName, commitCount, status);

      } else {
        db.insertJenkinsCommitCount(id, proName, commitCount, status);
      }
      db.updateJenkinsJobTimestamp(id, proName, strDate);

      boolean inDb = commitRecordDb.checkRecord(id, proName, dates[0], dates[1]);
      if (inDb) {
        commitRecordDb.updateRecordStatus(id, proName, status, dates[0], dates[1]);
      } else {
        commitRecordDb.insertCommitRecord(id, proName, status, dates[0], dates[1]);
      }

      updateCommitRecordState();

      ob.put("userName", userName);
      ob.put("proName", proName);
      ob.put("commitCount", commitCount);
      ob.put("dates", dates[0]);
      ob.put("dates1", dates[1]);
      ob.put("status", status);
    }

    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * update Commit_Record_State DB's data.
   */
  public void updateCommitRecordState() {

    List<String> lsNames = new ArrayList<>();
    lsNames = assignmentDb.listAllAssignmentNames();

    for (String name : lsNames) {

      Map<String, Integer> map = commitRecordDb.getCommitRecordStateCounts(name);

      int bs = 0;
      int ini = 0;
      int utf = 0;
      int csf = 0;
      int cpf = 0;

      if (map.containsKey(StatusEnum.BUILD_SUCCESS.getTypeName())) {
        bs = map.get(StatusEnum.BUILD_SUCCESS.getTypeName());
      }

      if (map.containsKey(StatusEnum.INITIALIZATION.getTypeName())) {
        ini = map.get(StatusEnum.INITIALIZATION.getTypeName());
      }

      if (map.containsKey(StatusEnum.UNIT_TEST_FAILURE.getTypeName())) {
        utf = map.get(StatusEnum.UNIT_TEST_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.CHECKSTYLE_FAILURE.getTypeName())) {
        csf = map.get(StatusEnum.CHECKSTYLE_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.COMPILE_FAILURE.getTypeName())) {
        cpf = map.get(StatusEnum.COMPILE_FAILURE.getTypeName());
      }

      int ccs = 0;
      ccs = bs + utf + csf + cpf;

      boolean check;
      check = crsdb.checkCommitRecordStatehw(name);

      if (check) {
        crsdb.updateCommitRecordState(name, bs, csf, cpf, utf, ini, ccs);

      } else {
        crsdb.addCommitRecordState(name, bs, csf, cpf, utf, ini, ccs);
      }

    }

  }

  /**
   * delete build result of hw.
   * 
   * @param hw hw
   */
  public void deleteResult(String hw) {
    IDatabase database = new MySqlDatabase();
    Connection connection = database.getConnection();
    db.deleteResult(hw);
  }
}
