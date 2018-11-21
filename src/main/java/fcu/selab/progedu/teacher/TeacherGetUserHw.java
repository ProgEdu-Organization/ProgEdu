package fcu.selab.progedu.teacher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class TeacherGetUserHw {
  private static final String ENCODING = "UTF-8";
  private static final String PATH = "/api/v3/projects/owned?private_token=";
  GitlabConfig gitData = GitlabConfig.getInstance();

  private String hostUrl;

  private String getGlilabUrl() {
    try {
      hostUrl = gitData.getGitlabHostUrl();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return hostUrl;
  }

  /**
   * Use http connect to get project event
   * 
   * @param strUrl project url
   * @return event number
   */
  public int httpGetProjectEvent(String strUrl) {
    HttpURLConnection conn = null;
    int totalCount = 0;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // create connection

      URL url = new URL(strUrl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // read data
      try (InputStreamReader isr = new InputStreamReader(conn.getInputStream(), ENCODING);
          BufferedReader reader = new BufferedReader(isr)) {
        String jsonString1 = reader.readLine();

        JSONArray ja = new JSONArray(jsonString1);
        int jsonArrayLength = ja.length();

        for (int i = 0; i < jsonArrayLength; i++) {
          JSONObject oj = ja.getJSONObject(i);
          JSONObject ojData = oj.getJSONObject("data");
          int totalCommitCount = ojData.getInt("total_commits_count");
          if (totalCommitCount == 1) {
            totalCount++;
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return totalCount;
  }

  /**
   * Use http connect to get all project name owned by student
   * 
   * @param privateToken suer's private token
   * @return lits of all project name
   */
  public List<String> httpGetStudentOwnedProjectName(String privateToken) {
    HttpURLConnection conn = null;
    List<String> projectsName = new ArrayList<>();
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // create connection
      String strurl = getGlilabUrl() + PATH + privateToken;
      URL url = new URL(strurl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // read data
      try (InputStreamReader isr = new InputStreamReader(conn.getInputStream(), ENCODING);
          BufferedReader reader = new BufferedReader(isr)) {
        String jsonString1 = reader.readLine();

        JSONArray ja = new JSONArray(jsonString1);
        int jsonArrayLength = ja.length();
        for (int i = 0; i < jsonArrayLength; i++) {
          JSONObject jsonObject = ja.getJSONObject(i);
          String name = jsonObject.getString("name");
          projectsName.add(name);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return projectsName;
  }

  /**
   * Use http connect to get all project url owned by student
   * 
   * @param privateToken user's private token
   * @return list of all project url
   */
  public List<String> httpGetStudentOwnedProjectUrl(String privateToken) {
    HttpURLConnection conn = null;
    List<String> projectsUrl = new ArrayList<>();
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // create connection
      String strurl = getGlilabUrl() + PATH + privateToken;
      URL url = new URL(strurl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // read data
      try (InputStreamReader isr = new InputStreamReader(conn.getInputStream(), ENCODING);
          BufferedReader reader = new BufferedReader(isr)) {
        String jsonString1 = reader.readLine();

        JSONArray ja = new JSONArray(jsonString1);
        int jsonArrayLength = ja.length();
        for (int i = 0; i < jsonArrayLength; i++) {
          JSONObject jsonObject = ja.getJSONObject(i);
          String webUrl = jsonObject.getString("web_url");
          webUrl = webUrl.replace("http://0912fe2b3e43", gitData.getGitlabHostUrl());
          projectsUrl.add(webUrl);
        }
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return projectsUrl;
  }

  /**
   * Use http connect to get all project owned by student
   * 
   * @param privateToken user's private token
   * @return list of all project id
   */
  public List<Integer> httpGetStudentOwnedProjectId(String privateToken) {
    HttpURLConnection conn = null;
    List<Integer> projectsId = new ArrayList<>();
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // create connection
      String strurl = getGlilabUrl() + PATH + privateToken;
      URL url = new URL(strurl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      // read data
      try (InputStreamReader isr = new InputStreamReader(conn.getInputStream(), ENCODING);
          BufferedReader reader = new BufferedReader(isr)) {
        String jsonString1 = reader.readLine();

        JSONArray ja = new JSONArray(jsonString1);
        int jsonArrayLength = ja.length();
        for (int i = 0; i < jsonArrayLength; i++) {
          JSONObject jsonObject = ja.getJSONObject(i);
          int id = jsonObject.getInt("id");
          projectsId.add(id);
        }
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return projectsId;
  }
}
