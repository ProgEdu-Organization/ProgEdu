package fcu.selab.progedu.updatecommit;

import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.*;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJob2StatusFactory;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJobStatus;
import fcu.selab.progedu.status.StatusEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.minidev.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value ="/publicApi/update")
public class UpdateCommit {

  @PostMapping("commits")
  public ResponseEntity<Object> updateCommitResult(@RequestParam("user") String username,
                                                   @RequestParam("proName") String assignmentName)
          throws Exception {
    // Todo 所有 assignment 相關的都要改掉 現在沒有 assignment

    HttpHeaders headers = new HttpHeaders();
    //


    JSONObject ob = new JSONObject();

    AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
    AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
    UserDbManager userDb = UserDbManager.getInstance();
    CommitRecordDbManager db = CommitRecordDbManager.getInstance();
    AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
    int commitNumber = db.getCommitCount(auId) + 1;
    Date date = new Date();
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
    date = time.parse(time.format(calendar.getTime()));


    int assignmentId = assignmentDb.getAssignmentTypeId(assignmentName);
    ProjectTypeEnum projectTypeEnum = atDb.getTypeNameById(assignmentId);
    JenkinsJobStatus jobStatus = JenkinsJob2StatusFactory.createJenkinsJobStatus(projectTypeEnum);

    String jobName = username + "_" + assignmentName;
    StatusEnum statusEnum = jobStatus.getStatus(jobName, commitNumber);


    db.insertCommitRecord(auId, commitNumber, statusEnum, date);

    ob.put("auId", auId);
    ob.put("commitNumber", commitNumber);
//    ob.put("time", time); // Todo 這根本沒用到
    ob.put("status", statusEnum.getType());
    return new ResponseEntity<>(ob, headers, HttpStatus.OK);
  }
}
