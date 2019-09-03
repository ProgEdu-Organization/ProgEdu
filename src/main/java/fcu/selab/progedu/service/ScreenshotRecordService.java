package fcu.selab.progedu.service;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

@Path("commits/screenshot/")
public class ScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  ScreenshotRecordDbManager db = ScreenshotRecordDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();

  public ScreenshotRecordService() {
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
  public Response updateScreenshotPng(@FormParam("userName") String userName,
      @FormParam("assignmentName") String assignmentName, @FormParam("url") List<String> urls) {
    System.out.println("userName: " + userName + "jobName: " + assignmentName);
    JSONObject ob = new JSONObject();
    if (!userName.equals("root")) {

      System.out.println("Png file name " + urls);
      int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
          userDb.getUserIdByUsername(userName));
      int lastCommitNum = commitRecordDb.getCommitCount(auid);
      int crId = commitRecordDb.getCommitRecordId(auid, lastCommitNum);

      try {
        for (String url : urls) {
          String screenShotUrl = "/job/" + userName + "_" + assignmentName + "/" + lastCommitNum
              + "/artifact/target/screenshot/" + url + ".png";
          db.addScreenshotRecord(crId, screenShotUrl);
        }
        ob.put("userName", userName);
        ob.put("proName", assignmentName);
        ob.put("commitCount", lastCommitNum);
        ob.put("url", urls);
        return Response.ok().entity(ob.toString()).build();
      } catch (Exception e) {
        System.out.print("update URL to DB error: ");
        e.printStackTrace();
      }
    }
    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
  }

}