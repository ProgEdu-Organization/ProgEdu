package fcu.selab.progedu.updatecommit;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.ProjectScreenshotRecordDbManager;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import net.minidev.json.JSONObject;

import javax.ws.rs.QueryParam;


@RestController
@RequestMapping(value ="/publicApi/groups/commits/screenshot")
public class GroupScreenshotRecordService {
  JenkinsConfig jenkinsData;
  JenkinsService jenkins;
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupScreenshotRecordService.class);
  ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  ProjectDbService pdb = ProjectDbService.getInstance();
  ProjectScreenshotRecordDbManager db = ProjectScreenshotRecordDbManager.getInstance();

  public GroupScreenshotRecordService() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsService();
  }


  @PostMapping("/updateURL")
  public ResponseEntity<Object> updateScreenshotPng(
          @RequestParam("username") String groupName,
          @RequestParam("assignmentName") String projectName,
          @RequestParam("url") List<String> urls) {

    HttpHeaders headers = new HttpHeaders();
    //



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
      return new ResponseEntity<>(ob, headers, HttpStatus.OK);
    } catch (Exception e) {
      System.out.print("update URL to DB error: ");
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  @GetMapping("getScreenshotURL")
  public ResponseEntity<Object> getScreenshotPng(
          @QueryParam("groupName") String groupName,
          @QueryParam("projectName") String projectName,
          @QueryParam("commitNumber") int commitNumber) {



    JSONObject ob = new JSONObject();
    int pgid = pgdb.getId(groupName, projectName);
    int pcrid = pdb.getCommitRecordId(pgid, commitNumber);
    try {
      List<String> urls = db.getScreenshotUrl(pcrid);
      for (String url : urls) {
        urls.set(urls.indexOf(url), jenkinsData.getJenkinsHostUrl() + url);
      }
      ob.put("urls", urls);
      return new ResponseEntity<>(ob, HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }



}
