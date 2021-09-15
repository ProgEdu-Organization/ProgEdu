package fcu.selab.progedu.service;
import fcu.selab.progedu.data.AssessmentTime;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value ="/chart")
public class ChartService {

  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private AssignmentService assignmentService = AssignmentService.getInstance();
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
  private CommitStatusDbManager commitStatusDbManager = CommitStatusDbManager.getInstance();
  private CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();
  private AssessmentTimeDbManager assessmentTimeDbManager = AssessmentTimeDbManager.getInstance();

  /**
   * get all commit record.
   * @return allCommits
   */
  @GetMapping("/allCommitRecord")
  public ResponseEntity<Object> getAllCommitRecords() {
    HttpHeaders headers = new HttpHeaders();
    //

    // Step1  find all assignment's name.
    List<String> assignmentNames =  assignmentService.getAllAssignmentNames();
    // Step2  find all commitRecord id by using assignment's id.
    JSONObject ob = new JSONObject();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.S");

    List<JSONObject> assignments = new ArrayList<>();

    assignmentNames.forEach(name -> {
      JSONObject commits = new JSONObject();
      List<Integer> assignmentIds = new ArrayList<>();
      assignmentIds.add(assignmentDbManager.getAssignmentIdByName(name));
      assignmentIds.forEach(id -> {
        List<Integer> auIds = assignmentUserDbManager.getAuids(id);

        List<JSONObject> array = new ArrayList<>();

        auIds.forEach(auId -> {
          List<CommitRecord> commitRecords = commitRecordDbManager.getCommitRecord(auId);
          for (CommitRecord commitRecord : commitRecords) {
            try {
              int number = commitRecord.getNumber();
              Date time = commitRecord.getTime();
              String status = commitRecord.getStatus().getType();
              JSONObject temp = new JSONObject();
              temp.put("number", number);
              temp.put("status", status.toUpperCase());
              temp.put("time", time);
              array.add(temp);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        commits.put("name", name);
        JSONArray jsonArray = new JSONArray();
        for(AssessmentTime assessmentTime : assessmentTimeDbManager.getAssessmentTimeByName(name)) {
          JSONObject assessmentTimeObject = new JSONObject();
          assessmentTimeObject.put("assessmentAction", assessmentTime.getAssessmentActionEnum().toString());
          assessmentTimeObject.put("startTime", dateFormat.format(assessmentTime.getStartTime()));
          assessmentTimeObject.put("endTime", dateFormat.format(assessmentTime.getEndTime()));
          jsonArray.add(assessmentTimeObject);
        }
        commits.put("assessmentTimes", jsonArray);
        commits.put("commits", array);
      });
      assignments.add(commits);
    });
    ob.put("allCommitRecord", assignments);

    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
  }

}
