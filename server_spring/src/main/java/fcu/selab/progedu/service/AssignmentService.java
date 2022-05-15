package fcu.selab.progedu.service;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.*;
import fcu.selab.progedu.db.*;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkinsconfig.AndroidPipelineConfig;
import fcu.selab.progedu.jenkinsconfig.JenkinsProjectConfig;
import fcu.selab.progedu.jenkinsconfig.JenkinsProjectConfigFactory;
import fcu.selab.progedu.jenkinsconfig.WebPipelineConfig;
import fcu.selab.progedu.setting.MavenAssignmentSetting;
import fcu.selab.progedu.utils.JavaIoUtile;
import fcu.selab.progedu.utils.ZipHandler;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.io.IOUtils;
import org.gitlab.api.models.GitlabProject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.web.multipart.MultipartFile;
import fcu.selab.progedu.utils.Linux;

import javax.ws.rs.QueryParam;


@RestController
@RequestMapping(value ="/assignment")
public class AssignmentService {

  private GitlabService gitlabService = GitlabService.getInstance();
  private TomcatService tomcatService = TomcatService.getInstance(); // Todo 命名有問題 應該是server 環境服務
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private CourseConfig courseConfig = CourseConfig.getInstance();
  private AssignmentDbManager dbManager = AssignmentDbManager.getInstance();
  private UserService userService = UserService.getInstance();
  private UserDbManager userDbManager = UserDbManager.getInstance();
  private AssignmentUserDbManager auDbManager = AssignmentUserDbManager.getInstance();
  private ReviewSettingDbManager rsDbManager = ReviewSettingDbManager.getInstance();
  private ReviewSettingMetricsDbManager rsmDbManager = ReviewSettingMetricsDbManager.getInstance();
  private PairMatchingDbManager pmDbManager = PairMatchingDbManager.getInstance();
  private ReviewStatusDbManager reviewStatusDbManager = ReviewStatusDbManager.getInstance();
  private AssignmentAssessmentDbManager aaDbManager = AssignmentAssessmentDbManager.getInstance();
  private CommitStatusDbManager csDbManager = CommitStatusDbManager.getInstance();
  private ReviewRecordDbManager rrDbManager = ReviewRecordDbManager.getInstance();
  private AssessmentTimeDbManager assessmentTimeDbManager = AssessmentTimeDbManager.getInstance();
  private CommitRecordDbManager crDbManager = CommitRecordDbManager.getInstance();
  private ScreenshotRecordDbManager srDbManager = ScreenshotRecordDbManager.getInstance();
  private AssessmentActionDbManager assessmentActionDbManager = AssessmentActionDbManager.getInstance();
  private ReviewRecordStatusDbManager rrsDbManager = ReviewRecordStatusDbManager.getInstance();


  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String uploadDir = tempDir + "/uploads/";
  private final String testDir = tempDir + "/tests/";
  private final String assignmentSettingDir = tempDir + "/assignmentSetting/";
  private final String mavenPomXmlSettingDir = tempDir + "/mavenPomXmlSetting/";

  private String gitlabRootUsername;
  private ZipHandler zipHandler;
  private String mailUsername;
  private String mailPassword;

  private static AssignmentService instance = new AssignmentService();
  public static AssignmentService getInstance() {
    return instance;
  }

  public AssignmentService() {
    try {
      zipHandler = new ZipHandler();
      mailUsername = jenkinsData.getMailUser();
      mailPassword = jenkinsData.getMailPassword();
      gitlabRootUsername = gitlabData.getGitlabRootUsername();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  @PostMapping("autoAssessment/create")
  public ResponseEntity<Object> createAutoAssessment(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("releaseTime") Date releaseTime, @RequestParam("deadline") Date deadline,
          @RequestParam("readMe") String readMe, @RequestParam("fileRadio") String assignmentType,
          @RequestParam("file") MultipartFile file,
          @RequestParam("order") String assignmentCompileOrdersAndScore) {

    HttpHeaders headers = new HttpHeaders();
    //
    List<AssessmentTime> assessmentTimes = new ArrayList<>();
    AssessmentTime autoAssessmentAssignmentTime = new AssessmentTime();
    autoAssessmentAssignmentTime.setAssessmentActionEnum(AssessmentActionEnum.AUTO);
    autoAssessmentAssignmentTime.setStartTime(releaseTime);
    autoAssessmentAssignmentTime.setEndTime(deadline);
    assessmentTimes.add(autoAssessmentAssignmentTime);

    try {
      AssignmentWithOrderCreator assignmentWithOrderCreator = new AssignmentWithOrderCreator();
      assignmentWithOrderCreator.createAssignment(assignmentName, readMe, assignmentType, file,
          assessmentTimes, assignmentCompileOrdersAndScore);
      addOrder(assignmentCompileOrdersAndScore, assignmentName);
      /*
      createAssignment(assignmentName, readMe,
              assignmentType, file, assessmentTimes);
      if (!assignmentCompileOrdersAndScore.isEmpty()) {
        addOrder(assignmentCompileOrdersAndScore, assignmentName);
      }*/

      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void addOrder(String assignmentCompileOrdersAndScore, String assignmentName) {
    List<String> ordersList = new ArrayList<>();
    List<String> scoresList = new ArrayList<>();

    String[] ordersAndScores = assignmentCompileOrdersAndScore.split(", ");
    for (String orderAndScore : ordersAndScores) {
      String[] token = orderAndScore.split(":");

      if (token[0].equals("Compile Failure")) {
        ordersList.add("cpf");
      } else if (token[0].equals("Unit Test Failure")) {
        ordersList.add("utf");
      } else if (token[0].equals("Coding Style Failure")) {
        ordersList.add("csf");
      } else if (token[0].equals("HTML Failure")) {
        ordersList.add("whf");
      } else if (token[0].equals("CSS Failure")) {
        ordersList.add("wsf");
      } else if (token[0].equals("JavaScript Failure")) {
        ordersList.add("wef");
      } else if (token[0].equals("UI Test Failure")) {
        ordersList.add("uitf");
      }
      scoresList.add(token[1]);
    }
    //write assignment assessment in to data base
    //addAssignmentAssessment( aid, int sid, int order, int score)
    for (int i = 0; i < ordersAndScores.length; i++) {
      aaDbManager.addAssignmentAssessment(dbManager.getAssignmentIdByName(assignmentName),
              csDbManager.getStatusIdByName(ordersList.get(i)),
              i + 1, Integer.valueOf(scoresList.get(i)));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createAssignment( // 把readme 的圖片處理拿掉 因為太複雜了
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("readMe") String readMe, @RequestParam("fileRadio") String assignmentType,
          @RequestParam("file") MultipartFile file,
          @RequestParam("assessmentTimes") List<AssessmentTime> assessmentTimes) {


    HttpHeaders headers = new HttpHeaders();
    //

    // 1. Create root project and get project id and url
    gitlabService.createRootProject(assignmentName);

    // 2. Clone the project
    final String cloneDirectoryPath = gitlabService.cloneProject(gitlabRootUsername,
            assignmentName);

    // 3. Store Zip File to uploads folder if file is not empty
    String filePath;
    try {
      filePath = tomcatService.storeFileToUploadsFolder(file.getInputStream(), file.getName());
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 4. Unzip the uploaded file to cloneDirectoryPath
    zipHandler.unzipFile(filePath, cloneDirectoryPath);

    // 5. Add .gitkeep if folder is empty.
    JavaIoUtile.addFile2EmptyFolder(new File(cloneDirectoryPath), ".gitkeep");

    if (!readMe.equals("<br>") || !"".equals(readMe) || !readMe.isEmpty()) {
      // Add readme to folder
      JavaIoUtile.createUtf8FileFromString(readMe, new File(cloneDirectoryPath, "README.md"));
    }

    // 8. git push
    gitlabService.pushProject(cloneDirectoryPath);

    // 9. import project information to database
    ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(assignmentType);
    addProject(assignmentName, readMe, projectTypeEnum, assessmentTimes);


    List<User> users = userService.getStudents();
    for (User user : users) {
      createAssignmentSettingsV2(user.getUsername(), assignmentName);
    }

    // 10. remove project file
    JavaIoUtile.deleteDirectory(new File(uploadDir));
    return new ResponseEntity<Object>(headers, HttpStatus.OK);
  }

  @GetMapping("getAllAssignments")
  public ResponseEntity<Object> getAllAssignments() {

    List<Assignment> assignments = dbManager.getAllAssignment();

    JSONObject ob = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    for(Assignment assignment : assignments) {
      int aId = assignment.getId();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", aId);
      jsonObject.put("name", assignment.getName());
      jsonObject.put("createTime", assignment.getCreateTime());
      JSONArray jsonArrayTime = new JSONArray();
      for (AssessmentTime assessmentTime : assignment.getAssessmentTimeList()) {
        JSONObject jsonObjectTime = new JSONObject();
        jsonObjectTime.put("assessmentAction", assessmentTime.getAssessmentActionEnum().toString());
        jsonObjectTime.put("startTime", assessmentTime.getStartTime());
        jsonObjectTime.put("endTime", assessmentTime.getEndTime());
        jsonArrayTime.add(jsonObjectTime);
      }
      jsonObject.put("assessmentTimes", jsonArrayTime);
      jsonObject.put("description", assignment.getDescription());
      jsonObject.put("type", assignment.getType());
      jsonObject.put("display", assignment.isDisplay());
      jsonArray.add(jsonObject);
    }
    ob.put("allAssignments", jsonArray);
    return new ResponseEntity<Object>(ob, HttpStatus.OK);
  }

  @PostMapping("peerReview/create")
  public ResponseEntity<Object> createPeerReview(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("readMe") String readMe,
          @RequestParam("fileRadio") String assignmentType,
          @RequestParam("file") MultipartFile file,
          @RequestParam("amount") int amount,
          @RequestParam("metrics") String metrics,
          @RequestParam("assessmentTimes") String assessmentTimes) {

    HttpHeaders headers = new HttpHeaders();
    //
    try {
      AssignmentWithoutOrderCreator assignmentWithoutOrderCreator = new AssignmentWithoutOrderCreator();
      // 1. create assignment
      JSONArray jsonArray = (JSONArray) JSONValue.parse(assessmentTimes);
      SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
      List<AssessmentTime> assessmentTimeList = new ArrayList<>();
      int totalRounds = jsonArray.size();
      for(int i = 0; i < jsonArray.size(); i++) {
        JSONObject object = (JSONObject) jsonArray.get(i);
        for(String assessmentAction : object.keySet()) {
          JSONObject timeObject = (JSONObject) object.get(assessmentAction);
          Date startTime = formatter.parse(timeObject.get("startTime").toString());
          Date endTime = formatter.parse(timeObject.get("endTime").toString());
          AssessmentTime assessmentTime = new AssessmentTime();
          int actionId = assessmentActionDbManager.getAssessmentActionIdByAction(assessmentAction);
          assessmentTime.setAssessmentActionEnum(assessmentActionDbManager.getAssessmentActionById(actionId));
          assessmentTime.setStartTime(startTime);
          assessmentTime.setEndTime(endTime);
          assessmentTimeList.add(assessmentTime);
        }
      }
      assignmentWithoutOrderCreator.createAssignment(assignmentName, readMe,
              assignmentType, file, assessmentTimeList);
      /*
      createAssignment(assignmentName,
              releaseTime, deadline, readMe, assignmentType, file);
      */
      // 2. create peer review setting
      int assignmentId = dbManager.getAssignmentIdByName(assignmentName);
      //rsDbManager.insertReviewSetting(assignmentId, amount, reviewStartTime, reviewEndTime);

      rsDbManager.insertReviewSetting(assignmentId, amount, totalRounds);

      // 3. set review metrics for specific peer review
      int reviewSettingId = rsDbManager.getReviewSettingIdByAid(assignmentId);
      int[] array = arrayStringToIntArray(metrics);
      for (int item: array) {
        rsmDbManager.insertReviewSettingMetrics(reviewSettingId, item);
      }

      // 4. set random reviewer and review status for each assignment_user
      randomPairMatching(amount, assignmentName);

      // 5. set review record status init
      List<AssignmentUser> assignmentUserList = auDbManager.getAssignmentUserListByAid(assignmentId);
      for (AssignmentUser assignmentUser : assignmentUserList) {
        List<PairMatching> pairMatchingList = pmDbManager.getPairMatchingByAuId(assignmentUser.getId());
        for (PairMatching pairMatching : pairMatchingList) {
          for (int j = 1; j <= totalRounds; j++) {
            rrsDbManager.insertReviewRecordStatus(pairMatching.getId(), ReviewStatusEnum.INIT, j);
          }
        }
      }

      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @GetMapping("peerReview/allAssignment")
  public ResponseEntity<Object> getAllReviewAssignment() {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      List<Assignment> assignmentList = dbManager.getAllReviewAssignment();
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date current = new Date();
      /*
      for (Assignment assignment : assignmentList) {
        if (current.compareTo(assignment.getReleaseTime()) >= 0) {
          updatePairMatchingStatusByAid(assignment.getId());
        }
      }
      */
      JSONObject result = new JSONObject();
      List<JSONObject> array = new ArrayList<>();
      for (Assignment assignment : assignmentList) {
        JSONObject ob = new JSONObject();
        ob.put("id", assignment.getId());
        ob.put("name", assignment.getName());
        ob.put("createTime", assignment.getCreateTime());
        ob.put("display", assignment.isDisplay());
        ob.put("description", assignment.getDescription());
        ob.put("amount", rsDbManager.getReviewSetting(assignment.getId()).getAmount());
        ob.put("round", rsDbManager.getReviewSetting(assignment.getId()).getRound());
        JSONArray jsonArray = new JSONArray();
        for(AssessmentTime assessmentTime : assignment.getAssessmentTimeList()) {
          JSONObject assessmentObject = new JSONObject();
          assessmentObject.put("assessmentAction", assessmentTime.getAssessmentActionEnum().toString());
          assessmentObject.put("startTime", dateFormat.format(assessmentTime.getStartTime()));
          assessmentObject.put("endTime", dateFormat.format(assessmentTime.getEndTime()));
          jsonArray.add(assessmentObject);
        }
        ob.put("assessmentTimes", jsonArray);

        array.add(ob);
      }
      result.put("allReviewAssignments", array);

      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //TODO pairMatching 沒有 status 要拔掉
  public void updatePairMatchingStatusByAid(int aid) throws SQLException {
    List<AssignmentUser> assignmentUserList = auDbManager.getAssignmentUserListByAid(aid);

    for (AssignmentUser assignmentUser : assignmentUserList) {

      if (!pmDbManager.checkStatusUpdated(assignmentUser.getId())) {
        List<PairMatching> pmList = pmDbManager.getPairMatchingByAuId(assignmentUser.getId());

        for (PairMatching pairMatching: pmList) {
          /*
          if (pairMatching.getReviewStatusEnum().equals(ReviewStatusEnum.INIT)) {
            int status = reviewStatusDbManager
                    .getReviewStatusIdByStatus(ReviewStatusEnum.UNCOMPLETED.getTypeName());
            pmDbManager.updatePairMatchingById(status, pairMatching.getId());
          }
          */
        }
      }
    }
  }


  @GetMapping("autoAssessment/allAssignment")
  public ResponseEntity<Object> getAllAutoAssignment() {

    HttpHeaders headers = new HttpHeaders();
    //
    try {
      List<Assignment> assignmentList = dbManager.getAutoAssessment();
      JSONObject ob = new JSONObject();
      ob.put("allAutoAssessment", assignmentList);

      return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("getAssignment")
  public ResponseEntity<Object> getAssignment(@RequestParam ("assignmentName") String assignmentName) {

    HttpHeaders headers = new HttpHeaders();
    //
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Assignment assignment = dbManager.getAssignmentByName(assignmentName);
    JSONObject ob = new JSONObject();
    ob.put("description", assignment.getDescription());
    JSONArray jsonArray = new JSONArray();
    for(AssessmentTime assessmentTime : assignment.getAssessmentTimeList()) {
      JSONObject assessmentTimeObject = new JSONObject();
      assessmentTimeObject.put("assessmentAction", assessmentTime.getAssessmentActionEnum().toString());
      assessmentTimeObject.put("startTime", dateFormat.format(assessmentTime.getStartTime()));
      assessmentTimeObject.put("endTime", dateFormat.format(assessmentTime.getEndTime()));
      jsonArray.add(assessmentTimeObject);
    }
    ob.put("assessmentTimes", jsonArray);
    ob.put("type", assignment.getType());
    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
  }


  @GetMapping("getAssignmentOrder")
  public ResponseEntity<Object> getAssignmentOrder(@RequestParam("fileName") String fileName) {

    HttpHeaders headers = new HttpHeaders();
    //

    int aid = dbManager.getAssignmentIdByName(fileName);
    String orders = aaDbManager.getAssignmentOrderAndScore(aid);
    if (orders.isEmpty()) {
      orders = "None";
    }
    JSONObject ob = new JSONObject();
    ob.put("orders", orders);

    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
  }


  public ArrayList<String> findAllDescriptionImagePaths(String readme) {
    ArrayList<String> paths = new ArrayList<>();
    Document doc = Jsoup.parse(readme);
    Elements srcAttributes = doc.select("img[src]");
    for (Element src : srcAttributes) {
      paths.add(src.attr("src"));
    }
    return paths;
  }

  @PostMapping("edit")
  public ResponseEntity<Object> editProject(
          @RequestParam("assignmentName") String assignmentName,
          @RequestParam("readMe") String readMe,
          @RequestParam("order") String assignmentCompileOrdersAndScore,
          @RequestParam("assessmentTimes") String assessmentTimes) {

    HttpHeaders headers = new HttpHeaders();
    //
    try {
      int aid = dbManager.getAssignmentIdByName(assignmentName);
      List<AssessmentTime> assessmentTimeList = assessmentTimeDbManager.getAssessmentTimeByName(assignmentName);

      dbManager.editAssignment(readMe, aid);
      JSONArray jsonArray = (JSONArray) JSONValue.parse(assessmentTimes);
      SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

      for(int i = 0; i < jsonArray.size(); i++) {
        JSONObject object = (JSONObject) jsonArray.get(i);
        int round = 0;
        for(String assessmentAction : object.keySet()) {
          int index = i * 2 + round;
          int aaId = assessmentActionDbManager.getAssessmentActionIdByAction(assessmentAction);
          JSONObject timeObject = (JSONObject) object.get(assessmentAction);
          Date startTime = formatter.parse(timeObject.get("startTime").toString());
          Date endTime = formatter.parse(timeObject.get("endTime").toString());

          assessmentTimeList.get(index).setAssessmentActionEnum(assessmentActionDbManager.getAssessmentActionById(aaId));
          assessmentTimeList.get(index).setStartTime(startTime);
          assessmentTimeList.get(index).setEndTime(endTime);

          round++;
        }
      }

      for(AssessmentTime assignmentTime : assessmentTimeList) {
        assessmentTimeDbManager.editAssignmentTime(assignmentTime);
      }

      if (!assignmentCompileOrdersAndScore.isEmpty()) {
        List<Integer> aaIds = aaDbManager.getAssignmentAssessmentIdByaId(aid);
        List<Integer> scoresList = new ArrayList<>();

        //order: Compile Failure:10, Coding Style Failure:80, Unit Test Failure:10
        String[] ordersAndScores = assignmentCompileOrdersAndScore.split(", ");
        for (String orderAndScore : ordersAndScores) {
          String[] token = orderAndScore.split(":");
          scoresList.add(Integer.valueOf(token[1]));
        }
        for (int i = 0; i < scoresList.size(); i++) {
          aaDbManager.updateScore(aid,
                  aaDbManager.getAssessmentOrder(aaIds.get(i)),
                  scoresList.get(i));
        }
      }

      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("delete")
  public ResponseEntity<Object> deleteProject(@RequestParam("assignmentName") String name) {

    HttpHeaders headers = new HttpHeaders();
    //

    JenkinsService jenkins = JenkinsService.getInstance();
    try {

      // if this assignment was assigned as pair review, delete review db
      if (rsDbManager.checkAssignmentByAid(name)) {
        deleteReviewDatabase(name);
      }

      // delete db
      deleteAssignmentDatabase(name);

      // delete gitlab
      gitlabService.deleteProjects(name);

      // delete Jenkins

      List<User> users = userService.getStudents();

      for (User user : users) {
        String jobName = jenkins.getJobName(user.getUsername(), name);
        jenkins.deleteJob(jobName);
      }
      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public void addProject(String name, String readMe,
                         ProjectTypeEnum projectType, List<AssessmentTime> assessmentTimes) {
    Assignment assignment = new Assignment();
    Date date = tomcatService.getCurrentTime();
    assignment.setName(name);
    assignment.setCreateTime(date);
    assignment.setDescription(readMe);
    assignment.setType(projectType);
    assignment.setAssessmentTimeList(assessmentTimes);

    int aId = dbManager.addAssignmentAndGetId(assignment);
    for(AssessmentTime assessmentTime : assignment.getAssessmentTimeList()) {
      assessmentTimeDbManager.addAssignmentTime(aId, assessmentTime);
    }
  }

  private void createAssignmentSettingsV2(String username, String assignmentName) {

    addAuid(username, assignmentName);
    //Todo 以上 addAuid 要改, 因為之後沒有 assignment

    GitlabProject gitlabProject = gitlabService.createPrivateProject(username,
            assignmentName, "root"); // Todo assignment 要改

    try {
      String jobName = username + "_" + assignmentName;

      gitlabService.setGitlabWebhook(gitlabProject, jobName);

      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + assignmentName
              + ".git";

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
              + "/webapi";
      String updateDbUrl = courseConfig.getTomcatServerIp() + "/publicApi/update/commits";

      /*
      String orderString = "";
      List<String> ordersList = new ArrayList<>();
      String[] ordersAndScores = aaDbManager.getAssignmentOrderAndScore(
          dbManager.getAssignmentIdByName(assignmentName)).split(", ");
      while (ordersList.size() == 0) {
        ordersAndScores = aaDbManager.getAssignmentOrderAndScore(
            dbManager.getAssignmentIdByName(assignmentName)).split(", ");
      }
      for (String orderAndScore : ordersAndScores) {
        String[] token = orderAndScore.split(":");
        ordersList.add(token[0]);
      }
      if (ordersList.isEmpty() != true) {
        for (int i = 0; i < ordersList.size(); i++) {
          orderString += ordersList.get(i);
          if (i < ordersList.size() - 1) {
            orderString += ", ";
          }
        }
      }
      */

      ProjectTypeEnum assignmentTypeEnum = dbManager.getAssignmentType(assignmentName);

      JenkinsProjectConfig jenkinsProjectConfig;
      if ( assignmentTypeEnum.equals(ProjectTypeEnum.WEB) ) {
        jenkinsProjectConfig = new WebPipelineConfig(projectUrl, updateDbUrl,
                username, assignmentName,
                courseConfig.getTomcatServerIp() + "/publicApi/commits/screenshot/updateURL");
      } else if ( assignmentTypeEnum.equals(ProjectTypeEnum.ANDROID) ) {
        jenkinsProjectConfig = new AndroidPipelineConfig(projectUrl, updateDbUrl,
                username, assignmentName);
      } else {
        jenkinsProjectConfig = JenkinsProjectConfigFactory
                .getJenkinsProjectConfig(assignmentTypeEnum.getTypeName(), projectUrl, updateDbUrl,
                        username, assignmentName);
      }

      JenkinsService jenkinsService = JenkinsService.getInstance();
      jenkinsService.createJobV2(jobName, jenkinsProjectConfig.getXmlConfig());
      jenkinsService.buildJob(jobName);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public int[] arrayStringToIntArray(String metrics) {
    String[] items = metrics.split(",");
    int[] array = new int[items.length];

    for (int i = 0; i < items.length; i++) {
      array[i] = Integer.parseInt(items[i].trim());
    }

    return array;
  }

  public void randomPairMatching(int amount, String assignmentName) throws SQLException {
    int aid = dbManager.getAssignmentIdByName(assignmentName);
    List<User> userList = userService.getStudents();
    List<AssignmentUser> assignmentUserList = auDbManager.getAssignmentUserListByAid(aid);
    int randomNumber;

    while (true) {
      randomNumber = (int) (Math.random() * userList.size());
      if (assignmentUserList.get(randomNumber).getUid() != userList.get(0).getId()) {
        break;
      }
    }

    for (int count = 0; count < amount; count++) {
      int userSize = userList.size();
      List<PairMatching> insertPairMatchingList = new ArrayList<>();
      for (int order = 0; order < userSize; order++) {
        int totalCount = randomNumber + order + count;
        int mod = totalCount % userSize;

        if (assignmentUserList.get(mod).getUid() == userList.get(order).getId()) {
          randomNumber++;
          totalCount = randomNumber + order + count;
          mod = totalCount % userSize;
        }

        PairMatching pairMatching = new PairMatching();
        pairMatching.setAuId(assignmentUserList.get(mod).getId());
        pairMatching.setReviewId(userList.get(order).getId());
        //TODO pairMatching 沒有 status 改到 reviewRecord status
        /*
        pairMatching.setReviewStatusEnum(ReviewStatusEnum.INIT);
        */
        insertPairMatchingList.add(pairMatching);
      }

      pmDbManager.insertPairMatchingList(insertPairMatchingList);
    }
  }

  public void addAuid(String username, String assignmentName) {
    int aid = dbManager.getAssignmentIdByName(assignmentName);
    int uid = userDbManager.getUserIdByUsername(username);

    auDbManager.addAssignmentUser(aid, uid);
  }

  /**
   * delete assignment form pair review db.
   * @param assignmentName assignment name
   * @throws SQLException SQLException
   */
  public void deleteReviewDatabase(String assignmentName) throws SQLException {
    int aid = dbManager.getAssignmentIdByName(assignmentName);
    int reviewSettingId = rsDbManager.getReviewSettingIdByAid(aid);
    List<AssignmentUser> auList = auDbManager.getAssignmentUserListByAid(aid);

    for (AssignmentUser au : auList) {
      List<PairMatching> pmList = pmDbManager.getPairMatchingByAuId(au.getId());
      for (PairMatching pm : pmList) {
        List<ReviewRecordStatus> reviewRecordStatusList = rrsDbManager.getAllReviewRecordStatusByPairMatchingId(pm.getId());
        for (ReviewRecordStatus reviewRecordStatus : reviewRecordStatusList) {
          rrDbManager.deleteReviewRecordByRrsId(reviewRecordStatus.getId());
        }
        rrsDbManager.deleteReviewRecordStatusByPmId(pm.getId());
        pmDbManager.deletePairMatchingById(pm.getId());
      }
    }

    rsmDbManager.deleteReviewSettingMetricsByAssignmentId(reviewSettingId);
    rsDbManager.deleteReviewSettingByAId(aid);
  }

  public void deleteAssignmentDatabase(String name) {

    int aid = dbManager.getAssignmentIdByName(name);
    List<Integer> auidList = auDbManager.getAuids(aid);
    List<Integer> aaidList = aaDbManager.getAssignmentAssessmentIdByaId(aid);

    for (int aaid : aaidList) {  //Assignment_Assessment
      aaDbManager.deleteAssignmentAssessment(aaid);
    }

    for (int auid : auidList) { // CommitRecord
      List<Integer> cridList = crDbManager.getCommitRecordId(auid);
      for (int crid : cridList) { // ScreenShot
        srDbManager.deleteScreenshotByCrid(crid);
      }
      crDbManager.deleteRecord(auid);
    }
    auDbManager.deleteAssignmentUserByAid(aid);// Assignment_User
    assessmentTimeDbManager.deleteAssignmentTimeByAid(aid);//Assignment_Time
    dbManager.deleteAssignment(name);// Assignment

  }

  public void createPreviousAssignment(String username) {
    List<String> assignmentNames = dbManager.getAllAssignmentNames();

    for (String assignmentName : assignmentNames) {
      createAssignmentSettingsV2(username, assignmentName);
    }

  }

  public List<String> getAllAssignmentNames() {
    return dbManager.getAllAssignmentNames();
  }

  @GetMapping(
          value ="getAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getAssignmentFile(@QueryParam("fileName") String fileName) throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + fileName + ".zip");

    String filePath = assignmentSettingDir + fileName + ".zip";


    File file = new File(filePath);
    InputStream targetStream = new FileInputStream(file);

    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }

  @GetMapping(
          value ="getMvnAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getMvnAssignmentFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "MvnQuickStart.zip");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/MvnQuickStart.zip");



    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }

  @GetMapping(
          value ="getJavaAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getJavaAssignmentFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "JavacQuickStart.zip");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/JavacQuickStart.zip");



    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }

  @GetMapping(
          value ="getAndroidAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getAndroidAssignmentFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "AndroidQuickStart.zip");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/AndroidQuickStart.zip");


    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }


  @GetMapping(
          value ="getWebAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getWebAssignmentFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "WebQuickStart.zip");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/WebQuickStart.zip");


    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }

  @GetMapping(
          value ="getPythonAssignmentFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getPythonAssignmentFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "PythonQuickStart.zip");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/PythonQuickStart.zip");


    byte[] assignmentFile = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(assignmentFile, headers, HttpStatus.OK);

  }

  @PostMapping("order")
  public ResponseEntity<Object> modifyAssignmentOrderFile(
          @RequestParam("fileRadio") String fileType,
          @RequestParam("order") String assignmentCompileOrdersAndScore,
          @RequestParam("assignmentName") String assignmentName) {

    HttpHeaders headers = new HttpHeaders();

    //


    //-----order: Compile Failure:10, Coding Style Failure:80, Unit Test Failure:10
    List<String> ordersList = new ArrayList<>();
    String[] tokens = assignmentCompileOrdersAndScore.split(", ");
    for (String token:tokens) {
      ordersList.add(token);
    }
    //---------make pom.xml
    try {
      if (fileType.equals("maven")) {

//        String mavenResourcesZipPath =
//                this.getClass().getResource("/sample/MvnQuickStart.zip").getPath();


        InputStream mavenResourcesZip = this.getClass()
                .getResourceAsStream("/sample/MvnQuickStart.zip");

        File tempFile = File.createTempFile(String.valueOf(mavenResourcesZip.hashCode()), ".tmp");

        System.out.println("franky-test mavenResourcesZip.hashCode()");
        System.out.println(mavenResourcesZip.hashCode());

        copyInputStreamToFile(mavenResourcesZip, tempFile);

        MavenAssignmentSetting mas = new MavenAssignmentSetting();
        ZipHandler zipHandler = new ZipHandler();
        zipHandler.unzipFile(tempFile,
                assignmentSettingDir + assignmentName);


        mas.createAssignmentSetting(ordersList, assignmentName,
                mavenPomXmlSettingDir);

        File mavenPomXmlSettingFile = new File(mavenPomXmlSettingDir
                + assignmentName + "_pom.xml");
        File assignmentSettingFile = new File(assignmentSettingDir
                + assignmentName + "/pom.xml");

        JavaIoUtile.copyDirectoryCompatibilityMode(mavenPomXmlSettingFile, assignmentSettingFile);

        zipHandler.zipTestFolder(assignmentSettingDir + assignmentName);
      }
      return new ResponseEntity<Object>(headers, HttpStatus.OK);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private static void copyInputStreamToFile(InputStream inputStream, File file)
          throws IOException {

    // append = false
    try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
      int read;
      final int DEFAULT_BUFFER_SIZE = 8192;
      byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    }

  }

}
