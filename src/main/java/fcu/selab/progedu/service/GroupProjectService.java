package fcu.selab.progedu.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.Group;
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
  public void createGroupProject(Group group, Date deadline, String readMe, String projectType,
      InputStream file, FormDataContentDisposition fileDetail) {

    String folderName = null;
    String filePath = null;

    final GitlabService gitlabService = GitlabService.getInstance();
    final GroupProjectType groupProject = GroupProjectFactory.getGroupProjectType(projectType);
    final ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(projectType);
    // 1. Create root project and get project id and url
    GitlabProject project = null;
    try {
      project = gitlabService.createGroupProject(group);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    String cloneDirectoryPath = gitlabService.cloneProject(group.getGroupName(),
        group.getProjectName());

    // 3. Store Zip File to folder if file is not empty
    filePath = tomcatService.storeFileToServer(file, fileDetail, uploadDir, groupProject);

    // 4. Unzip the uploaded file to uploads folder on tomcat

    /*
     * TO-DO : unzip
     * 
     */
    zipHandler.unzipFile(cloneDirectoryPath, filePath);

    // 5. Add .gitkeep if folder is empty.
    tomcatService.findEmptyFolder(cloneDirectoryPath);

    // 6. if README is not null
    if (!readMe.equals("<br>") || !"".equals(readMe) || !readMe.isEmpty()) {
      // Add readme to folder
      tomcatService.createReadmeFile(readMe, cloneDirectoryPath);
    }

    // 7. git push
    gitlabService.pushProject(cloneDirectoryPath);

    // 8. remove project file in linux
    tomcatService.removeFile(uploadDir);

    // 9. import project infomation to database
    addProject(group.getProjectName(), readMe, deadline, projectTypeEnum);

    // 10. set Gitlab webhook
    try {

      gitlabService.setGitlabWebhook(project);
    } catch (IOException | LoadConfigFailureException e) {
      e.printStackTrace();
    }

    // 11. Create each Jenkins Jobs
    groupProject.createJenkinsJob(group.getGroupName(), group.getProjectName());

  }
//
//  /**
//   * 
//   * @param groupName group name
//   * @return url gitlab project url
//   */
//  public String getGroupProjectUrl(String groupName, String projectName) {
//    String url = null;
//    String gitlabUrl = null;
//    try {
//      gitlabUrl = gitlabData.getGitlabRootUrl();
//      url = gitlabUrl + "/" + groupName + "/" + projectName;
//    } catch (LoadConfigFailureException e) {
//      e.printStackTrace();
//    }
//
//    return url;
//
//  }

  /**
   * Add a project to database
   * 
   * @param name        Project name
   * @param deadline    Project deadline
   * @param readMe      Project readme
   * @param projectType File type
   */

  public void addProject(String name, String readMe, Date deadline, ProjectTypeEnum projectType) {

    GroupProject groupProject = new GroupProject();
    groupProject.setName(name);
    groupProject.setDeadline(deadline);
    groupProject.setCreateTime(tomcatService.getCurrentTime());
    groupProject.setDescription(readMe);
    groupProject.setType(projectType);
    ProjectDbManager projectDb = ProjectDbManager.getInstance();
    projectDb.addProject(groupProject);
  }

}
