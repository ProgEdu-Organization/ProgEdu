package fcu.selab.progedu.conn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.gitlab.api.models.GitlabProject;
import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.jenkins.JobStatus;
import fcu.selab.progedu.service.CommitResultService;
import fcu.selab.progedu.status.StatusEnum;

public class StudentDashChoosePro {
  private static final String JOB = "/job/";
  private static final String JSON = "/api/json";
  JenkinsConfig jenkinsData;
  JenkinsApi jenkins;
  JobStatus jobStatus = new JobStatus();
  CommitResultService commitResultService = new CommitResultService();
  GitlabConfig gitlabConfig = GitlabConfig.getInstance();

  public StudentDashChoosePro() {
    jenkinsData = JenkinsConfig.getInstance();
    jenkins = new JenkinsApi();
  }

  /**
   * Get the choosed project
   * 
   * @param stuProjects all student projects
   * @param projectId   the choosed project id
   * @return gitlab project
   */
  public GitlabProject getChoosedProject(List<GitlabProject> stuProjects, int projectId) {
    GitlabProject project = new GitlabProject();
    for (GitlabProject stuProject : stuProjects) {
      if (stuProject.getId() == projectId) {
        project = stuProject;
      }
    }
    return project;
  }

  /**
   * Get the choosed project url
   * 
   * @param project the choosed project
   * @return url
   */
  public String getChoosedProjectUrl(GitlabProject project) {
    String url = null;
    url = project.getHttpUrl();
    try {
      url = url.replace(gitlabConfig.getGitlabContainerId(), gitlabConfig.getGitlabHostUrl());
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return url;
  }

  /**
   * Get the job build numbers
   * 
   * @param username    user name
   * @param projectName project name
   * @return list of build numbers
   */
  public List<Integer> getBuildNumbers(String username, String projectName) {
    String jobName = username + "_" + projectName;
    String jobUrl = null;
    List<Integer> buildNumbers = null;
    try {
      jobUrl = jenkinsData.getJenkinsHostUrl() + JOB + jobName + JSON;
      buildNumbers = jenkins.getJenkinsJobAllBuildNumber(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword(), jobUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return buildNumbers;
  }

  /**
   * Get the jenkins last build color
   * 
   * @param username    user name
   * @param projectName project name
   * @return color
   */
  public String getLastColor(String username, String projectName) {
    String color = null;
    color = commitResultService.getCommitResult(username, projectName);
    return color;
  }

  private String checkStatus(String result, String userName, String projectName, int num,
      String proType) {
    String status;

    if (jenkins.checkIsInitialization(num)) {
      // is Initialization;
      status = StatusEnum.INITIALIZATION.getTypeName();
    } else {
      String console = jenkins.getConsoleText(userName, projectName, num);
      if (jenkins.checkIsBuildSuccess(result)) {
        // is Initialization
        status = StatusEnum.BUILD_SUCCESS.getTypeName();
      } else if (jenkins.checkIsTestError(console, proType)) {
        // is test failure
        status = StatusEnum.UNIT_TEST_FAILURE.getTypeName();
      } else if (jenkins.checkIsCheckstyleError(console, proType)) {
        // is checkstyle failure = true
        status = StatusEnum.CHECKSTYLE_FAILURE.getTypeName();
      } else {
        // is compile failure
        status = StatusEnum.COMPILE_FAILURE.getTypeName();
      }
    }
    return status;

  }

  /**
   * count for SCM build
   * 
   * @param username    student name
   * @param projectName project name
   * @return count
   */
  public List<Integer> getScmBuildCounts(String username, String projectName) {
    String jobName = username + "_" + projectName;
    jobStatus.setName(jobName);
    String jobUrl = "";
    List<Integer> numbers = new ArrayList<>();
    List<Integer> counts = new ArrayList<>();
    String jenkinsHostUrl = "";
    try {
      jenkinsHostUrl = jenkinsData.getJenkinsHostUrl();
      jobUrl = jenkinsHostUrl + JOB + jobName + JSON;
      numbers = jenkins.getJenkinsJobAllBuildNumber(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword(), jobUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    for (int i : numbers) {
      jobStatus.setUrl(jenkinsHostUrl + JOB + jobName + "/" + i + JSON);
      // Get job status
      jobStatus.setJobApiJson();
      String apiJson = jobStatus.getJobApiJson();
      JSONObject json = new JSONObject(apiJson);
      JSONArray actions = json.getJSONArray("actions");
      JSONArray causes = actions.getJSONObject(0).getJSONArray("causes");
      String shortDescription = causes.getJSONObject(0).optString("shortDescription");
      if (shortDescription.contains("GitLab push")) {
        counts.add(i);
      } else {
        if (i == 1) { // teacher commit
          counts.add(i);
        }
      }
    }
    return counts;
  }

  /**
   * Get the last build number
   * 
   * @param username    user name
   * @param projectName project name
   * @return number
   */
  public String getLastBuildNum(String username, String projectName) {
    String num = null;
    List<Integer> buildNumbers = getBuildNumbers(username, projectName);
    num = String.valueOf(buildNumbers.get(buildNumbers.size() - 1));
    return num;
  }

  /**
   * Get commit color
   * 
   * @param num         commit number
   * @param userName    username
   * @param projectName project name
   * @param apiJson     apiJson
   * @param proType     proType
   * @return color
   */
  public String getCommitStatus(int num, String userName, String projectName, String apiJson,
      String proType) {
    String result = jenkins.getJobBuildResult(apiJson);
    return checkStatus(result, userName, projectName, num, proType);

  }

  /**
   * get
   * 
   * @param num         buuild num
   * @param userName    student name
   * @param projectName project name
   * @return commit message
   */
  public String getCommitMessage(int num, String userName, String projectName) {
    String console = jenkins.getCompleteConsoleText(userName, projectName, num);
    String modifiedCommit = jenkins.getConsoleTextCommitMessage(console);
    // Removing "<" and ">" to prevent js function.
    modifiedCommit = modifiedCommit.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    return modifiedCommit;
  }

  /**
   * get job build time
   * 
   * @param apiJson build api json
   * @return date
   */
  public String getCommitTime(String apiJson) {
    JSONObject json = new JSONObject(apiJson);
    long timestamp = json.getLong("timestamp");
    Date date = new Date(timestamp);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
    return sdf.format(date);
  }

  /**
   * get job build api json
   * 
   * @param num         build num
   * @param username    student name
   * @param projectName project name
   * @return json string
   */
  public String getBuildApiJson(int num, String username, String projectName) {
    String jobName = username + "_" + projectName;
    String buildUrl;
    String buildApiJson = "";
    try {
      buildUrl = jenkinsData.getJenkinsHostUrl() + JOB + jobName + "/" + num + JSON;
      buildApiJson = jenkins.getJobBuildApiJson(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword(), buildUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return buildApiJson;
  }

  /**
   * Get commit date
   * 
   * @param date commit date
   * @return string date
   */
  public String getCommitDate(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    return sdf.format(date);
  }
}
