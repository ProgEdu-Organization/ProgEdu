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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.gitlab.api.models.GitlabProject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class HttpConnect {

  private static HttpConnect httpConn = new HttpConnect();
  private String filePath = "README.md";
  private String branchName = "master";
  private String encoding = "test";
  private String commitMessage = "README";
  private GitlabConfig gitlab = GitlabConfig.getInstance();
  private JenkinsConfig jenkins = JenkinsConfig.getInstance();

  public static HttpConnect getInstance() {
    return httpConn;
  }

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
   * set gitlab webhook that trigger jenkins job to build
   * 
   * @param username group name
   * @param project  gitlab project
   */
  public void setGitlabWebhook(String username, GitlabProject project)
      throws IOException, LoadConfigFailureException {
    // for example,
    // http://localhost:80/api/v4/projects/3149/hooks?url=http://localhost:8888/project/webhook
    String gitlabWebhookApi = gitlab.getGitlabHostUrl() + "/api/v4/projects/" + project.getId()
        + "/hooks";
    String jenkinsJobUrl = jenkins.getJenkinsHostUrl() + "/project/" + username + "_"
        + project.getName();
    HttpPost post = new HttpPost(gitlabWebhookApi);
    post.addHeader("PRIVATE-TOKEN", gitlab.getGitlabApiToken());
    // Request parameters
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("url", jenkinsJobUrl));
    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    HttpClients.createDefault().execute(post);
  }
}
