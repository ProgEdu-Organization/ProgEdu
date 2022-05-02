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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.Date;

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
        int auid =assignmentUserDbManager.getAuid(aid, uid);

        int score = Integer.valueOf(csvReader.get("score"));

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
        int auid =assignmentUserDbManager.getAuid(aid, uid);

        int score = Integer.valueOf(csvReader.get("score"));

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
}
