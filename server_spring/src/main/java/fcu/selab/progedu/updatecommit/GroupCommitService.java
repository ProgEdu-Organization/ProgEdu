package fcu.selab.progedu.updatecommit;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.ProjectGroupDbService;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJob2StatusFactory;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJobStatus;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value ="/publicApi/groups/commits")
public class GroupCommitService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupCommitService.class);
  private ProjectDbService gpdb = ProjectDbService.getInstance();
  private ProjectGroupDbService pgdb = ProjectGroupDbService.getInstance();
  private JenkinsService js = JenkinsService.getInstance();

  @PostMapping("/update")
  public ResponseEntity<Object> updateCommitRecord(
          @RequestParam("user") String groupName, @RequestParam("proName") String projectName) {

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
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    try {
      date = time.parse(time.format(Calendar.getInstance().getTime()));
    } catch (Exception e) {
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
    ob.put("time", time);
    ob.put("status", statusEnum.getType());
    ob.put("committer", committer);

    return new ResponseEntity<>(ob, headers, HttpStatus.OK);

  }

}
