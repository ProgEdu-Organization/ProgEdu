package fcu.selab.progedu.service;

import com.csvreader.CsvReader;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.AssignmentScore;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentScoreDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value ="/score")
public class AssignmentScoreService {

  private UserDbManager userDbManager = UserDbManager.getInstance();
  private AssignmentScoreDbManager assignmentScoreDbManager = AssignmentScoreDbManager.getInstance();
  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
  private TomcatService tomcatService = TomcatService.getInstance();


  @PostMapping("/assignment/upload")
  public ResponseEntity<Object> uploadAssignmentScore(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("file") MultipartFile uploadedInputStream) {

    try {
      HttpHeaders headers = new HttpHeaders();

      int aid = assignmentDbManager.getAssignmentIdByName(assignmentName);

      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream.getInputStream(), "BIG5"));
      csvReader.readHeaders();

      while (csvReader.readRecord()) {
        AssignmentScore assignmentScore = new AssignmentScore();
        String userName = csvReader.get("ID");
        int uid = userDbManager.getUserIdByUsername(userName);
        int auid = assignmentUserDbManager.getAuid(aid, uid);

        int score = Integer.valueOf(csvReader.get("Score"));

        assignmentScore.setAuid(auid);
        assignmentScore.setScore(score);

        assignmentScoreDbManager.addAssignmentScore(assignmentScore);

      }
      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/exam/upload")
  public ResponseEntity<Object> uploadExamScore(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("file") MultipartFile uploadedInputStream) {
    try {
      HttpHeaders headers = new HttpHeaders();
      Assignment assignment = new Assignment();
      Date date = tomcatService.getCurrentTime();
      ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.EXAM;

      assignment.setName(assignmentName);
      assignment.setCreateTime(date);
      assignment.setDescription("exam");
      assignment.setType(projectTypeEnum);

      int aid = assignmentDbManager.addAssignmentAndGetId(assignment);

      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream.getInputStream(), "BIG5"));
      csvReader.readHeaders();

      while (csvReader.readRecord()) {
        AssignmentScore assignmentScore = new AssignmentScore();
        String userName = csvReader.get("ID");
        int uid = userDbManager.getUserIdByUsername(userName);

        assignmentUserDbManager.addAssignmentUser(aid, uid);
        int auid = assignmentUserDbManager.getAuid(aid, uid);

        int score = Integer.valueOf(csvReader.get("Score"));

        assignmentScore.setAuid(auid);
        assignmentScore.setScore(score);

        assignmentScoreDbManager.addAssignmentScore(assignmentScore);

      }

      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/allScore/mean")
  public ResponseEntity<Object> getAllScoreMean() {
    JSONArray jsonArray = new JSONArray();

    try {
      HttpHeaders headers = new HttpHeaders();
      List<Integer> assignmentIds = assignmentScoreDbManager.getAllGradedAssignments();
      for (int assignmentId : assignmentIds) {
        JSONObject ob = new JSONObject();
        int averageScore = assignmentScoreDbManager.getAssignmentMeanByAssignmentId(assignmentId);
        String assignmentName = assignmentDbManager.getAssignmentNameById(assignmentId);
        ProjectTypeEnum assignmentType = assignmentDbManager.getAssignmentType(assignmentName);
        ob.put("assignmentId", assignmentId);
        ob.put("assignmentName", assignmentName);
        ob.put("averageScore", averageScore);
        ob.put("type", assignmentType);
        jsonArray.add(ob);
      }

      return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
    } catch (Exception e) {
      HttpHeaders headers = new HttpHeaders();
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<Object> deleteAssignmentScore(
          @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();

    try {

      int aid = assignmentDbManager.getAssignmentIdByName(assignmentName);
      List<Integer> auIds = assignmentUserDbManager.getAuids(aid);
      for (int auId : auIds) {
        assignmentScoreDbManager.deleteAssignmentScoreByAuId(auId);
      }
      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/update")
  public ResponseEntity<Object> updateAssignmentScore(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("userName") String userName,
          @RequestParam("score") int score) {

    try {
      HttpHeaders headers = new HttpHeaders();
      int aid = assignmentDbManager.getAssignmentIdByName(assignmentName);
      int uid = userDbManager.getUserIdByUsername(userName);
      int auId = assignmentUserDbManager.getAuid(aid, uid);
      assignmentScoreDbManager.updateAssignmentScoreByAuId(auId, score);

      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      HttpHeaders headers = new HttpHeaders();
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/allUser")
  public ResponseEntity<Object> getAllUserScore(
          @RequestParam("assignmentName") String assignmentName) {
    HttpHeaders headers = new HttpHeaders();
    JSONArray jsonArray = new JSONArray();

    try {
      int aid = assignmentDbManager.getAssignmentIdByName(assignmentName);
      List<Integer> auIds = assignmentUserDbManager.getAuids(aid);

      for (int auId : auIds) {
        JSONObject ob = new JSONObject();
        int score = assignmentScoreDbManager.getScoreByAuId(auId);
        int uid = assignmentUserDbManager.getUidById(auId);
        String userName = userDbManager.getUsername(uid);
        ob.put("userName", userName);
        ob.put("score", score);
        jsonArray.add(ob);
      }

      return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
}
