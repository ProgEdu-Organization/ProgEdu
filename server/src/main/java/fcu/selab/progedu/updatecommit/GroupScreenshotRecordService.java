package fcu.selab.progedu.updatecommit;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.ProjectScreenshotRecordDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.utils.ExceptionUtil;

@Path("groups/commits/screenshot/")
public class GroupScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  ProjectScreenshotRecordDbManager db = ProjectScreenshotRecordDbManager.getInstance();
  GroupDbService gdb = GroupDbService.getInstance();
  ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  ProjectDbService pdb = ProjectDbService.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupScreenshotRecordService.class);

  public GroupScreenshotRecordService() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsService();
  }

  /**
   * update stu project commit record.
   *
   * @param projectName assignment name
   * @param urls screenshot png file name
   * @throws SQLException SQLException
   */
  @POST
  @Path("updateURL")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateScreenshotPng(
          @FormParam("username") String groupName,
          @FormParam("assignmentName") String projectName,
          @FormParam("url") List<String> urls) {
    JSONObject ob = new JSONObject();
    int pgid = pgdb.getId(groupName, projectName);
    CommitRecord commitResult = pdb.getCommitResult(pgid);
    int lastCommitNum = commitResult.getNumber();
    int pcrid = commitResult.getId();
    try {
      for (String url : urls) {
        String screenShotUrl =
                "/job/"
                        + groupName
                        + "_"
                        + projectName
                        + "/"
                        + lastCommitNum
                        + "/artifact/target/screenshot/"
                        + url
                        + ".png";
        db.addProjectScreenshotRecord(pcrid, screenShotUrl);
      }
      ob.put("groupName", groupName);
      ob.put("projectName", projectName);
      ob.put("commitNumber", lastCommitNum);
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
   * @param projectName assignment name
   * @return urls screenshot urls
   * @throws SQLException SQLException
   */
  @GET
  @Path("getScreenshotURL")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getScreenshotPng(
          @QueryParam("groupName") String groupName,
          @QueryParam("projectName") String projectName,
          @QueryParam("commitNumber") int number) {
    JSONObject ob = new JSONObject();
    int pgid = pgdb.getId(groupName, projectName);
    int pcrid = pdb.getCommitRecordId(pgid, number);
    try {
      List<String> urls = db.getScreenshotUrl(pcrid);
      for (String url : urls) {
        urls.set(urls.indexOf(url), jenkinsData.getJenkinsHostUrl() + url);
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

