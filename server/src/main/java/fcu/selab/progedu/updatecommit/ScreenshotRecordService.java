package fcu.selab.progedu.updatecommit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.ScreenshotRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("commits/screenshot/")
public class ScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  ScreenshotRecordDbManager db = ScreenshotRecordDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotRecordService.class);

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
  public Response updateScreenshotPng(@FormParam("username") String username,
                                      @FormParam("assignmentName") String assignmentName,
                                      @FormParam("url") List<String> urls) {
    JSONObject ob = new JSONObject();
    int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
    int lastCommitNum = commitRecordDb.getCommitCount(auid);
    int crId = commitRecordDb.getCommitRecordId(auid, lastCommitNum);

    try {
      for (String url : urls) {
        String screenShotUrl = "/job/" + username + "_" + assignmentName + "/" + lastCommitNum
                + "/artifact/target/screenshot/" + url + ".png";
        db.addScreenshotRecord(crId, screenShotUrl);
      }
      ob.put("username", username);
      ob.put("proName", assignmentName);
      ob.put("commitCount", lastCommitNum);
      ob.put("url", urls);
      return Response.ok().entity(ob.toString()).build();
    } catch (Exception e) {
      System.out.print("update URL to DB error: ");
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
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
                                      @QueryParam("assignmentName") String assignmentName,
                                      @QueryParam("commitNumber") int number) {
    JSONObject ob = new JSONObject();
    int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
    int crId = commitRecordDb.getCommitRecordId(auid, number);
    try {
      ArrayList<String> urls = db.getScreenshotUrl(crId);
      for (String url: urls) {
        urls.set(urls.indexOf(url),jenkinsData.getJenkinsHostUrl() + url);
      }
      ob.put("urls", urls);
      return Response.ok().entity(ob.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
  }

}
