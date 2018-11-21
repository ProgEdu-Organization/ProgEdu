package fcu.selab.progedu.conn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class HttpConnect {

  private static HttpConnect httpConn = new HttpConnect();
  private String filePath = "README.md";
  private String branchName = "master";
  private String encoding = "test";
  private String commitMessage = "README";

  public static HttpConnect getInstance() {
    return httpConn;
  }

  GitlabConfig gitData = GitlabConfig.getInstance();

  UserDbManager userDb = UserDbManager.getInstance();

  GitlabConfig gitlab = GitlabConfig.getInstance();

  /**
   * Httppost Readme to gitlab when creating project
   * 
   * @param url     Project url
   * @param content Readme content
   */
  public void httpPostReadme(String url, String content) {
    HttpClient client = new DefaultHttpClient();
    try {
      final HttpPost post = new HttpPost(url);
      List<NameValuePair> params = paramsAdd(filePath, branchName, encoding, content,
          commitMessage);
      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        System.out.println("HttpPost Readme Success");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Httpput readme to gitlab when creating project if readme is exist
   * 
   * @param url     Project url
   * @param content Readme content
   */
  public void httpPutReadme(String url, String content) {
    HttpClient client = new DefaultHttpClient();
    try {
      final HttpPut post = new HttpPut(url);
      List<NameValuePair> params = paramsAdd(filePath, branchName, encoding, content,
          commitMessage);
      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        System.out.println("HttpPut Readme Success");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Httppost file to gitlab when creating project
   * 
   * @param filePath    File path
   * @param url         Project url
   * @param fileContent File content
   */
  public void httpPostFile(String filePath, String url, String fileContent) {
    HttpClient client = new DefaultHttpClient();
    try {
      final HttpPost post = new HttpPost(url);
      List<NameValuePair> params = paramsAdd(filePath, branchName, "text", fileContent,
          commitMessage);

      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = resEntity.toString();
        System.out.println("HttpPost File Success : " + result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<NameValuePair> paramsAdd(String filePath, String branchName, String encoding,
      String content, String commitMessage) {
    List<NameValuePair> params = new ArrayList<>();
    params.add((NameValuePair) new BasicNameValuePair("file_path", filePath));
    params.add((NameValuePair) new BasicNameValuePair("branch_name", branchName));
    params.add((NameValuePair) new BasicNameValuePair("encoding", encoding));
    params.add((NameValuePair) new BasicNameValuePair("content", content));
    params.add((NameValuePair) new BasicNameValuePair("commit_message", commitMessage));
    return params;
  }

  /**
   * transfer project from root to group
   * 
   * @param groupId   group id
   * @param projectId project id
   */
  public void transferProjectToGroup(int groupId, int projectId) {
    HttpClient client = new DefaultHttpClient();
    String url = "";
    try {
      url = gitlab.getGitlabHostUrl() + "/api/v3/groups/" + groupId + "/projects/" + projectId
          + "?private_token=" + gitlab.getGitlabApiToken();
      HttpPost post = new HttpPost(url);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = resEntity.toString();
        System.out.println("httppost build " + groupId + " , result : " + result);
      }
    } catch (IOException | LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }
}
