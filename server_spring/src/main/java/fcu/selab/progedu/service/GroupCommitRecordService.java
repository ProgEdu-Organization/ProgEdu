package fcu.selab.progedu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusAnalysisFactory;
import fcu.selab.progedu.status.StatusEnum;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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

  // groupsCommit
  public void getAllGroupCommitRecord() {

  }

  // userGroupCommit
  public void getCommitRecordByUsername() {

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

      ob.put("releaseTime", dateFormat.format(project.getReleaseTime()));
      if (cr != null) {
        ob.put("number", cr.getNumber());
        ob.put("status", cr.getStatus().getType());
      }
      array.add(ob);
    }

    return new ResponseEntity<Object>(array, headers, HttpStatus.OK);

  }


}
