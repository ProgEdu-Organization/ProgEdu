package fcu.selab.progedu.conn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.data.Project;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.jenkins.JobStatus;

public class StudentDash {
  private static final String JOB = "/job/";
  private static final String JSON = "/api/json";
  String privateToken = null;
  StudentConn stuConn;
  GitlabUser user;
  ProjectDbManager pdb = ProjectDbManager.getInstance();
  List<Project> dbProjects = pdb.listAllProjects();
  List<GitlabProject> gitProjects;
  JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  JenkinsApi jenkins = JenkinsApi.getInstance();
  JobStatus jobStatus = new JobStatus();

  /**
   * Constructor
   * 
   * @param privateToken student private token
   */
  public StudentDash(String privateToken) {
    this.privateToken = privateToken;
    stuConn = new StudentConn(privateToken);
    user = stuConn.getUser();
    gitProjects = stuConn.getProject();
  }

  /**
   * studentDashboard.jsp Get student project
   * 
   * @return List project
   */
  public List<GitlabProject> getStuProject() {
    List<GitlabProject> projects = new ArrayList<>();

    for (Project dbProject : dbProjects) {
      for (GitlabProject project : gitProjects) {
        if (dbProject.getName().equals(project.getName())) {
          projects.add(project);
        }
      }
    }
    return projects;
  }

  /**
   * Get job color
   * 
   * @param projects list of project
   * @return list of color
   */
  public List<String> getMainTableJobColor(List<GitlabProject> projects) {
    List<String> colors = new ArrayList<>();
    for (GitlabProject project : projects) {
      JobStatus jobStatus = new JobStatus();
      String jobName = user.getUsername() + "_" + project.getName();
      jobStatus.setName(jobName);
      String jobUrl = null;
      try {
        jobUrl = jenkinsData.getJenkinsHostUrl() + JOB + jobName + JSON;
      } catch (LoadConfigFailureException e) {
        e.printStackTrace();
      }
      jobStatus.setUrl(jobUrl);
      jobStatus.setJobApiJson();
      if (null != jobStatus.getJobApiJson() && !"".equals(jobStatus.getJobApiJson())) {
        int checkstyleErrorAmount = 0;
        String jobApiJson = jobStatus.getJobApiJson();
        boolean isMaven = jenkins.checkProjectIsMvn(jobApiJson);
        String color = jenkins.getJobJsonColor(jobApiJson);
        JSONObject checkstyleDes = jenkins.getCheckstyleDes(jobApiJson);
        if (null != checkstyleDes && !"".equals(checkstyleDes)) {
          checkstyleErrorAmount = jenkins.getCheckstyleErrorAmount(checkstyleDes);
        }
        if (checkstyleErrorAmount != 0) {
          color = "orange";
        }
        colors.add(color);
      }
    }
    return colors;
  }

  /**
   * Get job commit counts
   * 
   * @param projects list of project
   * @return list of commit counts
   */
  public List<String> getMainTableJobCommitCount(List<GitlabProject> projects) {
    List<String> commitCounts = new ArrayList<>();
    for (GitlabProject project : projects) {
      try {
        int commitCount = stuConn.getAllCommitsCounts(project.getId());
        commitCounts.add(String.valueOf(commitCount));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return commitCounts;
  }

  /**
   * get jenkins job status
   * 
   * @param jobName job's name
   * @param jobUrl  job's jenkins url
   * @return status
   */
  public JobStatus getJobStatus(String jobName, String jobUrl) {
    JobStatus jobStatus = new JobStatus();
    jobStatus.setName(jobName);
    jobStatus.setUrl(jobUrl);
    jobStatus.setJobApiJson();
    return jobStatus;
  }

  /**
   * Get user job status list
   * 
   * @param projects gitlab project list
   * @param user     user
   * @return job status list
   */
  public List<JobStatus> getJobStatusList(List<GitlabProject> projects, GitlabUser user) {
    List<JobStatus> jobStatusList = new ArrayList<>();
    for (GitlabProject project : projects) {
      String jobName = user.getUsername() + "_" + project.getName();
      String jobUrl = null;
      try {
        jobUrl = jenkinsData.getJenkinsHostUrl() + JOB + jobName + JSON;
      } catch (LoadConfigFailureException e) {
        e.printStackTrace();
      }
      JobStatus jobStatus = getJobStatus(jobName, jobUrl);
      jobStatusList.add(jobStatus);
    }
    return jobStatusList;
  }

  /**
   * Get job commit counts
   * 
   * @param jobStatusis job status
   * @return commit counts list
   */
  public List<Integer> getJobCommits(List<JobStatus> jobStatusis) {
    List<Integer> commitCounts = new ArrayList<>();
    for (JobStatus jobStatus : jobStatusis) {
      int count = jenkins.getJobBuildCommit(jobStatus.getJobApiJson());
      commitCounts.add(count);
    }
    return commitCounts;
  }

  /**
   * get student project scm commit count
   * 
   * @param userName   student name
   * @param gitProject gitlab project
   * @return count
   */
  public int getScmCommit(String userName, GitlabProject gitProject) {
    String jobName = userName + "_" + gitProject.getName();
    jobStatus.setName(jobName);
    String jobUrl = "";
    List<Integer> numbers = new ArrayList<>();
    String jenkinsHostUrl = "";
    try {
      jenkinsHostUrl = jenkinsData.getJenkinsHostUrl();
      jobUrl = jenkinsHostUrl + JOB + jobName + JSON;
      numbers = jenkins.getJenkinsJobAllBuildNumber(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword(), jobUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    int commitCount = 0;
    for (int i : numbers) {
      jobStatus.setUrl(jenkinsHostUrl + JOB + jobName + "/" + i + JSON);
      // Get job status
      jobStatus.setJobApiJson();
      String apiJson = jobStatus.getJobApiJson();
      JSONObject json = new JSONObject(apiJson);
      JSONArray actions = json.getJSONArray("actions");
      JSONArray causes = actions.getJSONObject(0).getJSONArray("causes");
      String shortDescription = causes.getJSONObject(0).optString("shortDescription");
      if ("Started by GitLab push".equals(shortDescription)) {
        commitCount++;
      } else {
        if (i == 1) { // teacher commit
          commitCount++;
        }
      }
    }
    return commitCount;
  }
}
