package fcu.selab.progedu.service;

import java.sql.SQLException;
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
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.ProjectScreenshotRecordDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;

@Path("groups/commits/screenshot/")
public class GroupScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  ProjectScreenshotRecordDbManager db = ProjectScreenshotRecordDbManager.getInstance();
  GroupDbService gdb = GroupDbService.getInstance();
  ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  ProjectDbService pdb = ProjectDbService.getInstance();

  public GroupScreenshotRecordService() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsService();
  }

  /**
   * update stu project commit record.
   * 
   * @param projectName assignment name
   * @param urls        screenshot png file name
   * @throws SQLException SQLException
   */
  @POST
  @Path("updateURL")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateScreenshotPng(@FormParam("username") String groupName,
      @FormParam("assignmentName") String projectName, @FormParam("url") List<String> urls) {
    System.out.println("username: " + groupName + "jobName: " + projectName);
    JSONObject ob = new JSONObject();
    System.out.println("Png file name " + urls);
    int pgid = pgdb.getId(groupName, projectName);
    CommitRecord commitResult = pdb.getCommitResult(pgid);
    int lastCommitNum = commitResult.getNumber();
    int pcrid = commitResult.getId();

    try {
      for (String url : urls) {
        String screenShotUrl = "/job/" + groupName + "_" + projectName + "/" + lastCommitNum
            + "/artifact/target/screenshot/" + url + ".png";
        db.addProjectScreenshotRecord(pcrid, screenShotUrl);
      }
      ob.put("groupName", groupName);
      ob.put("projectName", projectName);
      ob.put("commitNumber", lastCommitNum);
      ob.put("url", urls);
      return Response.ok().entity(ob.toString()).build();
    } catch (Exception e) {
      System.out.print("update URL to DB error: ");
      e.printStackTrace();
    }
    return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
  }

  /**
   * update stu project commit record.
   *
   * @param projectName assignment name
   * @return urls screenshot urls
   * @throws SQLException SQLException
   */
  @GET
  @Path("getScreenshotURL")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getScreenshotPng(@QueryParam("username") String groupName,
      @QueryParam("assignmentName") String projectName, @QueryParam("commitNumber") int number) {
    JSONObject ob = new JSONObject();
    int pgid = pgdb.getId(groupName, projectName);
    System.out.println(pgid);
    int pcrid = pdb.getCommitRecordId(pgid, number);
    System.out.println(pcrid);
    try {
      List<String> urls = db.getScreenshotUrl(pcrid);
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
