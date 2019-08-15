package fcu.selab.progedu.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.project.AssignmentFactory;
import fcu.selab.progedu.project.AssignmentType;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.utils.Linux;
import fcu.selab.progedu.utils.ZipHandler;

@Path("assignment/")
public class AssignmentService {
  private GitlabService gitlabService = GitlabService.getInstance();
  private GitlabUser root = gitlabService.getRoot();
  private ZipHandler zipHandler;
  private JenkinsApi jenkins = JenkinsApi.getInstance();
  private TomcatService tomcatService = TomcatService.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  private CourseConfig courseConfig = CourseConfig.getInstance();
  private UserService userService = UserService.getInstance();
  private String mailUsername;
  private String mailPassword;
  private String gitlabRootUsername;
  private AssignmentDbManager dbManager = AssignmentDbManager.getInstance();
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String uploadDir = tempDir + "/uploads/";
  private final String testDir = tempDir + "/tests/";

  boolean isSave = true;

  private String testZipChecksum = "";
  private String testZipUrl = "";

  /**
   * Constuctor
   */
  public AssignmentService() {
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
   * 
   * @param assignmentName abc
   * @param readMe         abc
   * @param assignmentType abc
   * @param file           abc
   * @param fileDetail     abc
   * @return abc
   * @throws Exception abc
   */
  @POST
  @Path("create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAssignment(@FormDataParam("assignmentName") String assignmentName,
      @FormDataParam("releaseTime") String releaseTime, @FormDataParam("deadline") String deadline,
      @FormDataParam("readMe") String readMe, @FormDataParam("fileRadio") String assignmentType,
      @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {

    String rootProjectUrl = null;
    String folderName = null;
    String filePath = null;
    boolean hasTemplate = false;

    final AssignmentType assignment = AssignmentFactory.getAssignmentType(assignmentType);
    final ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(assignmentType);
    // 1. Create root project and get project id and url
    createRootProject(assignmentName);
    rootProjectUrl = getRootProjectUrl(assignmentName);

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    String cloneDirectoryPath = gitlabService.cloneProject(gitlabRootUsername, assignmentName);

    // 3. Store Zip File to folder if file is not empty
    folderName = fileDetail.getFileName();
    filePath = tomcatService.storeFileToServer(file, folderName, uploadDir, assignment);

    // 4. Unzip the uploaded file to tests folder and uploads folder on tomcat,
    // extract main method from tests folder, then zip as root project

    /*
     * TO-DO : unzip, create template and test file
     * 
     */

//    try {
//      assignment.unzip(filePath, fileDetail.getFileName(), name, zipHandler);
//      setTestFileInfo();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
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

    // 9. String removeTestDirectoryCommand = "rm -rf tests/" + name;
    tomcatService.removeFile(testDir + assignmentName);

    // 10. import project infomation to database
    addProject(assignmentName, releaseTime, deadline, readMe, projectTypeEnum, hasTemplate,
        testZipChecksum, testZipUrl);

    List<User> users = userService.getUsers();
    for (User user : users) {
      // 11. Create student project, and import project
      GitlabProject project;
      try {
        project = gitlabService.createPrivateProject(user.getGitLabId(), assignmentName,
            rootProjectUrl);
        gitlabService.setGitlabWebhook(project);
      } catch (IOException | LoadConfigFailureException e) {
        e.printStackTrace();
      }

      // 12. Create each Jenkins Jobs
      assignment.createJenkinsJob(user.getUsername(), assignmentName);
    }

    return Response.ok().build();
  }

  private void createRootProject(String name) {
    gitlabService.createRootProject(name);
  }

  public List<String> getAllAssignmentNames() {
    return dbManager.listAllAssignmentNames();
  }

  private String getRootProjectUrl(String name) {
    String url = null;
    String gitlabUrl = null;
    try {
      gitlabUrl = gitlabData.getGitlabRootUrl();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    List<GitlabProject> rootProjects = gitlabService.getProject(root);
    for (GitlabProject project : rootProjects) {
      String proName = project.getName();
      if (proName.equals(name)) {
        url = gitlabUrl + "/root/" + name;
      }
    }
    return url;
  }

  /**
   * 
   * @param groupName group name
   * @return url gitlab project url
   */
  public String getGroupProjectUrl(String groupName, String projectName) {
    String url = null;
    String gitlabUrl = null;
    try {
      gitlabUrl = gitlabData.getGitlabRootUrl();
      url = gitlabUrl + "/" + groupName + "/" + projectName;
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return url;

  }

  /**
   * Send the notification email to student
   *
   */
  public void sendEmail(String email, String name) {
    final String username = mailUsername;
    // final String password = "csclbyqwjhgogypt";// your password
    final String password = mailPassword;

    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.port", "587");
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      String content = "You have a new assignment \"" + name + "\" !";
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
      message.setSubject("New assignment notification", "utf-8");
      message.setContent(content, "text/html;charset=utf-8");

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  // private void createJenkinsJob(String name) {
  // String jenkinsCrumb = jenkins.getCrumb(jenkinsRootUsername,
  // jenkinsRootPassword);
  // StringBuilder sb = zipHandler.getStringBuilder();
  // jenkins.createRootJob(name, jenkinsCrumb, fileType, sb);
  // List<GitlabUser> users = conn.getUsers();
  // Collections.reverse(users);
  // for (GitlabUser user : users) {
  // if (user.getId() == 1) {
  // jenkins.buildJob(user.getUsername(), name, jenkinsCrumb);
  // continue;
  // }
  // jenkins.createJenkinsJob(user.getUsername(), name, jenkinsCrumb, fileType,
  // sb);
  // jenkins.buildJob(user.getUsername(), name, jenkinsCrumb);
  // }
  //
  // }

  /**
   * Add a project to database
   * 
   * @param name        Project name
   * @param deadline    Project deadline
   * @param readMe      Project readme
   * @param projectType File type
   * @param hasTemplate Has template
   */
  public void addProject(String name, String releaseTime, String deadline, String readMe,
      ProjectTypeEnum projectType, boolean hasTemplate, String testZipChecksum, String testZipUrl) {
    Assignment assignment = new Assignment();

    assignment.setName(name);
    assignment.setCreateTime(tomcatService.getCurrentTime());
    assignment.setReleaseTime(releaseTime);
    assignment.setDeadline(deadline);
    assignment.setDescription(readMe);
    assignment.setType(projectType);
    assignment.setHasTemplate(hasTemplate);
    assignment.setTestZipChecksum(testZipChecksum);
    assignment.setTestZipUrl(testZipUrl);

    dbManager.addAssignment(assignment);
  }

  /**
   * delete projects
   * 
   * @param name project name
   * @return response
   */
  @POST
  @Path("delete")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteProject(@FormDataParam("del_Hw_Name") String name) {
    Linux linuxApi = new Linux();
    CommitRecordService commitRecordService = new CommitRecordService();
    // delete tomcat test file
    String removeTestDirectoryCommand = "rm -rf tests/" + name;
    linuxApi.execLinuxCommandInFile(removeTestDirectoryCommand, tempDir);

    String removeZipTestFileCommand = "rm tests/" + name + ".zip";
    linuxApi.execLinuxCommandInFile(removeZipTestFileCommand, tempDir);

    String removeFileCommand = "rm -rf tests/" + name + "-COMPLETE";
    linuxApi.execLinuxCommandInFile(removeFileCommand, tempDir);
    // delete db
    dbManager.deleteAssignment(name);
    commitRecordService.deleteRecord(name);

    // delete gitlab
    gitlabService.deleteProjects(name);
    String jenkinsUserName = "";
    String jenkinsPass = "";
    try {
      jenkinsUserName = JenkinsConfig.getInstance().getJenkinsRootUsername();
      jenkinsPass = JenkinsConfig.getInstance().getJenkinsRootPassword();
    } catch (LoadConfigFailureException e) {
      isSave = false;
      e.printStackTrace();
    }
    String crumb = jenkins.getCrumb(jenkinsUserName, jenkinsPass);

    List<User> users = userService.getUsers();
    // delete Jenkins
    for (User user : users) {
      String jobName = user.getUsername() + "_" + name;
      jenkins.deleteJob(jobName, crumb);
    }

    Response response = Response.ok().build();
    if (!isSave) {
      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return response;
  }

  /**
   * edit projects
   * 
   * @param assignmentName project name
   * @return response
   */
  @POST
  @Path("edit")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response editProject(@FormDataParam("assignmentName") String assignmentName,
      @FormDataParam("releaseTime") String releaseTime, @FormDataParam("deadline") String deadline,
      @FormDataParam("readMe") String readMe,
      @FormDataParam("testCase") InputStream uploadedInputStream,
      @FormDataParam("testCase") FormDataContentDisposition fileDetail) {

//    dbManager.editAssignment(deadline, readMe, releaseTime, assignmentName);
//
//    if (!fileDetail.getFileName().isEmpty()) {
//      // update test case
//      String filePath = storeFileToTestsFolder(assignmentName + ".zip", uploadedInputStream);
//      // update database checksum
//
//      String checksum = getChecksum(filePath);
////      System.out.println("checksum : " + checksum);
//
//      dbManager.updateAssignmentChecksum(assignmentName, checksum);
//    }

//    Response response = Response.ok().build();
//    if (!isSave) {
//      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
//    }

    return Response.ok().build();
  }

  /**
   * get project checksum
   * 
   * @param projectName project name
   * @return checksum
   */
  @GET
  @Path("checksum")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProject(@QueryParam("proName") String projectName) {
    Assignment assignment = dbManager.getAssignmentByName(projectName);
    return Response.ok().entity(assignment).build();
  }

  public void setTestFileInfo() {
    testZipChecksum = String.valueOf(zipHandler.getChecksum());
    testZipUrl = zipHandler.getUrlForJenkinsDownloadTestFile();
  }

  /**
   * get course name
   * 
   * @return course name
   */
  public String getCourseName() {
    String name = "";
    try {
      name = courseConfig.getCourseName();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return name;
  }

//  /**
//   * Edit test case upload test case to test folder
//   * 
//   * @param fileName            file name
//   * @param uploadedInputStream file
//   */
//  private String storeFileToTestsFolder(String fileName, InputStream uploadedInputStream) {
//    try {
//      createFolderIfNotExists(testDir);
//    } catch (SecurityException se) {
//      System.out.println(se.toString());
//    }
//    String uploadedFileLocation = testDir + fileName;
//    File uploadedFile = new File(uploadedFileLocation);
//    if (uploadedFile.exists()) {
//      uploadedFile.delete();
//    }
//    try {
//      saveToFile(uploadedInputStream, uploadedFileLocation);
//    } catch (IOException e) {
//      System.out.println(e.toString());
//    }
//    return uploadedFileLocation;
//  }

  private String getChecksum(String zipFilePath) {
    String strChecksum = "";

    try (CheckedInputStream cis = new CheckedInputStream(new FileInputStream(zipFilePath),
        new CRC32());) {
      byte[] buf = new byte[1024];
      // noinspection StatementWithEmptyBody
      while (cis.read(buf) >= 0) {
      }
      System.out.println(cis.getChecksum().getValue());
      strChecksum = String.valueOf(cis.getChecksum().getValue());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return strChecksum;
  }

}
