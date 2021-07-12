package fcu.selab.progedu.service;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.UserDbManager;


@RestController
@RequestMapping(value = "/commits")
public class CommitRecordService {

  private JenkinsService jenkinsService = JenkinsService.getInstance();
  private GitlabService gitlabService = GitlabService.getInstance();
  private AssignmentUserDbManager assignmentUserDb = AssignmentUserDbManager.getInstance();
  private AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
  private UserDbManager userDb = UserDbManager.getInstance();
  private CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();

  /**
   * get GitLab project url
   * 
   * @param username       username
   * @param assignmentName assignmentName
   */
  @GetMapping("/gitLab")
  public ResponseEntity<Object> getGitLabURL(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    
    String projectUrl = gitlabService.getProjectUrl(username, assignmentName);

    JSONObject gitLabEntity = new JSONObject();
    gitLabEntity.put("url", projectUrl);

    return new ResponseEntity<Object>(gitLabEntity, headers, HttpStatus.OK);
  }

  /**
   * get a part of student build detail info
   * 
   * @param username       student id
   * @param assignmentName assignment name
   * @param currentPage current page
   * @return build detail
   */
  @GetMapping("/partCommitRecords")
  public ResponseEntity<Object> getPartCommitRecord(
          @RequestParam("username") String username,
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("currentPage") int currentPage) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    
    JSONArray jsonArray = new JSONArray();
    String jobName = username + "_" + assignmentName;
    int aid = assignmentDb.getAssignmentIdByName(assignmentName);
    int uid = userDb.getUserIdByUsername(username);
    int auId = assignmentUserDb.getAuid(aid, uid);
    List<CommitRecord> commitRecords = commitRecordDb.getPartCommitRecord(auId, currentPage);
    int totalCommit = commitRecordDb.getCommitCount(auId);
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss.S");

    for (CommitRecord commitRecord : commitRecords) {
      int number = commitRecord.getNumber();
      String message = jenkinsService.getCommitMessage(jobName, number);
      Date dateTime = commitRecord.getTime();
      String status = commitRecord.getStatus().getType();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("totalCommit", totalCommit);
      jsonObject.put("number", number);
      jsonObject.put("status", status.toUpperCase());
      jsonObject.put("time", dateFormat.format(dateTime));
      jsonObject.put("message", message);

      jsonArray.add(jsonObject);
    }

    return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
  }

  public void getAutoAssessment() {

  }

  // getAssignmentFeedback
  public void getFeedback() {

  }

  // getAllStudentCommitRecord
  public void getAllUsersCommitRecord() {

  }

}
