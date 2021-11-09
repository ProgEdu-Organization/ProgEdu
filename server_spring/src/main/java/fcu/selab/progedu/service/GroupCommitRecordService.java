package fcu.selab.progedu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import javax.ws.rs.core.Response;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusAnalysisFactory;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJobStatus;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJob2StatusFactory;
import fcu.selab.progedu.utils.ExceptionUtil;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/groups")
public class GroupCommitRecordService {
  private JenkinsService js = JenkinsService.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private GroupDbService gdb = GroupDbService.getInstance();
  private ProjectDbService gpdb = ProjectDbService.getInstance();
  private ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupCommitRecordService.class);

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GetMapping("/commits")
  public ResponseEntity<Object> getAllGroupCommitRecord() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //
    JSONArray array = new JSONArray();
    List<Group> groups = gdb.getGroups();
    for (Group group : groups) {
      ResponseEntity<Object> response = getResult(group.getGroupName());
      JSONObject ob = new JSONObject();
      ob.put("groupName", group.getGroupName());
      ob.put("commitRecord", response.getBody());
      array.add(ob);
    }
    return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
  }

  /**
   * Display
   *
   * @param username username
   */
  @GetMapping("/{username}/commits")
  public ResponseEntity<Object> getCommitRecordByUsername(@PathVariable("username") String username) {

    HttpHeaders headers = new HttpHeaders();
    //

    GroupService gs = new GroupService();
    int uid = dbManager.getUserIdByUsername(username);
    List<String> groupNames = gdb.getGroupNames(uid);
    List<JSONObject> array = new ArrayList<>();
    for (String groupName : groupNames) {
      ResponseEntity<Object> response = getResult(groupName);
      JSONObject ob = new JSONObject();
      ob.put("groupName", groupName);
      ob.put("commitRecord", response.getBody());
      array.add(ob);
    }
    return new ResponseEntity<>(array, headers, HttpStatus.OK);
  }


  /**
   * get student commit feedback detail
   *
   * @param groupName username
   * @param projectName assignment name
   * @param number commit number
   * @throws ParseException (to do)
   */
  @GetMapping("/{name}/projects/{projectName}/feedback/{num}")
  public ResponseEntity<Object> getFeedback(
      @PathVariable("name") String groupName,
      @PathVariable("projectName") String projectName,
      @PathVariable("num") int number) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    JenkinsService js = JenkinsService.getInstance();
    ProjectTypeEnum projectTypeEnum = gpdb.getProjectType(projectName);

    String jobName = js.getJobName(groupName, projectName);
    String console = js.getConsole(jobName, number);
    int pgid = pgdb.getId(groupName, projectName);

    StatusEnum statusType = gpdb.getCommitRecordStatus(pgid, number);

    Status statusAnalysis = StatusAnalysisFactory.getStatusAnalysis(projectTypeEnum,
        statusType.getType());

    String message = statusAnalysis.extractFailureMsg(console);
    ArrayList feedBacks = statusAnalysis.formatExamineMsg(message);
    String feedBackMessage = statusAnalysis.tojsonArray(feedBacks);

    return new ResponseEntity<Object>(feedBackMessage, headers, HttpStatus.OK);
  }

  /**
   * get student build detail info
   *
   * @param groupName student id
   * @param projectName assignment name
   * @return build detail
   */
  @GetMapping("/{name}/projects/{projectName}/commits")
  public ResponseEntity<Object> getCommitRecord( // Todo 這API 好像沒用到
          @PathVariable("name") String groupName, @PathVariable("projectName") String projectName){

    HttpHeaders headers = new HttpHeaders();
    //

    JenkinsService js = JenkinsService.getInstance();
    List<JSONObject> array = new ArrayList<>();
    int pgid = gpdb.getPgid(groupName, projectName);
    List<CommitRecord> commitRecords = gpdb.getCommitRecords(pgid);
    String jobName = groupName + "_" + projectName;
    for (CommitRecord commitRecord : commitRecords) {
      int number = commitRecord.getNumber();
      String message = js.getCommitMessage(jobName, number);
      Date time = commitRecord.getTime();
      String status = commitRecord.getStatus().getType();
      String committer = commitRecord.getCommitter();

      JSONObject ob = new JSONObject();

      ob.put("number", number);
      ob.put("status", status.toUpperCase());
      ob.put("time", time.toString());
      ob.put("message", message);
      ob.put("committer", committer);
      array.add(ob);
    }
    return new ResponseEntity<>(array, headers, HttpStatus.OK);
  }

  /**
   * get all commit record of one student.
   *
   * @param groupName group name
   * @return homework, commit status, commit number
   */
  @GetMapping("/{name}/commits/result")
  public ResponseEntity<Object> getResult(@PathVariable("name") String groupName) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    JSONArray array = new JSONArray();
    JSONObject ob = new JSONObject();

    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.S");

    List<Integer> pgids = gpdb.getPgids(groupName);
    for (int pgid : pgids) {
      GroupProject project = gpdb.getProject(pgid);

      ob.put("name", project.getName());
      CommitRecord cr = gpdb.getCommitResult(pgid);


      Date releaseTimeDate = project.getReleaseTime();
      if(releaseTimeDate != null) {
        String releaseTime = dateFormat.format(releaseTimeDate);
        ob.put("releaseTime", releaseTime);
      }

      if (cr != null) {
        ob.put("number", cr.getNumber());
        ob.put("status", cr.getStatus().getType());
      }
      array.add(ob);
    }

    return new ResponseEntity<Object>(array, headers, HttpStatus.OK);

  }

  /**
   * update user assignment commit record to DB.
   *
   * @param groupName username
   * @param projectName assignment name
   * @throws ParseException
   */
  @PostMapping(path = "/commits/update")
  public ResponseEntity<Object> updateCommitRecord(
          @RequestParam("user") String groupName,
          @RequestParam("proName") String projectName) {

    HttpHeaders headers = new HttpHeaders();
    //
    
    ProjectTypeEnum projectTypeEnum = gpdb.getProjectType(projectName);
    int pgid = pgdb.getId(groupName, projectName);
    CommitRecord cr = gpdb.getCommitResult(pgid);
    int commitNumber = 1;
    if (cr != null) {
      commitNumber = cr.getNumber() + 1;
    }
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    try {
      date = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
    } catch (ParseException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    JenkinsJobStatus jobStatus = JenkinsJob2StatusFactory.createJenkinsJobStatus(projectTypeEnum);
    String jobName = groupName + "_" + projectName;
    StatusEnum statusEnum = jobStatus.getStatus(jobName, commitNumber);

    String committer = js.getCommitter(jobName, commitNumber);
    gpdb.insertProjectCommitRecord(pgid, commitNumber, statusEnum, date, committer);
    JSONObject ob = new JSONObject();
    ob.put("pgid", pgid);
    ob.put("commitNumber", commitNumber);
    ob.put("time", dateFormat.toString());
    ob.put("status", statusEnum.getType());
    ob.put("committer", committer);

    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
  }

  /**
   * get student build detail info.
   * @param groupName group name
   * @param projectName project name
   * @param currentPage current page
   * @return build detail
   */
  @GetMapping("/{name}/projects/{projectName}/partCommits/{currentPage}")
  public ResponseEntity<Object> getPartCommitRecord(
          @PathVariable("name") String groupName,
          @PathVariable("projectName") String projectName,
          @PathVariable("currentPage") int currentPage
  ) {
    HttpHeaders headers = new HttpHeaders();
    //

    JenkinsService js = JenkinsService.getInstance();
    JSONArray array = new JSONArray();
    int pgid = gpdb.getPgid(groupName, projectName);
    List<CommitRecord> commitRecords = gpdb.getPartCommitRecords(pgid,currentPage);
    String jobName = groupName + "_" + projectName;
    int totalCommit = gpdb.getCommitCount(pgid);

    for (CommitRecord commitRecord : commitRecords) {
      int number = commitRecord.getNumber();
      String message = js.getCommitMessage(jobName, number);
      Date time = commitRecord.getTime();
      String status = commitRecord.getStatus().getType();
      String committer = commitRecord.getCommitter();
      JSONObject ob = new JSONObject();

      ob.put("totalCommit", totalCommit);
      ob.put("number", number);
      ob.put("status", status.toUpperCase());
      ob.put("time", time);
      ob.put("message", message);
      ob.put("committer", committer);
      array.add(ob);
    }
    return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
  }
}
