package fcu.selab.progedu.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
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
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.data.Project;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.utils.Linux;
import fcu.selab.progedu.utils.ZipHandler;

@Path("project/")
public class ProjectService {

  private Conn conn = Conn.getInstance();
  private GitlabUser root = conn.getRoot();
  private ZipHandler zipHandler;
  private JenkinsApi jenkins = JenkinsApi.getInstance();

  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
  private CourseConfig courseConfig = CourseConfig.getInstance();

  private String jenkinsRootUsername;
  private String jenkinsRootPassword;
  private String mailUsername;
  private String mailPassword;

  private ProjectDbManager dbManager = ProjectDbManager.getInstance();
  private CommitRecordService commitRecordService = new CommitRecordService();
  private CommitResultService commitResultService = new CommitResultService();
  private CommitRecordStateService commitRecordStateService = new CommitRecordStateService();

  private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
  private static String uploadDir = TEMP_DIR + "/uploads/";
  private static String testDir = TEMP_DIR + "/tests/";

  boolean isSave = true;

  private String testZipChecksum = "";
  private String testZipUrl = "";

  AssignmentTypeSelector assignmentTypeSelector;

  /**
   * Constuctor
   */
  public ProjectService() {
    try {
      zipHandler = new ZipHandler();
      jenkinsRootUsername = jenkinsData.getJenkinsRootUsername();
      jenkinsRootPassword = jenkinsData.getJenkinsRootPassword();
      mailUsername = jenkinsData.getMailUser();
      mailPassword = jenkinsData.getMailPassword();

    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param name
   *          abc
   * @param readMe
   *          abc
   * @param assignmentType
   *          abc
   * @param uploadedInputStream
   *          abc
   * @param fileDetail
   *          abc
   * @return abc
   * @throws Exception
   *           abc
   */
  @POST
  @Path("create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response newProject(@FormDataParam("Hw_Name") String name,
      @FormDataParam("Hw_Deadline") String deadline, @FormDataParam("Hw_README") String readMe,
      @FormDataParam("fileRadio") String assignmentType,
      @FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {
    final Linux linuxApi = new Linux();
    String rootProjectUrl = null;
    String folderName = null;
    String filePath = null;
    boolean hasTemplate = false;

    assignmentTypeSelector = AssignmentTypeFactory.getAssignmentType(assignmentType);

    // 1. Create root project and get project id and url
    createRootProject(name);
    rootProjectUrl = getThisProjectUrl(name);

    // 2. Clone the project to C:\\Users\\users\\AppData\\Temp\\uploads
    String cloneFilePath = uploadDir + name;
    String cloneCommand = "git clone " + rootProjectUrl + " " + cloneFilePath;
    linuxApi.execLinuxCommand(cloneCommand);
    System.out.println("cloneCommand : " + cloneCommand);

    // 3. Store Zip File to folder if file is not empty
    if (!fileDetail.getFileName().isEmpty()) {
      hasTemplate = true;
      // get the folder name
      folderName = fileDetail.getFileName();
      // store to C://User/AppData/Temp/uploads/
      filePath = storeFileToTemp(fileDetail.getFileName(), uploadedInputStream);
    } else {
      if (assignmentType != null && !"".equals(assignmentType)) {
        filePath = this.getClass().getResource(assignmentTypeSelector.getSampleZip()).getFile();
      }
    }

    // 4. Unzip the file to the root project
    try {
      assignmentTypeSelector.unzip(filePath, folderName, name, zipHandler);
      setTestFileInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Add .gitkeep if folder is empty.
    findEmptyFolder(cloneFilePath);

    // 5. if README is not null
    if (!readMe.equals("<br>") || !"".equals(readMe) || !readMe.isEmpty()) {
      // Add readme to folder
      createReadmeFile(readMe, name);
    }

    // 6. Cmd gitlab add
    String addCommand = "git add .";
    linuxApi.execLinuxCommandInFile(addCommand, cloneFilePath);

    // 7. Cmd gitlab commit
    String commitCommand = "git commit -m \"Instructor&nbsp;Commit\"";
    linuxApi.execLinuxCommandInFile(commitCommand, cloneFilePath);

    // 8. Cmd gitlab push
    String pushCommand = "git push";
    linuxApi.execLinuxCommandInFile(pushCommand, cloneFilePath);

    // remove project file in linux
    String removeFileCommand = "rm -rf uploads/";
    linuxApi.execLinuxCommandInFile(removeFileCommand, TEMP_DIR);

    // String removeTestDirectoryCommand = "rm -rf tests/" + name;
    // linuxApi.execLinuxCommandInFile(removeTestDirectoryCommand, TEMP_DIR);

    // 9. Add project to database
    Date date = new Date();
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
    String dateTime = sdFormat.format(date);
    addProject(name, dateTime, deadline, readMe, assignmentType, hasTemplate, testZipChecksum,
        testZipUrl);

    List<GitlabUser> users = conn.getUsers();
    Collections.reverse(users);
    for (GitlabUser user : users) {
      if (user.getId() == 1) {
        continue;
      }

      // 10. Create student project, and import project
      conn.createPrivateProject(user.getId(), name, rootProjectUrl);
    }

    // 12. Create each Jenkins Jobs
    assignmentTypeSelector.createJenkinsJob(name, jenkinsRootUsername, jenkinsRootPassword);

    Response response = Response.ok().build();
    if (!isSave) {
      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return response;
  }

  private void createRootProject(String name) {
    conn.createRootProject(name);
  }

  public List<String> getAllProjectNames() {
    return dbManager.listAllProjectNames();
  }

  private String getThisProjectUrl(String name) {
    String url = null;
    String gitlabUrl = null;
    try {
      gitlabUrl = gitlabData.getGitlabRootUrl();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    List<GitlabProject> rootProjects = conn.getProject(root);
    for (GitlabProject project : rootProjects) {
      String proName = project.getName();
      if (proName.equals(name)) {
        url = gitlabUrl + "/root/" + name;
      }
    }
    return url;
  }

  private String storeFileToTemp(String fileName, InputStream uploadedInputStream) {
    try {
      createFolderIfNotExists(uploadDir);
    } catch (SecurityException se) {
      System.out.println(se.toString());
    }
    String uploadedFileLocation = uploadDir + fileName;
    try {
      saveToFile(uploadedInputStream, uploadedFileLocation);
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return uploadedFileLocation;
  }

  /**
   * Utility method to save InputStream data to target location/file
   * 
   * @param inStream
   *          - InputStream to be saved
   * @param target
   *          - full path to destination file
   */
  private void saveToFile(InputStream inStream, String target) throws IOException {
    int read = 0;
    byte[] bytes = new byte[1024];
    try (OutputStream out = new FileOutputStream(new File(target));) {
      while ((read = inStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a folder to desired location if it not already exists
   * 
   * @param dirName
   *          - full path to the folder
   * @throws SecurityException
   *           - in case you don't have permission to create the folder
   */
  private void createFolderIfNotExists(String dirName) {
    File theDir = new File(dirName);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
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

  private void createReadmeFile(String readMe, String projectName) {
    String projectDir = uploadDir + projectName;

    System.out.println("readMe : " + readMe);

    try (Writer writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(projectDir + "/README.md"), "utf-8"));) {
      writer.write(readMe);
    } catch (IOException ex) {
      // report
    }
  }

  /**
   * Add a project to database
   * 
   * @param name
   *          Project name
   * @param deadline
   *          Project deadline
   * @param readMe
   *          Project readme
   * @param fileType
   *          File type
   * @param hasTemplate
   *          Has template
   */
  public void addProject(String name, String createTime, String deadline, String readMe,
      String fileType, boolean hasTemplate, String testZipChecksum, String testZipUrl) {
    Project project = new Project();

    project.setName(name);
    project.setCreateTime(createTime);
    project.setDeadline(deadline);
    project.setDescription(readMe);
    project.setType(fileType);
    project.setHasTemplate(hasTemplate);
    project.setTestZipChecksum(testZipChecksum);
    project.setTestZipUrl(testZipUrl);

    dbManager.addProject(project);
  }

  /**
   * delete projects
   * 
   * @param name
   *          project name
   * @return response
   */
  @POST
  @Path("delete")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteProject(@FormDataParam("del_Hw_Name") String name) {
    Linux linuxApi = new Linux();
    // delete tomcat test file
    String removeTestDirectoryCommand = "rm -rf tests/" + name;
    linuxApi.execLinuxCommandInFile(removeTestDirectoryCommand, TEMP_DIR);

    String removeZipTestFileCommand = "rm tests/" + name + ".zip";
    linuxApi.execLinuxCommandInFile(removeZipTestFileCommand, TEMP_DIR);

    String removeFileCommand = "rm -rf tests/" + name + "-COMPLETE";
    linuxApi.execLinuxCommandInFile(removeFileCommand, TEMP_DIR);
    // delete db
    dbManager.deleteProject(name);
    commitRecordService.deleteRecord(name);
    commitResultService.deleteResult(name);
    commitRecordStateService.deleteRecordState(name);

    // delete gitlab
    conn.deleteProjects(name);
    String jenkinsUserName = "";
    String jenkinsPass = "";
    try {
      jenkinsUserName = JenkinsConfig.getInstance().getJenkinsRootUsername();
      jenkinsPass = JenkinsConfig.getInstance().getJenkinsRootPassword();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    String crumb = jenkins.getCrumb(jenkinsUserName, jenkinsPass);

    List<GitlabUser> users = conn.getUsers();
    // delete Jenkins
    for (GitlabUser user : users) {
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
   * @param name
   *          project name
   * @return response
   */
  @POST
  @Path("edit")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response editProject(@FormDataParam("Edit_Hw_Name") String name,
      @FormDataParam("Hw_Deadline") String deadline, @FormDataParam("Hw_README") String readMe,
      @FormDataParam("Hw_TestCase") InputStream uploadedInputStream,
      @FormDataParam("Hw_TestCase") FormDataContentDisposition fileDetail) {

    dbManager.editProject(deadline, readMe, name);

    if (!fileDetail.getFileName().isEmpty()) {
      // update test case
      String filePath = storeFileToTestsFolder(name + ".zip", uploadedInputStream);
      // update database checksum

      String checksum = getChecksum(filePath);
      System.out.println("checksum : " + checksum);

      dbManager.updateProjectChecksum(name, checksum);
    }

    Response response = Response.ok().build();
    if (!isSave) {
      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return response;
  }

  /**
   * get project checksum
   * 
   * @param projectName
   *          project name
   * @return checksum
   */
  @GET
  @Path("checksum")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProject(@QueryParam("proName") String projectName) {
    Project project = new Project();
    project = dbManager.getProjectByName(projectName);
    return Response.ok().entity(project).build();
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

  /**
   * Edit test case upload test case to test folder
   * 
   * @param fileName
   *          file name
   * @param uploadedInputStream
   *          file
   */
  private String storeFileToTestsFolder(String fileName, InputStream uploadedInputStream) {
    try {
      createFolderIfNotExists(testDir);
    } catch (SecurityException se) {
      System.out.println(se.toString());
    }
    String uploadedFileLocation = testDir + fileName;
    File uploadedFile = new File(uploadedFileLocation);
    if (uploadedFile.exists()) {
      uploadedFile.delete();
    }
    try {
      saveToFile(uploadedInputStream, uploadedFileLocation);
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return uploadedFileLocation;
  }

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

  private void findEmptyFolder(String path) {
    File dir = new File(path);
    File[] files = dir.listFiles();

    if (dir.exists() && dir.isDirectory()) {
      if (files.length == 0) {
        addGitkeep(path);
      } else {
        for (int i = 0; i < files.length; i++) {
          findEmptyFolder(files[i].getPath());
        }
      }
    }
  }

  private void addGitkeep(String path) {
    File gitkeep = new File(path + "/.gitkeep");
    if (!gitkeep.exists()) {
      try {
        gitkeep.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
