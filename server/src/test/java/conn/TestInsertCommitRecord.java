package conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gitlab.api.models.GitlabProject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
/*
public class TestInsertCommitRecord {

  public static void main(String[] args) {
    UserDbManager db = UserDbManager.getInstance();
    List<User> users = db.listAllUsers();
    ProjectDbManager pDb = ProjectDbManager.getInstance();
    List<Project> dbProjects = pDb.listAllProjects();
    List<GitlabProject> gitProjects = new ArrayList<>();
    Conn conn = Conn.getInstance();
    CommitRecordDbManager commitDb = CommitRecordDbManager.getInstance();
    StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

    for (User user : users) {
      String userName = user.getUserName();
      gitProjects = conn.getProject(user);
      Collections.reverse(gitProjects);
      for (Project dbProject : dbProjects) {
        if (!dbProject.getName().equals("OOP-HW10") && !dbProject.getName().equals("OOP-HW11")) {
          continue;
        }
        String proName = null;
        for (GitlabProject gitProject : gitProjects) {
          if (dbProject.getName().equals(gitProject.getName())) {
            proName = dbProject.getName();
            List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(userName, proName);
            for (Integer num : buildNum) {
              String buildApiJson = stuDashChoPro.getBuildApiJson(num, userName, proName);
              String strDate = stuDashChoPro.getCommitTime(buildApiJson);
              String[] dates = strDate.split(" ");
              String color = stuDashChoPro.getCommitColor(num, userName, proName, buildApiJson);
              if (num == 1) {
                continue;
              }
              if (color.equals("red")) {
                String style = checkErrorStyle(jenkinsData, userName, proName, num);
                boolean ifCheckStyle = style.contains("Checkstyle violation");
                if (ifCheckStyle) {
                  color = "orange";
                }
              }
              String hw = proName;
              boolean inDb = commitDb.checkRecord(user.getId(), hw, color, dates[0], dates[1]);
              if (!inDb) {
                boolean check = commitDb.insertCommitRecord(user.getId(), hw, color, dates[0],
                    dates[1]);
                if (check) {
                  System.out.println(user.getId() + ", " + hw + ", " + color + ", " + strDate);
                }
              }
            }
            break;
          }
        }
      }
    }
  }

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

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(connUrl.getInputStream(), "UTF-8"));
      String line = "";
      while ((line = reader.readLine()) != null) {
        jsonStringBuilder.append(line);
      }
      reader.close();
    } catch (LoadConfigFailureException | IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return jsonStringBuilder.toString();
  }
}
*/