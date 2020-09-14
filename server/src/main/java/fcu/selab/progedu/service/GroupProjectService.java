package fcu.selab.progedu.service;

import java.io.File;
import java.io.IOException;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.project.GroupProjectFactory;
import fcu.selab.progedu.project.GroupProjectType;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.utils.ZipHandler;
import fcu.selab.progedu.utils.ExceptionUtil;

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
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupProjectService.class);

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
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * 
   * @param groupName   group name
   * @param projectName project name
   * @param projectType projectType
   */
  public void createGroupProject(String groupName, String projectName, String projectType) {
    String readMe = "Initialization";
//    final GitlabService gitlabService = GitlabService.getInstance();
    final GroupProjectType groupProject = GroupProjectFactory.getGroupProjectType(projectType);
    final ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(projectType);
    // 1. Create root project and get project id and url
    int projectId = 0;
    try {
      projectId = gitlabService.createGroupProject(groupName, projectName);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    String cloneDirectoryPath = gitlabService.cloneProject(groupName, projectName);
    // 3. if README is not null
    tomcatService.createReadmeFile(readMe, cloneDirectoryPath);

    // 4 create template
    String filePath = tomcatService.storeFileToServer(null, null, groupProject);
    zipHandler.unzipFile(filePath, cloneDirectoryPath);

    // 5. Add .gitkeep if folder is empty.
    tomcatService.findEmptyFolder(cloneDirectoryPath);
    // 6. git push
    gitlabService.pushProject(cloneDirectoryPath);

    // 7. remove project file in linux
    tomcatService.deleteDirectory(new File(uploadDir));

    // 8. import project infomation to database
    addProject(groupName, projectName, readMe, projectTypeEnum);

    // 9. set Gitlab webhook
    try {
      GitlabProject project = gitlabService.getProject(projectId);
      gitlabService.setGitlabWebhook(project);
    } catch (IOException | LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    // 10. Create each Jenkins Jobs
    groupProject.createJenkinsJob(groupName, projectName);
  }

  /**
   * Add a project to database
   * 
   * @param groupName   group name
   * @param projectName project name
   * @param readMe      readMe
   * @param projectType projectType
   */
  public void addProject(String groupName, String projectName, String readMe,
      ProjectTypeEnum projectType) {
    GroupProject groupProject = new GroupProject();
    groupProject.setName(projectName);
    groupProject.setCreateTime(tomcatService.getCurrentTime());
    groupProject.setDeadline(tomcatService.getCurrentTime());
    groupProject.setDescription(readMe);
    groupProject.setType(projectType);
//    ProjectDbManager projectDb = ProjectDbManager.getInstance();
//    projectDb.addProject(groupProject);

    ProjectDbService gpdb = ProjectDbService.getInstance();
    gpdb.addProject(groupProject, groupName);
  }

}
