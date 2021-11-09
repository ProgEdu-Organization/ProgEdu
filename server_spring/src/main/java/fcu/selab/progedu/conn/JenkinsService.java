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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;

public class JenkinsService {
  private static JenkinsService instance = new JenkinsService();
  private JenkinsConfig jenkinsConfig;
  private String jenkinsRootUrl;
  private String jenkinsRootUsername;
  private String jenkinsRootPassword;
  private String jenkinsApiToken;
  private final String contentType = "Content-Type";
  private final String jenkinsCrumb = "Jenkins-Crumb";
  private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);

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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  public static JenkinsService getInstance() {
    return instance;
  }

  /**
   * get crumb
   * 
   * @return crumb
   */
  public String getCrumb() {
    String jenkinsUserName = "";
    String jenkinsPass = "";
    try {
      jenkinsUserName = JenkinsConfig.getInstance().getJenkinsRootUsername();
      jenkinsPass = JenkinsConfig.getInstance().getJenkinsRootPassword();
    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return getCrumb(jenkinsUserName, jenkinsPass);
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
      String encoding = "Basic " + encoder.encodeToString(input.getBytes());
      conn.setRequestProperty("Authorization", encoding);
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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
   * @param configPath (to do)
   */
  public void createJob(String jobName, String configPath) {

    try {
      String crumb = getCrumb();
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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }


  /**
   * (to do)
   *
   * @param jobName    (to do)
   * @param xmlString (to do)
   */
  public void createJobV2(String jobName, String xmlString) {

    try {
      String crumb = getCrumb();
      String url = jenkinsRootUrl + "/createItem?name=" + jobName;
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/xml");
      post.addHeader(jenkinsCrumb, crumb);

      StringEntity se = new StringEntity(xmlString, ContentType.create("text/xml", Consts.UTF_8));
      se.setChunked(true);
      post.setEntity(se);

      HttpClient client = new DefaultHttpClient();
      client.execute(post);

    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * (to do)
   * 
   * @param jobName (to do)
   */
  public void deleteJob(String jobName) {
    try {
      String crumb = getCrumb();
      HttpClient client = new DefaultHttpClient();
      String url = jenkinsRootUrl + "/job/" + jobName + "/doDelete";
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/x-www-form-urlencoded");
      post.addHeader(jenkinsCrumb, crumb);

      client.execute(post);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * (to do)
   * 
   * @param jobName (to do)
   */
  public void buildJob(String jobName) {
    try {
      String crumb = getCrumb();

      String url = jenkinsRootUrl + "/job/" + jobName + "/build";
      HttpPost post = new HttpPost(url);

      post.addHeader(contentType, "application/xml");
      post.addHeader(crumb, crumb);

      List<NameValuePair> params = new ArrayList<>();
      params.add((NameValuePair) new BasicNameValuePair("token", jenkinsApiToken));

      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpClient client = new DefaultHttpClient();
      client.execute(post);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * (to do)
   * 
   * @param jobName (to do)
   * @param num     (to do)
   * @return message (to do)
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
      String encoding = "Basic " + encoder.encodeToString(input.getBytes());
      conn.setRequestProperty("Authorization", encoding);
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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
   * 
   * @param jobName (to do)
   * @param num     (to do)
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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return sb.toString();
  }

  /**
   * get committer
   * 
   * @param jobName jenkins jobName
   * @param num     commit number
   * @return committer
   */
  public String getCommitter(String jobName, int num) {
    String committer = "";
    if (num == 1) {
      committer = "Administrator";
    } else {
      String console = getConsole(jobName, num);

      String beginStr = "Started by GitLab push by ";
      String endStr = "\n";
      int beginIndex = console.lastIndexOf(beginStr) + beginStr.length();
      int endIndex = console.indexOf(endStr, beginIndex);
      // extract committer
      committer = console.substring(beginIndex, endIndex);
    }

    return committer;

  }

  public String getJobName(String username, String projectName) {
    return username + "_" + projectName;

  }
}
