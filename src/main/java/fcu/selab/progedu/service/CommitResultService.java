package fcu.selab.progedu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.StudentDashChoosePro;
import fcu.selab.progedu.data.CommitResult;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.CommitRecordStateDbManager;
import fcu.selab.progedu.db.CommitResultDbManager;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.status.StatusEnum;

@Path("commits/")
public class CommitResultService {
  CommitResultDbManager db = CommitResultDbManager.getInstance();
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  CommitRecordStateDbManager crsdb = CommitRecordStateDbManager.getInstance();
  UserDbManager userDb = UserDbManager.getInstance();
  ProjectDbManager projectDb = ProjectDbManager.getInstance();

  /**
   * get counts by different color.
   * 
   * @param type color
   * @return counts
   */
  @GET
  @Path("color")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCounts(@QueryParam("color") String type) {
    JSONObject commitCounts = db.getCounts(type);
    List<Integer> counts = new ArrayList<>();
    List<String> pnames = projectDb.listAllProjectNames();
    String status = "";
    for (String pname : pnames) {
      int count = commitCounts.optInt(pname);
      counts.add(count);
    }
    StatusEnum statusEnum = StatusEnum.getStatusEnum(type);
    switch (statusEnum) {
      case BUILD_SUCCESS:
        status = "success";
        break;
      case COMPILE_FAILURE:
        status = "compile failure";
        break;
      case CHECKSTYLE_FAILURE:
        status = "checkstyle failure";
        break;
      case UNIT_TEST_FAILURE:
        status = "JUnit failure";
        break;
      case INITIALIZATION:
        status = "not build";
        break;
      default:
        break;
    }

    JSONObject ob = new JSONObject();
    ob.put("data", counts);
    ob.put("name", status);
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * get Commit Sum.
   * 
   * @return sum
   */
  @GET
  @Path("count")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitSum() {
    List<Integer> array = crsdb.getCommitSum();
    JSONObject ob = new JSONObject();
    ob.put("data", array);
    ob.put("name", "commit counts");
    ob.put("type", "column");
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * get commit result by stuId and hw.
   * 
   * @param proName  project name
   * @param userName student id
   * @return hw, color, commit
   */
  @GET
  @Path("result")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getCommitResultByStudentAndHw(@QueryParam("proName") String proName,
      @QueryParam("userName") String userName) {
    int id = userDb.getUser(userName).getId();

    CommitResult commitResult = db.getCommitResultByStudentAndHw(id, proName);
    String circleColor = "circle " + commitResult.getColor();
    String result = userName + "_" + proName + "," + circleColor + ","
        + (commitResult.getCommit() + 1);

    return Response.ok().entity(result).build();
  }

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GET
  @Path("all")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getCommitResult() {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();

    List<User> users = userDb.listAllUsers();
    for (User user : users) {
      JSONObject ob = db.getCommitResultByStudent(user.getId());
      array.put(ob);
    }
    result.put("result", array);

    return Response.ok().entity(result.toString()).build();
  }

  /**
   * get commit result by stuId and hw.
   * 
   * @param userName student id
   * @param proName  project name
   * @return color
   */
  public String getCommitResult(String userName, String proName) {
    int id = userDb.getUser(userName).getId();
    return db.getCommitResultByStudentAndHw(id, proName).getColor();
  }

  /**
   * update stu project commit record.
   * 
   * @param userName stu id
   * @param proName  project name
   */
  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult(@FormParam("user") String userName,
      @FormParam("proName") String proName) {
    JSONObject ob = new JSONObject();
    if (!userName.equals("root")) {
      ob.put("userName", userName);
      ob.put("proName", proName);
      JenkinsService jenkinsService = new JenkinsService();
      JenkinsApi jenkinsApi = new JenkinsApi();
      StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();
      JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

      String[] result = jenkinsService.getColor(proName, userName).split(",");

      int commit = Integer.valueOf(result[1]) - 1;
      ob.put("commit", commit);

      List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(userName, proName);
      int num = 0;
      if (buildNum.size() > 1) {
        num = buildNum.size() - 1;
      }

      String color = "";
      String consoleText = checkErrorStyle(jenkinsData, userName, proName, buildNum.get(num));
      String proType = proName.substring(0, 3);
      boolean isSuccess = jenkinsApi.getJobBuildResultByConsoleText(consoleText, proType);
      if (!isSuccess) {
        boolean isCheckStyle = jenkinsApi.checkIsCheckstyleError(consoleText, proType);
        boolean isTestError = false;

        if (proType.equals("OOP")) {
          boolean isJunitError = jenkinsApi.checkIsJunitError(consoleText);
          isTestError = isJunitError;
        } else if (proType.equals("WEB")) {
          boolean isWebTestError = jenkinsApi.checkIsWebTestError(consoleText);
          isTestError = isWebTestError;
        }

        if (isTestError) {
          color = "CTF";
        } else if (isCheckStyle) {
          color = "CSF";
        } else {
          if (proType.equals("WEB")) {
            color = "blue";
          } else {
            color = "red";
          }
        }
        ob.put("isCheckStyle", isCheckStyle);
        ob.put("isJunitError", isTestError);
      } else {
        color = "blue";
      }

      if (commit == 0) {
        color = "gray";
      }
      ob.put("num", num);
      ob.put("color", color);

      switch (color) {
        case "blue":
          color = "S";
          break;
        case "red":
          color = "CPF";
          break;
        case "gray":
          color = "NB";
          break;

        default:
          break;
      }
      ob.put("color1", color);

      String buildApiJson = stuDashChoPro.getBuildApiJson(buildNum.get(num), userName, proName);
      String strDate = stuDashChoPro.getCommitTime(buildApiJson);
      String[] dates = strDate.split(" ");
      int id = userDb.getUser(userName).getId();
      ob.put("dates", dates[0]);
      ob.put("dates1", dates[1]);

      boolean check = db.checkJenkinsJobTimestamp(id, proName);
      if (check) {
        db.updateJenkinsCommitCount(id, proName, commit, color);
        db.updateJenkinsJobTimestamp(id, proName, strDate);
      } else {
        db.insertJenkinsCommitCount(id, proName, commit, color);
        db.updateJenkinsJobTimestamp(id, proName, strDate);
      }

      boolean inDb = commitRecordDb.checkRecord(id, proName, color, dates[0], dates[1]);
      if (!inDb) {
        commitRecordDb.insertCommitRecord(id, proName, color, dates[0], dates[1]);
      } else {
        commitRecordDb.updateRecordStatus(id, proName, color, dates[0], dates[1]);
      }

      updateCommitRecordState();
    }

    Logger log = Logger.getLogger("my.logger");
    log.setLevel(Level.ALL);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new SimpleFormatter());
    handler.setLevel(Level.ALL);
    log.addHandler(handler);
    log.fine(ob.toString());

    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * update Commit_Record_State DB's data.
   */
  public void updateCommitRecordState() {

    List<String> lsNames = new ArrayList<>();
    lsNames = projectDb.listAllProjectNames();

    for (String name : lsNames) {

      Map<String, Integer> map = commitRecordDb.getCommitRecordStateCounts(name);

      int bs = 0;
      int ini = 0;
      int utf = 0;
      int csf = 0;
      int cpf = 0;

      if (map.containsKey(StatusEnum.BUILD_SUCCESS.getTypeName())) {
        bs = map.get(StatusEnum.BUILD_SUCCESS.getTypeName());
      }

      if (map.containsKey(StatusEnum.INITIALIZATION.getTypeName())) {
        ini = map.get(StatusEnum.INITIALIZATION.getTypeName());
      }

      if (map.containsKey(StatusEnum.UNIT_TEST_FAILURE.getTypeName())) {
        utf = map.get(StatusEnum.UNIT_TEST_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.CHECKSTYLE_FAILURE.getTypeName())) {
        csf = map.get(StatusEnum.CHECKSTYLE_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.COMPILE_FAILURE.getTypeName())) {
        cpf = map.get(StatusEnum.COMPILE_FAILURE.getTypeName());
      }

      int ccs = 0;
      ccs = bs + utf + csf + cpf;

      boolean check;
      check = crsdb.checkCommitRecordStatehw(name);

      if (check) {
        crsdb.updateCommitRecordState(name, bs, csf, cpf, utf, ini, ccs);

      } else {
        crsdb.addCommitRecordState(name, bs, csf, cpf, utf, ini, ccs);
      }

    }

  }

  /**
   * check if error is checkstyle error.
   * 
   * @param jenkinsData connect jenkins
   * @param userName    stu id
   * @param proName     project name
   * @param num         build number
   * @return result string
   */
  public static String checkErrorStyle(JenkinsConfig jenkinsData, String userName, String proName,
      int num) {
    StringBuilder jsonStringBuilder = new StringBuilder();
    try {
      HttpURLConnection connUrl = null;
      String consoleUrl = jenkinsData.getJenkinsHostUrl() + "/job/" + userName + "_" + proName + "/"
          + num + "/consoleText";
      URL url = new URL(consoleUrl);
      connUrl = (HttpURLConnection) url.openConnection();
      connUrl.setReadTimeout(10000);
      connUrl.setConnectTimeout(15000);
      connUrl.setRequestMethod("GET");
      connUrl.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connUrl.getInputStream(), "UTF-8"));) {
        String line = "";

        while ((line = reader.readLine()) != null) {
          jsonStringBuilder.append(line);
        }
      }

    } catch (LoadConfigFailureException | IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return jsonStringBuilder.toString();
  }

  /**
   * delete build result of hw.
   * 
   * @param hw hw
   */
  public void deleteResult(String hw) {
    IDatabase database = new MySqlDatabase();
    Connection connection = database.getConnection();
    db.deleteResult(hw);
  }
}
