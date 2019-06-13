package fcu.selab.progedu.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.db.ScreenshotRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;

@Path("commits/screenshot/")
public class ScreenshotRecordService {
  private static final String JOB = "/job/";
  private static final String JSON = "/api/json";
  private static final String AUTHORIZATION = "Authorization";
  private static final String BASIC = "Basic ";
  private static final String UTF_8 = "UTF-8";

  JenkinsConfig jenkinsData;
  JenkinsApi jenkins;
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  ScreenshotRecordDbManager db = ScreenshotRecordDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  ProjectDbManager projectDb = ProjectDbManager.getInstance();

  public ScreenshotRecordService() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsApi();
  }

  /**
   * Jenkins job next build number
   * 
   * @return next build number
   */
  @GET
  @Path("nextCommitNumber/")
  @Produces(MediaType.TEXT_PLAIN)
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
   * @param proName
   *          project name
   * @param url
   *          screenshot png url
   */
  @POST
  @Path("updateURL")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateScreenshotPng(@FormParam("proName") String proName,
      @FormParam("url") List<String> url) {
    String[] userJob = proName.split("_");
    JSONObject ob = new JSONObject();
    if (!userJob[0].equals("root")) {
      int lastCommitNum = getJenkinsNextBuildNumber(proName);
      int id = userDb.getUser(userJob[0]).getId();
      System.out.println("url " + url);
      db.insertJenkinsCommitCount(id, userJob[1], lastCommitNum, url);

      ob.put("userName", userJob[0]);
      ob.put("proName", userJob[1]);
      ob.put("commitCount", lastCommitNum);
      ob.put("url", url);
    }

    return Response.ok().entity(ob.toString()).build();
  }

}