package fcu.selab.progedu.service;

import com.csvreader.CsvReader;
import fcu.selab.progedu.data.AssignmentScore;
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

@RestController
@RequestMapping(value ="/score")
public class AssignmentScoreService {

  private UserDbManager userDbManager = UserDbManager.getInstance();
  private AssignmentScoreDbManager assignmentScoreDbManager = AssignmentScoreDbManager.getInstance();
  private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
  private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();

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
}
