package fcu.selab.progedu.conn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class JenkinsService {
  private static JenkinsService instance = new JenkinsService();
  private JenkinsConfig jenkinsConfig;
  private String jenkinsRootUrl;
  private String jenkinsRootUsername;
  private String jenkinsRootPassword;
  private String jenkinsApiToken;
  private final String contentType = "Content-Type";
  private final String jenkinsCrumb = "Jenkins-Crumb";

  /**
   * (to do)
   */
  public JenkinsService() {
    try {
      jenkinsConfig = JenkinsConfig.getInstance();
      jenkinsRootUrl = jenkinsConfig.getJenkinsRootUrl();
      jenkinsRootUsername = jenkinsConfig.getJenkinsRootUsername();
      jenkinsRootPassword = jenkinsConfig.getJenkinsRootPassword();
      jenkinsApiToken = jenkinsConfig.getJenkinsApiToken();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public static JenkinsService getInstance() {
    return instance;
  }

  /**
   * (to do)
   * 
   * @param username (to do)
   * @param password (to do)
   * @return jenkinsCrumb (to do)
   */
  public String getCrumb(String username, String password) {

    String jenkinsCrumb = null;
    HttpURLConnection conn = null;

    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      String strUrl = jenkinsRootUrl + "/crumbIssuer/api/json";
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

  /**
   * (to do)
   * 
   * @param jobName    (to do)
   * @param crumb      (to do)
   * @param configPath (to do)
   */
  public void createJob(String jobName, String crumb, String configPath) {
    try {
      String url = jenkinsRootUrl + "/createItem?name=" + jobName;
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/xml");
      post.addHeader(jenkinsCrumb, crumb);

      String config = getConfig(configPath);
      StringEntity se = new StringEntity(config, ContentType.create("text/xml", Consts.UTF_8));
      se.setChunked(true);
      post.setEntity(se);

      HttpClient client = new DefaultHttpClient();
//      HttpResponse response = 
      client.execute(post);
//      HttpEntity resEntity = response.getEntity();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * (to do)
   * 
   * @param jobName (to do)
   * @param crumb   (to do)
   */
  public void deleteJob(String jobName, String crumb) {

    try {
      HttpClient client = new DefaultHttpClient();
      String url = jenkinsRootUrl + "/job/" + jobName + "/doDelete";
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/x-www-form-urlencoded");
      post.addHeader(jenkinsCrumb, crumb);

//      HttpResponse response = 
      client.execute(post);
//      HttpEntity resEntity = response.getEntity();
//      if (resEntity != null) {
//        String result = EntityUtils.toString(response.getEntity());
//        String result2 = resEntity.toString();
//        System.out.println(jobName + " : " + result2);
//      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * (to do)
   * @param jobName (to do)
   * @param jenkinsCrumb (to do)
   */
  public void buildJob(String jobName, String jenkinsCrumb) {
    try {
      
      String url = jenkinsRootUrl + "/job/" + jobName + "/build";
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/xml");
      post.addHeader(jenkinsCrumb, jenkinsCrumb);

      List<NameValuePair> params = new ArrayList<>();
      params.add((NameValuePair) new BasicNameValuePair("token", jenkinsApiToken));

      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);
      
      HttpClient client = new DefaultHttpClient();
//      HttpResponse response = 
      client.execute(post);
//      HttpEntity resEntity = response.getEntity();
//      if (resEntity != null) {
//        String result = resEntity.toString();
//        System.out.println("httppost build " + jobName + " , result : " + result);
//      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * (to do)
   * @param jobName (to do)
   * @param num (to do)
   * @return  message (to do)
   */
  public String getCommitMessage(String jobName, int num) {
    String console = getCompleteConsole(jobName, num);
    String beginStr = "Commit message: ";
    String endStr = "\n";
    int beginIndex = console.indexOf(beginStr) + beginStr.length();
    int endIndex = console.indexOf(endStr, beginIndex);
    // extract commit message
    String message = console.substring(beginIndex, endIndex);
    return message;
  }

  public String getConsole(String jobName, int num) {
    return filterCommitMessage(getCompleteConsole(jobName, num));
  }

  private String getCompleteConsole(String jobName, int num) {
    String consoleUrl = getConsoleUrl(jobName, num);
    String console = "";
    HttpURLConnection conn = null;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      URL url = new URL(consoleUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = jenkinsRootUsername + ":" + jenkinsRootPassword;
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
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), "UTF-8"));) {
        String str = "";
        StringBuilder sb = new StringBuilder();
        while (null != (str = br.readLine())) {
          sb.append("\n");
          sb.append(str);
        }
        console = sb.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return console;
  }

  private String filterCommitMessage(String console) {
    StringBuilder sb = new StringBuilder(console);
    String startStr = "Commit message: ";
    String endStr = "\n";
    int startIndex = console.indexOf(startStr);
    int endIndex = console.indexOf(endStr, startIndex);
    // Delete commit message
    sb.delete(startIndex, endIndex);
    return sb.toString();
  }

  /**
   * (to do)
   * @param jobName (to do)
   * @param num (to do)
   * @return consoleUrl (to do)
   */
  public String getConsoleUrl(String jobName, int num) {
    return (jenkinsRootUrl + "/job/" + jobName + "/" + num + "/consoleText");
  }

  /**
   * Get the config file
   * 
   * @param filePath Config file path
   * @return config content
   */
  private String getConfig(String filePath) {
    StringBuilder sb = new StringBuilder();
    String strConfig = null;
    try (FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fis, "UTF8");
        BufferedReader buf = new BufferedReader(reader);) {
      while ((strConfig = buf.readLine()) != null) {
        sb.append(strConfig);
        sb.append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
}
