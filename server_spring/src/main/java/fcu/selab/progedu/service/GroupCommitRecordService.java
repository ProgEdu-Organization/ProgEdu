package fcu.selab.progedu.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusAnalysisFactory;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.data.CommitRecord;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
   * update user assignment commit record to DB.
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
  public ResponseEntity<Object> getCommitRecord(
          @PathVariable("name") String groupName, @PathVariable("projectName") String projectName){
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
    return new ResponseEntity<>(array, HttpStatus.OK);
  }

  // groupsCommit
  public void getAllGroupCommitRecord() {

  }

  // userGroupCommit
  public void getCommitRecordByUsername() {

  }


}
