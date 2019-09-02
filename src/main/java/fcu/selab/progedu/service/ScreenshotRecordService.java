package fcu.selab.progedu.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
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
import fcu.selab.progedu.exception.LoadConfigFailureException;

@Path("commits/screenshot/")
public class ScreenshotRecordService {
  private static final String JOB = "/job/";
  private static final String JSON = "/api/json";
  private static final String AUTHORIZATION = "Authorization";
  private static final String BASIC = "Basic ";
  private static final String UTF_8 = "UTF-8";

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
   * Jenkins job next build number
   * 
   * @return next build number
   */
  @GET
  @Path("nextCommitNumber/")
  @Produces(MediaType.APPLICATION_JSON)
  public int getJenkinsNextBuildNumber(@QueryParam("jobName") String jobName) {
    int nextBuildNumber = 0;
    String username = "";
    String password = "";
    String jobUrl = "";
    HttpURLConnection conn = null;
    try {
      username = jenkinsData.getJenkinsRootUsername();
      password = jenkinsData.getJenkinsRootPassword();
      jobUrl = jenkinsData.getJenkinsHostUrl() + JOB + jobName + JSON;
      System.out.println("jobUrl:" + jobUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      URL url = new URL(jobUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {

        String jsonString = reader.readLine();
        JSONObject j1 = new JSONObject(jsonString);
        nextBuildNumber = j1.optInt("nextBuildNumber") - 1;
      }

    } catch (Exception e) {
      System.out.print("Jenkins get job build result error : ");
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    System.out.println("nextBuildNumber= " + nextBuildNumber);
    return nextBuildNumber;
  }

  /**
   * update stu project commit record.
   * 
   * @param proName project name
   * @param urls    screenshot png urls
   * @throws SQLException SQLException
   */
  @POST
  @Path("updateURL")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateScreenshotPng(@FormParam("proName") String proName,
      @FormParam("url") List<String> urls) {
    String[] userJob = proName.split("_");
    String userName = userJob[0];
    String jobName = userJob[1];
    System.out.println("userName: " + userName + "jobName: " + jobName);
    JSONObject ob = new JSONObject();
    if (!userJob[0].equals("root")) {

      System.out.println("url " + urls);
      int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(jobName),
          userDb.getUserIdByUsername(userName));
      int lastCommitNum = commitRecordDb.getCommitCount(auid);
      int crId = commitRecordDb.getCommitRecordId(auid, lastCommitNum);

      try {
        for (String url : urls) {
          db.addScreenshotRecord(crId, url);
        }
        ob.put("userName", userName);
        ob.put("proName", jobName);
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