package fcu.selab.progedu.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.conn.TomcatService;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
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
  private JenkinsService jenkins = JenkinsService.getInstance();
  private TomcatService tomcatService = TomcatService.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  private CourseConfig courseConfig = CourseConfig.getInstance();
  private UserService userService = UserService.getInstance();
  private String mailUsername;
  private String mailPassword;
  private String gitlabRootUsername;
  private AssignmentDbManager dbManager = AssignmentDbManager.getInstance();
  private AssignmentUserDbManager auDbManager = AssignmentUserDbManager.getInstance();
  private UserDbManager userDbManager = UserDbManager.getInstance();
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String uploadDir = tempDir + "/uploads/";
  private final String testDir = tempDir + "/tests/";

  boolean isSave = true;

  private long testZipChecksum = 0;
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
      @FormDataParam("releaseTime") Date releaseTime, @FormDataParam("deadline") Date deadline,
      @FormDataParam("readMe") String readMe, @FormDataParam("fileRadio") String assignmentType,
      @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    System.out.println(assignmentName);
    System.out.println(releaseTime);
    System.out.println(deadline);
    System.out.println(readMe);
    System.out.println(assignmentType);
    System.out.println(file.toString());
    System.out.println(fileDetail.getFileName());
    String rootProjectUrl = null;

    final AssignmentType assignment = AssignmentFactory.getAssignmentType(assignmentType);
    final ProjectTypeEnum projectTypeEnum = ProjectTypeEnum.getProjectTypeEnum(assignmentType);
    // 1. Create root project and get project id and url
    createRootProject(assignmentName);
    rootProjectUrl = getRootProjectUrl(assignmentName);

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    final String cloneDirectoryPath = gitlabService.cloneProject(gitlabRootUsername,
        assignmentName);
//
//    // 3. Store Zip File to folder if file is not empty
    String filePath = tomcatService.storeFileToServer(file, fileDetail, uploadDir, assignment);

    // 4. Unzip the uploaded file to tests folder and uploads folder on tomcat,
    // extract main method from tests folder, then zip as root project
    String testDirectory = testDir + assignmentName;

    zipHandler.unzipFile(filePath, cloneDirectoryPath);
    zipHandler.unzipFile(filePath, testDirectory);
    assignment.createTemplate(cloneDirectoryPath);
    assignment.createTestCase(testDirectory);
    zipHandler.zipTestFolder(testDirectory);
    testZipChecksum = zipHandler.getChecksum();
    testZipUrl = zipHandler.serverIp + "/ProgEdu/webapi/assignment/getTestFile?filePath="
        + testDirectory + ".zip";
    // zipHandler.setUrlForJenkinsDownloadTestFile(zipHandler.serverIp
    // + "/ProgEdu/webapi/jenkins/getTestFile?filePath=" + testDirectory + ".zip");

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
    boolean hasTemplate = false;

    addProject(assignmentName, releaseTime, deadline, readMe, projectTypeEnum, hasTemplate,
        testZipChecksum, testZipUrl);

    List<User> users = userService.getStudents();
    for (User user : users) {
      // 11. Create student project, and import project
      try {
        GitlabProject project = gitlabService.createPrivateProject(user.getGitLabId(),
            assignmentName, rootProjectUrl);
        gitlabService.setGitlabWebhook(project);
      } catch (IOException | LoadConfigFailureException e) {
        e.printStackTrace();
      }
      addAuid(user.getUsername(), assignmentName);
      // 12. Create each Jenkins Jobs
      assignment.createJenkinsJob(user.getUsername(), assignmentName);
    }

    return Response.ok().build();
  }

  private void createRootProject(String name) {
    gitlabService.createRootProject(name);
  }

  public List<String> getAllAssignmentNames() {
    return dbManager.getAllAssignmentNames();
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

  /**
   * Add a project to database
   * 
   * @param name        Project name
   * @param deadline    Project deadline
   * @param readMe      Project readme
   * @param projectType File type
   * @param hasTemplate Has template
   */
  public void addProject(String name, Date releaseTime, Date deadline, String readMe,
      ProjectTypeEnum projectType, boolean hasTemplate, long testZipChecksum, String testZipUrl) {
    Assignment assignment = new Assignment();
    Date date = tomcatService.getCurrentTime();
    assignment.setName(name);
    assignment.setCreateTime(date);
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
   * Add auid to database
   * 
   * @param username       username
   * @param assignmentName assignment name
   */
  public void addAuid(String username, String assignmentName) {
    int aid = dbManager.getAssignmentIdByName(assignmentName);
    int uid = userDbManager.getUserIdByUsername(username);

    auDbManager.addAssignmentUser(aid, uid);
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
    System.out.println("delete: " + name);
    Linux linuxApi = new Linux();
    // delete tomcat test file
    String removeZipTestFileCommand = "rm tests/" + name + ".zip";
    linuxApi.execLinuxCommandInFile(removeZipTestFileCommand, tempDir);

    // delete db
    CommitRecordService commitRecordService = new CommitRecordService();
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

    List<User> users = userService.getStudents();
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
      @FormDataParam("releaseTime") Date releaseTime, @FormDataParam("deadline") Date deadline,
      @FormDataParam("readMe") String readMe, @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    
    int id = dbManager.getAssignmentIdByName(assignmentName);
    if (file == null) {
      dbManager.editAssignment(deadline, releaseTime, readMe, id);
    } else {
      ProjectTypeEnum assignmentType = dbManager.getAssignmentType(assignmentName);
      final AssignmentType assignment = AssignmentFactory
          .getAssignmentType(assignmentType.getTypeName());

      String tempFilePath = uploadDir + assignmentName;
      String testCasePath = testDir + assignmentName;
      String testCaseZipPath = testCasePath + ".zip";
      // remove current test case
      tomcatService.removeFile(testCaseZipPath);
      tomcatService.storeFileToUploadsFolder(file, tempFilePath);

      zipHandler.unzipFile(tempFilePath, testCasePath);
      assignment.createTestCase(testCasePath);
      zipHandler.zipTestFolder(testCasePath);
      long checksum = zipHandler.getChecksum();
      tomcatService.removeFile(uploadDir);
      tomcatService.removeFile(testCasePath);

      dbManager.editAssignment(deadline, releaseTime, readMe, checksum, id);
    }
    return Response.ok().build();
  }

  /**
   * get project checksum
   * 
   * @param assignmentName assignment name
   * @return checksum
   */
  @GET
  @Path("checksum")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProject(@QueryParam("proName") String assignmentName) {
    Assignment assignment = dbManager.getAssignmentByName(assignmentName);
    JSONObject ob = new JSONObject();

    ob.put("testZipUrl", assignment.getTestZipUrl());
    ob.put("testZipChecksum", assignment.getTestZipChecksum());
    System.out.println(ob.toString());
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * get project checksum
   * 
   * @param assignmentName assignment name
   * @return checksum
   */
  @GET
  @Path("getAssignment")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAssignment(@QueryParam("assignmentName") String assignmentName) {
    Assignment assignment = dbManager.getAssignmentByName(assignmentName);
    JSONObject ob = new JSONObject();
    ob.put("description", assignment.getDescription());
    ob.put("deadline", assignment.getDeadline());
    ob.put("type", assignment.getType());

    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * 
   * @return AllAssignments
   */
  @GET
  @Path("getAllAssignments")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllAssignments() {
    List<Assignment> assignments = dbManager.getAllAssignment();
    JSONObject ob = new JSONObject();
    ob.put("allAssignments", assignments);
    return Response.ok().entity(ob.toString()).build();
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

  /**
   * get test folder
   * 
   * @param filePath folder directory
   * @return zip file
   */
  @GET
  @Path("getTestFile")
  public Response getTestFile(@QueryParam("filePath") String filePath) {
    File file = new File(filePath);

    ResponseBuilder response = Response.ok((Object) file);
    response.header("Content-Disposition", "attachment;filename=");
    return response.build();
  }

}
