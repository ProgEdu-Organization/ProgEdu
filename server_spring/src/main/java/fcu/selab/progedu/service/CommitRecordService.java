package fcu.selab.progedu.service;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.utils.ExceptionUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusAnalysisFactory;
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
import fcu.selab.progedu.db.CommitStatusDbManager;
import fcu.selab.progedu.db.AssignmentTypeDbManager;
import fcu.selab.progedu.data.ProjectTypeEnum;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/commits")
public class CommitRecordService {

  private JenkinsService jenkinsService = JenkinsService.getInstance();
  private GitlabService gitlabService = GitlabService.getInstance();
  private AssignmentUserDbManager assignmentUserDb = AssignmentUserDbManager.getInstance();
  private AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
  private UserDbManager userDb = UserDbManager.getInstance();
  private CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  private CommitStatusDbManager csDb = CommitStatusDbManager.getInstance();
//  private CommitRecordDbManager db = CommitRecordDbManager.getInstance();
//  private AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(CommitRecordService.class);

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
   * get all commit record from auto assessment of one student
   *
   * @param username       student id
   * @return homework, commit status, commit number
   */
  @GetMapping("/autoAssessment")
  public ResponseEntity<Object> getAutoAssessment(
      @RequestParam("username") String username) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    JSONArray jsonArray = new JSONArray();
    int userId = userDb.getUserIdByUsername(username);
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss.S");

    try {
      for (Assignment assignment: assignmentDb.getAutoAssessment()) {
        int auId = assignmentUserDb.getAuid(assignment.getId(), userId);
        JSONObject jsonObject = new JSONObject();
        JSONObject lastCommitRecord = new JSONObject();
        org.json.JSONObject lastCommitRecordJson = commitRecordDb.getLastCommitRecord(auId);

        String commitReleaseTime = dateFormat.format(lastCommitRecordJson.get("commitTime"));
        lastCommitRecord.put("commitNumber", lastCommitRecordJson.get("commitNumber"));
        lastCommitRecord.put("commitTime", commitReleaseTime);
        lastCommitRecord.put("status", lastCommitRecordJson.get("status"));

        String assignmentReleaseTime = dateFormat.format(assignment.getReleaseTime());
        jsonObject.put("assignmentName", assignment.getName());
        jsonObject.put("releaseTime", assignmentReleaseTime);
        jsonObject.put("commitRecord", lastCommitRecord);
        jsonArray.add(jsonObject);
      }
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
  }

  /**
   * get all commit record of one student.
   *
   * @return homework, commit status, commit number
   */
  @GetMapping("oneUser")
  public ResponseEntity<Object> getOneUserCommitRecord(@RequestParam("username") String username) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    int userId = userDb.getUserIdByUsername(username);
    JSONArray array = new JSONArray();

    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.S");

    for (Assignment assignment : assignmentDb.getAllAssignment()) {
      int auId = assignmentUserDb.getAuid(assignment.getId(), userId);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("assignmentName", assignment.getName());
      jsonObject.put("releaseTime", assignment.getReleaseTime());

      org.json.JSONObject lastCommitRecordJson = commitRecordDb.getLastCommitRecord(auId);
      JSONObject lastCommitRecord = new JSONObject();
      lastCommitRecord.put("commitNumber", lastCommitRecordJson.get("commitNumber"));
      lastCommitRecord.put("commitTime", dateFormat.format(lastCommitRecordJson.get("commitTime")));
      lastCommitRecord.put("status", lastCommitRecordJson.get("status"));

      jsonObject.put("commitRecord",lastCommitRecord);
      array.add(jsonObject);
    }
    return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
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

  @GetMapping("/feedback")
  public ResponseEntity<Object> getFeedback(@RequestParam("username") String username,
                                            @RequestParam("assignmentName") String assignmentName, @RequestParam("number") int number) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    JenkinsService js = JenkinsService.getInstance();
    String jobName = username + "_" + assignmentName;
    String console = js.getConsole(jobName, number);
    int auId = getAuid(username, assignmentName);
    String statusType = getStatusTypeName(auId, number);

    ProjectTypeEnum projectTypeEnum = getProjectTypeEnum(assignmentName);
    Status statusAnalysis = StatusAnalysisFactory.getStatusAnalysis(projectTypeEnum, statusType);


    String message = statusAnalysis.extractFailureMsg(console);
    ArrayList feedBacks = statusAnalysis.formatExamineMsg(message);
    String feedBackMessage = statusAnalysis.tojsonArray(feedBacks);

    return new ResponseEntity<Object>(feedBackMessage, headers, HttpStatus.OK);
  }


  private int getAuid(String username, String assignmentName) {
    return assignmentUserDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
  }

  private String getStatusTypeName(int auId, int number) {
    int statusId = commitRecordDb.getCommitRecordStatus(auId, number);
    return csDb.getStatusNameById(statusId).getType();
  }

  private ProjectTypeEnum getProjectTypeEnum(String assignmentName) {
    AssignmentDbManager adb = AssignmentDbManager.getInstance();
    AssignmentTypeDbManager atdb = AssignmentTypeDbManager.getInstance();
    int typeId = adb.getAssignmentTypeId(assignmentName);
    return atdb.getTypeNameById(typeId);
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
