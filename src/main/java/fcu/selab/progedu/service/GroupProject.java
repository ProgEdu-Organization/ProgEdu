package fcu.selab.progedu.service;

import java.io.IOException;

import org.gitlab.api.models.GitlabProject;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.HttpConnect;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.status.StatusFactorySelector;
import fcu.selab.progedu.utils.Linux;

public abstract class GroupProject implements IGroupProject {

  public GroupProject() {
  }

  public abstract ProjectTypeEnum getProjectType();

  /**
   * get jenkins config template
   */
  @Override
  public abstract String getJenkinsConfig();

  /**
   * modify Jenkins job configuration base on sample file
   * 
   * @param filePath    filePath
   * @param updateDbUrl updateDbUrl
   * @param userName    userName
   * @param proName     proName
   * @param tomcatUrl   tomcatUrl
   */
  @Override
  public abstract void createJenkinsJobConfiguration(String filePath, String updateDbUrl,
      String userName, String proName, String tomcatUrl);

  /**
   * create Jenkins job
   * 
   * @param projectName group
   */
  @Override
  public void createJenkinsJob(String groupName, String projectName) {
    JenkinsApi jenkins = JenkinsApi.getInstance();
    GitlabConfig gitlabConfig = GitlabConfig.getInstance();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
    try {
      String proUrl = gitlabConfig.getGitlabHostUrl() + "/" + groupName + "/" + projectName
          + ".git";
      String jenkinsCrumb = jenkins.getCrumb(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword());
      String filePath = modifyJenkinsJobConfiguration(groupName, projectName, proUrl);
      JenkinsApi.postCreateJob(groupName, projectName, proUrl, jenkinsCrumb, filePath);
      jenkins.buildJob(groupName, projectName, jenkinsCrumb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String modifyJenkinsJobConfiguration(String userName, String proName, String proUrl) {
    String filePath = null;
    String configType = getJenkinsConfig();
    filePath = this.getClass().getResource("/jenkins/" + configType).getPath();

    try {
      String tomcatUrl;
      CourseConfig courseData = CourseConfig.getInstance();
      tomcatUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/project/checksum?proName="
          + proName;
      String updateDbUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/commits/update";
      // proUrl project name toLowerCase
      proUrl = proUrl.toLowerCase();
      JenkinsApi.modifyXmlFileUrl(filePath, proUrl);
      createJenkinsJobConfiguration(filePath, updateDbUrl, userName, proName, tomcatUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return filePath;
  }

  @Override
  public void createGitlabProject(Group group, String projectName)
      throws IOException, LoadConfigFailureException {
    GitlabService conn = GitlabService.getInstance();
    GitlabProject gitlabProject = conn.createGroupProject(group, projectName);
    initialize(group.getGroupName(), gitlabProject);
  }

  private void initialize(String groupName, GitlabProject gitlabProject)
      throws IOException, LoadConfigFailureException {
    Linux linuxApi = new Linux();
    AssignmentService ps = new AssignmentService();
    String tempDir = System.getProperty("java.io.tmpdir");
    String cloneFilePath = tempDir + "/uploads/" + groupName;
    String cloneCommand = "git clone " + ps.getGroupProjectUrl(groupName, gitlabProject.getName())
        + " " + cloneFilePath;
    linuxApi.execLinuxCommand(cloneCommand);
    ps.createReadmeFile("", groupName);

    // 1. Cmd gitlab add
    String addCommand = "git add .";
    linuxApi.execLinuxCommandInFile(addCommand, cloneFilePath);

    // 2. Cmd gitlab commit
    String commitCommand = "git commit -m \"Instructor&nbsp;Commit\"";
    linuxApi.execLinuxCommandInFile(commitCommand, cloneFilePath);

    // 3. Cmd gitlab push
    String pushCommand = "git push";
    linuxApi.execLinuxCommandInFile(pushCommand, cloneFilePath);

    HttpConnect.getInstance().setGitlabWebhook(groupName, gitlabProject);
  }

  /**
   * @param statusType status.
   */
  public Status getStatus(String statusType) {
    return getStatusFactory().getStatus(statusType);
  }

  private StatusFactory getStatusFactory() {
    return StatusFactorySelector.getStatusFactory(getProjectType());
  }

}
