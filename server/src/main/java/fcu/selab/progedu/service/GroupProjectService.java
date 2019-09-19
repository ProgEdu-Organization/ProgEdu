package fcu.selab.progedu.service;

import java.io.IOException;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.project.GroupProjectFactory;
import fcu.selab.progedu.project.GroupProjectType;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.utils.ZipHandler;

public class GroupProjectService {
  private static GroupProjectService instance = new GroupProjectService();
  private GitlabService gitlabService = GitlabService.getInstance();
  private GitlabUser root = gitlabService.getRoot();
  private ZipHandler zipHandler;
  private JenkinsService jenkins = JenkinsService.getInstance();
  private TomcatService tomcatService = TomcatService.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  private CourseConfig courseConfig = CourseConfig.getInstance();
  private UserService userService = UserService.getInstance();
  private String mailUsername;
  private String mailPassword;
  private String gitlabRootUsername;
//  private GroupProjectDbManager dbManager = GroupProjectDbManager.getInstance();
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String uploadDir = tempDir + "/uploads/";
  private final String testDir = tempDir + "/tests/";

  boolean isSave = true;

  public static GroupProjectService getInstance() {
    return instance;
  }

  /**
   * Constuctor
   */
  public GroupProjectService() {
    try {
      zipHandler = new ZipHandler();
      mailUsername = jenkinsData.getMailUser();
      mailPassword = jenkinsData.getMailPassword();
      gitlabRootUsername = gitlabData.getGitlabRootUsername();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  /**
   * (to do)
   * 
   * @param group       (to do)
   * @param deadline    (to do)
   * @param readMe      (to do)
   * @param projectType (to do)
   * @param file        (to do)
   * @param fileDetail  (to do)
   */
  public void createGroupProject(String groupName, String projectName, String leader,
      String projectType) {
    String readMe = "Initialization";
//    final GitlabService gitlabService = GitlabService.getInstance();
    final GroupProjectType groupProject = GroupProjectFactory.getGroupProjectType(projectType);
    final ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(projectType);
    // 1. Create root project and get project id and url
    GitlabProject project = null;
    try {
      project = gitlabService.createGroupProject(groupName, projectName, leader);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    String cloneDirectoryPath = gitlabService.cloneProject(groupName, projectName);
    // 3. if README is not null
    tomcatService.createReadmeFile(readMe, cloneDirectoryPath);

    // 4. git push
    gitlabService.pushProject(cloneDirectoryPath);

    // 5. remove project file in linux
    tomcatService.removeFile(uploadDir);

    // 6. import project infomation to database
    addProject(groupName, projectName, readMe, projectTypeEnum);

    // 7. set Gitlab webhook
    try {
      gitlabService.setGitlabWebhook(project);
    } catch (IOException | LoadConfigFailureException e) {
      e.printStackTrace();
    }

    // 8. Create each Jenkins Jobs
    groupProject.createJenkinsJob(groupName, projectName);
  }

  /**
   * Add a project to database
   * 
   * @param name        Project name
   * @param deadline    Project deadline
   * @param readMe      Project readme
   * @param projectType File type
   */
  public void addProject(String groupName, String projectName, String readMe,
      ProjectTypeEnum projectType) {
    GroupProject groupProject = new GroupProject();
    groupProject.setName(projectName);
    groupProject.setCreateTime(tomcatService.getCurrentTime());
    groupProject.setDeadline(tomcatService.getCurrentTime());
    groupProject.setDescription(readMe);
    groupProject.setType(projectType);
    ProjectDbManager projectDb = ProjectDbManager.getInstance();
    projectDb.addProject(groupProject);
  }

}
