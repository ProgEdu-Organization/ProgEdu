package fcu.selab.progedu.updatecommit;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.db.*;
import fcu.selab.progedu.utils.ExceptionUtil;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value ="/publicApi/commits/screenshot")
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

  @PostMapping("updateURL")
  public ResponseEntity<Object> updateScreenshotPng(@RequestParam("username") String username,
                                      @RequestParam("assignmentName") String assignmentName,
                                      @RequestParam("url") List<String> urls) {
    HttpHeaders headers = new HttpHeaders();

    //

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
      return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);

    } catch (Exception e) {
      System.out.print("update URL to DB error: ");
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("getScreenshotURL")
  public ResponseEntity<Object> getScreenshotURL(@QueryParam("username") String username,
                                      @QueryParam("assignmentName") String assignmentName,
                                      @QueryParam("commitNumber") String commitNumber) {


    JSONObject ob = new JSONObject();
    int auid = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
    int crId = commitRecordDb.getCommitRecordId(auid, Integer.valueOf(commitNumber));
    try {
      ArrayList<String> urls = db.getScreenshotUrl(crId);
      for (String url: urls) {
        urls.set(urls.indexOf(url),jenkinsData.getJenkinsHostUrl() + url);
      }
      ob.put("urls", urls);
      return new ResponseEntity<Object>(ob, HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

}
