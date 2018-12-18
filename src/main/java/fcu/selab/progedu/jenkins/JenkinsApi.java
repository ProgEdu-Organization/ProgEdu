package fcu.selab.progedu.jenkins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class JenkinsApi {
  private static JenkinsApi instance = new JenkinsApi();
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String JENKINS_CRUMB = "Jenkins-Crumb";
  private static final String AUTHORIZATION = "Authorization";
  private static final String BASIC = "Basic ";
  private static final String UTF_8 = "UTF-8";
  private static final String PROGEDU_DB_URL = "progeduDbUrl";
  private static final String JOB_NAME = "jobName";
  private static final String PRO_NAME = "proName";

  private Conn conn = Conn.getInstance();

  GitlabConfig gitData = GitlabConfig.getInstance();
  CourseConfig courseData = CourseConfig.getInstance();

  private String gitlabHostUrl;

  JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

  private String jenkinsRootUrl;
  private String jenkinsHostUrl;
  private String jenkinsRootUsername;
  private String jenkinsRootPassword;
  private String jenkinsApiToken;

  /**
   * constructor
   * 
   * @throws LoadConfigFailureException on properties call error
   */
  public JenkinsApi() {
    try {
      gitlabHostUrl = gitData.getGitlabHostUrl();
      jenkinsRootUrl = jenkinsData.getJenkinsRootUrl();
      jenkinsHostUrl = jenkinsData.getJenkinsHostUrl();
      jenkinsRootUsername = jenkinsData.getJenkinsRootUsername();
      jenkinsRootPassword = jenkinsData.getJenkinsRootPassword();
      jenkinsApiToken = jenkinsData.getJenkinsApiToken();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public static JenkinsApi getInstance() {
    return instance;
  }

  /**
   * Create gitlab root job on jenkins
   * 
   * @param proName      The project name
   * @param jenkinsCrumb The jenkins crumb
   * @param fileType     The file type
   * @param sb           The config build job command
   */
  public void createRootJob(String proName, String jenkinsCrumb, String fileType,
      StringBuilder sb) {

    // ---Create Jenkins Job---
    String proUrl = gitlabHostUrl + "/root/" + proName + ".git";
    System.out.println("proUrl : " + proUrl);
    String filePath = modifyXml("root", proName, proUrl, fileType, sb);
    postCreateJob("root", proName, proUrl, jenkinsCrumb, filePath);
    backupConfig(filePath, proName);
    // ------------------------
  }

  /**
   * Create all user jenkins job
   * 
   * @param proName      Project name
   * @param jenkinsCrumb Jenkins crumb
   * @param fileType     File type
   * @param sb           The config build job command
   * @throws LoadConfigFailureException on properties call error
   * @throws IOException                on gitlab getuser call error
   */
  public void createJenkinsJob(String userName, String proName, String jenkinsCrumb,
      String fileType, StringBuilder sb) {

    // ---Create Jenkins Job---
    String proUrl = gitlabHostUrl + "/" + userName + "/" + proName + ".git";
    String filePath = modifyXml(userName, proName, proUrl, fileType, sb);
    postCreateJob(userName, proName, proUrl, jenkinsCrumb, filePath);
    // ------------------------
  }

  /**
   * Httppost to create jenkins job
   * 
   * @param proUrl       Gitlab project url
   * @param jenkinsCrumb Jenkins crumb
   * @param filePath     File path
   */
  public void postCreateJob(String userName, String proName, String proUrl, String jenkinsCrumb,
      String filePath) {
    HttpClient client = new DefaultHttpClient();
    String jobName = userName + "_" + proName;

    String url = jenkinsRootUrl + "/createItem?name=" + jobName;
    System.out.println("jenkins url: " + url);
    try {
      HttpPost post = new HttpPost(url);

      post.addHeader(CONTENT_TYPE, "application/xml");
      post.addHeader(JENKINS_CRUMB, jenkinsCrumb);

      StringBuilder sbConfig = getConfig(filePath);
      StringEntity se = new StringEntity(sbConfig.toString(),
          ContentType.create("text/xml", Consts.UTF_8));
      se.setChunked(true);
      post.setEntity(se);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * modify job config
   * 
   * @param userName student id
   * @param proName  project name
   * @param proUrl   project url
   * @param fileType job type
   * @param sb       command
   * @return config file path
   */
  public String modifyXml(String userName, String proName, String proUrl, String fileType,
      StringBuilder sb) {
    String filePath = null;
    String configType = getConfigType(fileType);
    filePath = this.getClass().getResource("/jenkins/" + configType).getPath();

    try {
      String tomcatUrl;
      tomcatUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/project/checksum?proName="
          + proName;
      String updateDbUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/commits/update";
      // proUrl project name toLowerCase
      proUrl = proUrl.toLowerCase();
      modifyXmlFileUrl(filePath, proUrl);
      if ("Javac".equals(fileType)) {
        modifyXmlFileCommand(filePath, sb, updateDbUrl, userName, proName);
      }
      if ("Web".equals(fileType)) {
        modifyWebXmlFile(filePath, sb, updateDbUrl, userName, proName, tomcatUrl);
      }
      if ("Maven".equals(fileType)) {
        modifyXmlFileProgEdu(filePath, userName, proName, tomcatUrl, updateDbUrl);
      }
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return filePath;
  }

  /**
   * backup job config
   * 
   * @param source source file
   */
  public void backupConfig(String source, String proName) {
    String tempDir = System.getProperty("java.io.tmpdir");
    try {
      File sourceFile = new File(source);
      File desFile = new File(tempDir + "/configs/" + proName + ".xml");
      FileUtils.copyFile(sourceFile, desFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the jenkins crumb
   * 
   * @param username Jenkins root user name
   * @param password Jenkins root password
   * @return jenkins crumb
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
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
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
   * Get the config file
   * 
   * @param filePath Config file path
   * @return config content
   */
  public StringBuilder getConfig(String filePath) {
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
    return sb;
  }

  /**
   * Change the config file's project url
   * 
   * @param filePath Config file path
   * @param url      Project url
   */
  public void modifyXmlFileUrl(String filePath, String url) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      Node ndUrl = doc.getElementsByTagName("url").item(0);
      ndUrl.setTextContent(url);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config file command (Maven or Javac)
   * 
   * @param filePath    Config file path
   * @param sb          Command string
   * @param updateDbUrl tomcat db url
   * @param userName    user name
   */
  public void modifyXmlFileCommand(String filePath, StringBuilder sb, String updateDbUrl,
      String userName, String proName) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      Node ndUrl = doc.getElementsByTagName("command").item(0);
      ndUrl.setTextContent(sb.toString());

      Node progeduDbUrl = doc.getElementsByTagName(PROGEDU_DB_URL).item(0);
      progeduDbUrl.setTextContent(updateDbUrl);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config file command (Maven or Javac)
   * 
   * @param filePath Config file path
   * @param userName user name
   */
  public void modifyXmlFileCommand(String filePath, String userName, String proName) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config ProgEdu plugin content
   * 
   * @param userName    user name
   * @param proName     project name
   * @param tomcatUrl   tomcat project url
   * @param updateDbUrl updating db url
   */
  public void modifyXmlFileProgEdu(String filePath, String userName, String proName,
      String tomcatUrl, String updateDbUrl) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      String strJobName = userName + "_" + proName;
      Node jobName = doc.getElementsByTagName(JOB_NAME).item(0);
      jobName.setTextContent(strJobName);

      Node testFileName = doc.getElementsByTagName("testFileName").item(0);
      testFileName.setTextContent(proName);

      Node proDetailUrl = doc.getElementsByTagName("proDetailUrl").item(0);
      proDetailUrl.setTextContent(tomcatUrl);

      Node progeduDbUrl = doc.getElementsByTagName(PROGEDU_DB_URL).item(0);
      progeduDbUrl.setTextContent(updateDbUrl);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config ProgEdu plugin content
   * 
   * @param userName user name
   * @param proName  project name
   */
  public void modifyXmlFileProgEdu(String filePath, String userName, String proName) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      String strJobName = userName + "_" + proName;
      Node jobName = doc.getElementsByTagName(JOB_NAME).item(0);
      jobName.setTextContent(strJobName);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config file command (Maven or Javac)
   * 
   * @param filePath    Config file path
   * @param sb          Command string
   * @param updateDbUrl tomcat db url
   * @param userName    user name
   * @param tomcatUrl   tomcat project url
   */
  public void modifyWebXmlFile(String filePath, StringBuilder sb, String updateDbUrl,
      String userName, String proName, String tomcatUrl) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      String strJobName = userName + "_" + proName;
      Node jobName = doc.getElementsByTagName(JOB_NAME).item(0);
      jobName.setTextContent(strJobName);

      Node ndUrl = doc.getElementsByTagName("command").item(0);
      ndUrl.setTextContent(sb.toString());

      Node testFileName = doc.getElementsByTagName("testFileName").item(0);
      testFileName.setTextContent(proName);

      Node progeduDbUrl = doc.getElementsByTagName(PROGEDU_DB_URL).item(0);
      progeduDbUrl.setTextContent(updateDbUrl);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      Node proDetailUrl = doc.getElementsByTagName("proDetailUrl").item(0);
      proDetailUrl.setTextContent(tomcatUrl);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Change the config ProgEdu plugin content
   * 
   * @param userName user name
   * @param proName  project name
   */
  public void modifyWebXmlFile(String filePath, String userName, String proName) {
    try {
      String filepath = filePath;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filepath);

      String strJobName = userName + "_" + proName;
      Node jobName = doc.getElementsByTagName(JOB_NAME).item(0);
      jobName.setTextContent(strJobName);

      Node user = doc.getElementsByTagName("user").item(0);
      user.setTextContent(userName);

      Node ndProName = doc.getElementsByTagName(PRO_NAME).item(0);
      ndProName.setTextContent(proName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filepath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the jenkins job status color
   * 
   * @param jobApiJson job api json
   * @return color
   */
  public String getJobJsonColor(String jobApiJson) {
    String color = null;
    JSONObject j1 = new JSONObject(jobApiJson);
    color = j1.getString("color");
    return color;
  }

  /**
   * get jekins job all build number
   * 
   * @param username jenkins user name
   * @param password jenkins user password
   * @param jobUrl   jenkins job url
   * @return number list
   */
  public List<Integer> getJenkinsJobAllBuildNumber(String username, String password,
      String jobUrl) {
    List<Integer> numbers = new ArrayList<>();
    HttpURLConnection conn = null;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      URL url = new URL(jobUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
        String jsonString1 = reader.readLine();

        JSONObject j1 = new JSONObject(jsonString1);
        JSONArray builds = j1.getJSONArray("builds");
        for (int i = 0; i < builds.length(); i++) {
          JSONObject build = builds.getJSONObject(i);
          int buildNumber = build.optInt("number");
          numbers.add(buildNumber);
        }
      }

    } catch (Exception e) {
      System.out.print("Jenkins get job build result error : ");
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    Collections.reverse(numbers);
    return numbers;
  }

  /**
   * Jenkins build the job
   * 
   * @param proName      Jenkins job name
   * @param jenkinsCrumb Jenkins crumb
   * @throws IOException on gitlab getuser call error
   */
  public void buildJob(String userName, String proName, String jenkinsCrumb) {
    String jobName = null;
    jobName = userName + "_" + proName;
    postBuildJob(jobName, jenkinsCrumb);
  }

  /**
   * Httppost to build jenkins job
   * 
   * @param jobName      Jenkins job name
   * @param jenkinsCrumb Jenkins crumb
   */
  public void postBuildJob(String jobName, String jenkinsCrumb) {
    HttpClient client = new DefaultHttpClient();

    String url = jenkinsRootUrl + "/job/" + jobName + "/build";
    try {
      HttpPost post = new HttpPost(url);

      post.addHeader(CONTENT_TYPE, "application/xml");
      post.addHeader(JENKINS_CRUMB, jenkinsCrumb);

      List<NameValuePair> params = new ArrayList<>();
      params.add((NameValuePair) new BasicNameValuePair("token", jenkinsApiToken));

      UrlEncodedFormEntity ent = null;
      ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      post.setEntity(ent);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = resEntity.toString();
        System.out.println("httppost build " + jobName + " , result : " + result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get a list of Jenkins jobs
   * 
   * @return list
   */
  public List<String> getJobNameList() {
    String jobUrl = jenkinsHostUrl + "/api/json";
    String username = jenkinsRootUsername;
    String password = jenkinsRootPassword;
    List<String> jobNames = new ArrayList<>();
    HttpURLConnection conn = null;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      URL url = new URL(jobUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
        String jsonString1 = reader.readLine();
        reader.close();

        JSONObject jsonObject = new JSONObject(jsonString1);
        JSONArray ja = jsonObject.getJSONArray("jobs");

        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          String name = jo.getString("name");
          jobNames.add(name);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return jobNames;
  }

  /**
   * Delete the jenkins job
   * 
   * @param jobName Jenkins job name
   */
  public void deleteJob(String jobName, String crumb) {
    HttpClient client = new DefaultHttpClient();

    try {
      String url = jenkinsData.getJenkinsRootUrl() + "/job/" + jobName + "/doDelete";
      HttpPost post = new HttpPost(url);

      post.addHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");
      post.addHeader(JENKINS_CRUMB, crumb);

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

  /**
   * Get
   * 
   * @param strUrl strUrl
   * @return console
   */
  public String getCompleteConsoleText(String userName, String proName, int num) {
    String url = getConsoleUrl(userName, proName, num);
    return getCompleteConsoleText(url);
  }

  /**
   * Get
   * 
   * @param strUrl strUrl
   * @return console
   */
  public String getCompleteConsoleText(String strUrl) {
    String console = "";
    HttpURLConnection conn = null;

    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      URL url = new URL(strUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = jenkinsData.getJenkinsRootUsername() + ":"
          + jenkinsData.getJenkinsRootPassword();
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
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

  /**
   * Get
   * 
   * @param strUrl strUrl
   * @return console
   */
  public String getConsoleText(String strUrl) {
    String completeConsole = getCompleteConsoleText(strUrl);
    return deleteCommitMessage(completeConsole);
  }

  /**
   * Get
   * 
   * @param userName proName num
   * @return console
   */
  public String getConsoleText(String userName, String proName, int num) {
    String url = getConsoleUrl(userName, proName, num);
    return getConsoleText(url);
  }

  /**
   * Delete commit message
   * 
   * @param console text
   * @return console
   */
  private String deleteCommitMessage(String console) {
    StringBuilder sb = new StringBuilder(console);
    String startStr = "Commit message: ";
    String endStr = " > git rev-list";
    int startIndex = console.indexOf(startStr);
    int endIndex = console.lastIndexOf(endStr);
    // Delete commit message
    sb.delete(startIndex, endIndex);
    return sb.toString();
  }

  /**
   * Get commitMessage
   * 
   * @param console console
   * @return commitMessage commitMessage
   */

  public String getConsoleTextCommitMessage(String console) {
    String commitMessage;
    String startStr = "Commit message: ";
    String endStr = "\n";
    int startIndex = console.indexOf(startStr) + startStr.length() + 1;
    int endIndex = console.indexOf(endStr, startIndex) - 1;

    commitMessage = console.substring(startIndex, endIndex);
    return commitMessage;
  }

  /**
   * Get jenkins console url
   * 
   * @param strUrl strUrl
   * @return console
   */
  public String getConsoleUrl(String userName, String proName, int num) {
    return (jenkinsHostUrl + "/job/" + userName + "_" + proName + "/" + num + "/consoleText");
  }

  /**
   * Get Job Api Json
   * 
   * @param username jenkins username
   * @param password jenkins password
   * @param jobUrl   job url
   * @return api json
   */
  public String getJobApiJson(String username, String password, String jobUrl) {
    String result = null;
    HttpURLConnection conn = null;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      URL url = new URL(jobUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
        String jsonString1 = reader.readLine();
        result = jsonString1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return result;
  }

  /**
   * Get checkstyle description
   * 
   * @param jobApiJson job api json
   * @return description
   */
  public JSONObject getCheckstyleDes(String jobApiJson) {
    JSONObject jsonCheckstyle = null;
    JSONObject jsonJobApi = new JSONObject(jobApiJson);
    JSONArray jsonHealthReport = jsonJobApi.getJSONArray("healthReport");
    if (jsonHealthReport.length() == 2) {
      jsonCheckstyle = jsonHealthReport.getJSONObject(1);
    }
    return jsonCheckstyle;
  }

  /**
   * Get error amount
   * 
   * @param checkstyleDes description
   * @return amount
   */
  public int getCheckstyleErrorAmount(JSONObject checkstyleDes) {
    String description = checkstyleDes.optString("description");
    int num = Character.getNumericValue(description.charAt(description.length() - 1));
    int amount = 0;
    int index = 0;
    if (num >= 0 && num <= 9) {
      index = description.indexOf("is") + 3;
      amount = Integer.valueOf(description.substring(index, description.length()));
    }

    return amount;
  }

  /**
   * Check if project is Maven
   * 
   * @param jobApiJson api json
   * @return boolean
   */
  public Boolean checkProjectIsMvn(String jobApiJson) {
    String checkstyleText = "checkstyle";
    Boolean found;
    found = jobApiJson.contains(checkstyleText);
    return found;
  }

  /**
   * Get job build api json
   * 
   * @param username root username
   * @param password root password
   * @param buildUrl job build url
   * @return api json
   */
  public String getJobBuildApiJson(String username, String password, String buildUrl) {
    String buildApiJson = null;
    HttpURLConnection conn = null;
    try {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }

      URL url = new URL(buildUrl);
      conn = (HttpURLConnection) url.openConnection();
      String input = username + ":" + password;
      Base64.Encoder encoder = Base64.getEncoder();
      String encoding = encoder.encodeToString(input.getBytes());
      conn.setRequestProperty(AUTHORIZATION, BASIC + encoding);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.connect();
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), UTF_8));) {
        String jsonString1 = reader.readLine();
        buildApiJson = jsonString1;
      }

    } catch (Exception e) {
      System.out.print("Jenkins get job build result error : ");
      e.printStackTrace();
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return buildApiJson;
  }

  /**
   * Get the jenkins job all status color
   * 
   * @param apiJson jenkins job api json
   * 
   * @return job status color
   */
  public String getJobBuildResult(String apiJson) {
    String buildResult = null;
    JSONObject jsonApiJson = new JSONObject(apiJson);
    String result = jsonApiJson.getString("result");
    buildResult = result;
    return buildResult;
  }

  /**
   * get job build result
   * 
   * @param consoleText build detail
   * @return is build success or not
   */
  public boolean getJobBuildResultByConsoleText(String consoleText, String proType) {
    boolean isSuccess = false;
//    StringBuilder sb = new StringBuilder(consoleText);
//    // Delete commit message
//    sb.delete(sb.indexOf("Commit message"), sb.lastIndexOf(" > git rev-list"));

    if ((proType.equals("OOP") && consoleText.contains("BUILD SUCCESS"))
        || (proType.equals("WEB") && consoleText.contains("===Build success==="))) {
      isSuccess = true;
    }
    return isSuccess;
  }

  /**
   * Check is Not Built
   * 
   * @param num num
   * @return boolean
   */
  public boolean checkIsNotBuilt(int num) {
    boolean isNotBuilt = false;
    if (num == 1) {
      isNotBuilt = true;
    }
    return isNotBuilt;
  }

  /**
   * Check is build success
   * 
   * @param result result
   * @return boolean
   */
  public boolean checkIsBuildSuccess(String result) {
    boolean isBuildSuccess = false;
    if (result.contains("SUCCESS")) {
      isBuildSuccess = true;
    }
    return isBuildSuccess;
  }

  /**
   * Check is checkstyle error
   * 
   * @param consoleText jenkins job console text
   * @return boolean
   */
  public boolean checkIsCheckstyleError(String consoleText, String proType) {
    boolean isCheckstyleError = false;
    if (proType.equals("OOP")) {
      String checkstyleError = "Failed during checkstyle execution";
      isCheckstyleError = consoleText.contains(checkstyleError);
    } else if (proType.equals("WEB")) {
      String webCheckstyleSuccess = "===Checkstyle success===";
      isCheckstyleError = !(consoleText.contains(webCheckstyleSuccess));
    }
    return isCheckstyleError;
  }

  /**
   * Check is Test error
   *
   * @param console Protype
   * @return boolean
   */
  public boolean checkIsTestError(String console, String proType) {
    boolean isTestError = false;
    if (proType.equals("OOP")) {
      isTestError = checkIsJunitError(console);
    } else if (proType.equals("WEB")) {
      isTestError = checkIsWebTestError(console);
    }
    return isTestError;

  }

  /**
   * Check is JUnit error
   *
   * @param consoleText jenkins job console text
   * @return boolean
   */
  public boolean checkIsJunitError(String consoleText) {
    boolean isJunitError = false;
    if (consoleText.contains("T E S T S")) {
      // have run junit test
      if (consoleText.contains("Failed tests")) {
        // junit runs failure.
        isJunitError = true;
      } else {
        // junit runs pass.
        // return false.
      }
    } else {
      // do nothing
      // return false
    }
    return isJunitError;
  }

  /**
   * Check is Web test error
   *
   * @param consoleText jenkins job console text
   * @return boolean
   */
  public boolean checkIsWebTestError(String consoleText) {
    boolean isWebTestError = false;
    if (consoleText.contains("assert")) {
      isWebTestError = true;
    } else {
      // do nothing.
    }
    return isWebTestError;
  }

  /**
   * Get job build commit counts
   * 
   */
  public Integer getJobBuildCommit(String apiJson) {
    int jobCommits = 0;
    JSONObject jsonApiJson = new JSONObject(apiJson);
    JSONArray jsonBuilds = jsonApiJson.getJSONArray("builds");
    jobCommits = jsonBuilds.length();
    return jobCommits;
  }

  private String getConfigType(String fileType) {
    String configType = null;
    if (fileType != null) {
      if (fileType.equals("Maven")) {
        configType = "config_maven.xml";
      } else if (fileType.equals("Javac")) {
        configType = "config_javac.xml";
      } else if (fileType.equals("Web")) {
        configType = "config_web.xml";
      }
    }
    return configType;
  }
}