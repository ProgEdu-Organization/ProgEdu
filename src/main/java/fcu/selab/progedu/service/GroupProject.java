package fcu.selab.progedu.service;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.utils.Linux;

public abstract class GroupProject implements IGroupProject {
  private StatusFactory statusFactory;

  public GroupProject(StatusFactory statusFactory) {
    this.statusFactory = statusFactory;
  }

  @Override
  public abstract String getJenkinsConfig();

  @Override
  public abstract void modifyJenkinsJobConfiguration(String filePath, String updateDbUrl,
      String userName, String proName, String tomcatUrl);

  @Override
  public void createJenkinsJob(Group group) {
    JenkinsApi jenkins = JenkinsApi.getInstance();
    GitlabConfig gitlabConfig = GitlabConfig.getInstance();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
    try {
      String proUrl = gitlabConfig.getGitlabHostUrl() + "/" + group.getGroupName() + "/"
          + group.getGroupName() + ".git";
      String jenkinsCrumb = jenkins.getCrumb(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword());
      String filePath = modifyXml(group.getGroupName(), group.getGroupName(), proUrl);
      JenkinsApi.postCreateJob(group.getGroupName(), group.getGroupName(), proUrl, jenkinsCrumb,
          filePath);
      jenkins.buildJob(group.getGroupName(), group.getGroupName(), jenkinsCrumb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String modifyXml(String userName, String proName, String proUrl) {
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
      modifyJenkinsJobConfiguration(filePath, updateDbUrl, userName, proName, tomcatUrl);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return filePath;
  }

  @Override
  public void createGitlabProject(Group group) {
    Conn conn = Conn.getInstance();
    conn.createGroupProject(group.getGroupName());
    initialize(group.getGroupName());
  }

  private void initialize(String groupName) {
    Linux linuxApi = new Linux();
    ProjectService ps = new ProjectService();
    String tempDir = System.getProperty("java.io.tmpdir");
    String cloneFilePath = tempDir + "/uploads/" + groupName;
    String cloneCommand = "git clone " + ps.getGroupProjectUrl(groupName) + " " + cloneFilePath;
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

    return;
  }

  /**
   * @param statusType status.
   */
  public Status getStatus(String statusType) {
    return statusFactory.getStatus(statusType);
  }

}
