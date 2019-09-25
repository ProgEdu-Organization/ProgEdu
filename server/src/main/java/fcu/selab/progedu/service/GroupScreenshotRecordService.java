package fcu.selab.progedu.service;

import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.ScreenshotRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;

@Path("groups/commits/screenshot/")
public class GroupScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  ScreenshotRecordDbManager db = ScreenshotRecordDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();

  public GroupScreenshotRecordService() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsService();
  }

  /**
   * update stu project commit record.
   * 
   * @param assignmentName assignment name
   * @param urls           screenshot png file name
   * @throws SQLException SQLException
   */
  @POST
  @Path("updateURL")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateScreenshotPng(@FormParam("username") String username,
      @FormParam("assignmentName") String assignmentName, @FormParam("url") List<String> urls) {
    return null;
//    System.out.println("username: " + username + "jobName: " + assignmentName);
//    JSONObject ob = new JSONObject();
//    System.out.println("Png file name " + urls);
//    int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
//        userDb.getUserIdByUsername(username));
//    int lastCommitNum = commitRecordDb.getCommitCount(auid);
//    int crId = commitRecordDb.getCommitRecordId(auid, lastCommitNum);
//
//    try {
//      for (String url : urls) {
//        String screenShotUrl = "/job/" + username + "_" + assignmentName + "/" + lastCommitNum
//            + "/artifact/target/screenshot/" + url + ".png";
//        db.addScreenshotRecord(crId, screenShotUrl);
//      }
//      ob.put("username", username);
//      ob.put("proName", assignmentName);
//      ob.put("commitCount", lastCommitNum);
//      ob.put("url", urls);
//      return Response.ok().entity(ob.toString()).build();
//    } catch (Exception e) {
//      System.out.print("update URL to DB error: ");
//      e.printStackTrace();
//    }
//    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
  }

  /**
   * update stu project commit record.
   *
   * @param assignmentName assignment name
   * @return urls screenshot urls
   * @throws SQLException SQLException
   */
  @GET
  @Path("getScreenshotURL")
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateScreenshotPng(@QueryParam("username") String username,
      @QueryParam("assignmentName") String assignmentName, @QueryParam("commitNumber") int number) {
    JSONObject ob = new JSONObject();
    int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
        userDb.getUserIdByUsername(username));
    System.out.println(auid);
    int crId = commitRecordDb.getCommitRecordId(auid, number);
    System.out.println(crId);
    try {
      ArrayList<String> urls = db.getScreenshotUrl(crId);
      for (String url : urls) {
        urls.set(urls.indexOf(url), jenkinsData.getJenkinsHostUrl() + url);
      }
      System.out.println(urls);
      ob.put("urls", urls);
      return Response.ok().entity(ob.toString()).build();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
  }
}
