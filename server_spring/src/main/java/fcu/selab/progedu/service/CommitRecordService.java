package fcu.selab.progedu.service;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fcu.selab.progedu.conn.GitlabService;


@RestController
@RequestMapping(value = "/commits")
public class CommitRecordService {

  private GitlabService gitlabService = GitlabService.getInstance();

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

  public void getPartCommitRecord() {

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
