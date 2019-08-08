package fcu.selab.progedu.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class JenkinsService {
  private static JenkinsService instance = new JenkinsService();
  JenkinsConfig jenkinsConfig;

  public JenkinsService() {
    jenkinsConfig = JenkinsConfig.getInstance();
  }

  public static JenkinsService getInstance() {
    return instance;
  }

  public Response getJobBuildInfo(String jobName, int num) {
  }

  public String getCrumb(String username, String password) {

    String jenkinsCrumb = null;
    HttpURLConnection conn = null;

    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      String strUrl = jenkinsConfig.getJenkinsRootUrl() + "/crumbIssuer/api/json";
      URL url = new URL(strUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty("Authorization", "Basic " + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), "UTF-8"));) {
        String jsonString = reader.readLine();
        JSONObject jsonObj = new JSONObject(jsonString);
        jenkinsCrumb = jsonObj.getString("crumb");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return jenkinsCrumb;
  }

  public void createJob(String jobName, String jenkinsCrumb, String configPath) {
  }

  public void deleteJob(String jobName, String jenkinsCrumb) {
    HttpClient client = new DefaultHttpClient();

    try {
      String url = jenkinsConfig.getJenkinsRootUrl() + "/job/" + jobName + "/doDelete";
      HttpPost post = new HttpPost(url);

      post.addHeader("Content-Type", "application/x-www-form-urlencoded");
      post.addHeader("Jenkins-Crumb", jenkinsCrumb);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = EntityUtils.toString(response.getEntity());
        String result2 = resEntity.toString();
        System.out.println(jobName + " : " + result2);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void buildJob(String jobName, String jenkinsCrumb) {
    try {
      HttpClient client = new DefaultHttpClient();
      String url = jenkinsConfig.getJenkinsRootUrl() + "/job/" + jobName + "/build";
      HttpPost post = new HttpPost(url);

      post.addHeader("Content-Type", "application/xml");
      post.addHeader("Jenkins-Crumb", jenkinsCrumb);

      List<NameValuePair> params = new ArrayList<>();
      params
          .add((NameValuePair) new BasicNameValuePair("token", jenkinsConfig.getJenkinsApiToken()));

      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = resEntity.toString();
        System.out.println("httppost build " + jobName + " , result : " + result);
      }
    } catch (IOException | LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  private String getConsole(String jobName, int num) {
    return filterCommitMessage(getCompleteConsole(jobName, num));
  }

  private String getCompleteConsole(String jobName, int num) {

  }

  private String filterCommitMessage(String console) {
  }
}
