package fcu.selab.progedu.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.csvreader.CsvReader;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.data.Project;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.ProjectDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.jenkins.JenkinsApi;

@Path("user/")
public class UserService {

  Conn userConn = Conn.getInstance();

  CourseConfig course = CourseConfig.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private ProjectDbManager projectDbManager = ProjectDbManager.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsApi jenkins = JenkinsApi.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

  /**
   * Upload a csv file for student batch registration
   * 
   * @param uploadedInputStream file of student list
   * @param fileDetail          file information
   * @return Response
   */
  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    Response response = Response.ok().build();
    boolean errorFlag = false;
    List<User> userList = new ArrayList<>();

    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream, "UTF-8"));
      csvReader.readHeaders();
      while (csvReader.readRecord()) {
        if (csvReader.get("Password").length() < 8) {
          errorFlag = true;
          response = Response.serverError().entity("password must be at least 8 characters.")
              .build();
          break;
        } else if (dbManager.checkUserName(csvReader.get("StudentId"))) {
          errorFlag = true;
          response = Response.serverError()
              .entity("username : " + csvReader.get("StudentId") + " is exist.").build();
          break;
        } else if (dbManager.checkEmail(csvReader.get("Email"))) {
          errorFlag = true;
          response = Response.serverError()
              .entity("Email : " + csvReader.get("Email") + " is exist.").build();
          break;
        }

        else {
          User user = new User();
          user.setUserName(csvReader.get("StudentId"));
          user.setName(csvReader.get("Name"));
          user.setEmail(csvReader.get("Email"));
          user.setPassword(csvReader.get("Password"));
          userList.add(user);
        }
      }
      if (!errorFlag) {
        register(userList);
      }

      csvReader.close();
    } catch (IOException e) {
      response = Response.serverError().entity("Fail !").build();
      e.printStackTrace();
    }

    return response;
  }

  /**
   * Translate uploaded file content to string and parse to register
   * 
   * @param userList file content to string
   */
  public void register(List<User> userList) {
    for (User user : userList) {
      userConn.createUser(user.getEmail(), user.getPassword(), user.getUserName(), user.getName());
    }
    printStudent(userList);
  }

  /**
   *
   * @param name  name
   * @param id    id
   * @param email email
   * @return response
   */
  @POST
  @Path("new")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createAStudentAccount(@FormDataParam("studentName") String name,
      @FormDataParam("studentId") String id, @FormDataParam("studentEmail") String email,
      @FormDataParam("password") String password) {
    System.out.println(email + password + id + name);
    boolean isSave = false;
    try {
      isSave = userConn.createUser(email, password, id, name);
      User user = dbManager.getUser(id);
      boolean isSuccess = importPreviousProject(user);
      isSave = isSave && isSuccess;
    } catch (Exception e) {
      e.printStackTrace();
    }
    Response response = Response.ok().build();
    if (!isSave) {
      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    }
    return response;
  }

  /**
   * create previous project for new student.
   * 
   * @param user student
   * @return check
   */
  public boolean importPreviousProject(User user) {
    boolean check = false;
    List<Project> projects = projectDbManager.listAllProjects();
    String url = "";
    String gitlabUrl = "";
    String userName = user.getUserName();
    try {
      gitlabUrl = gitlabData.getGitlabRootUrl();
      for (Project project : projects) {
        String projectName = project.getName();
        Project project1 = projectDbManager.getProjectByName(projectName);
        url = gitlabUrl + "/root/" + projectName;
        userConn.createPrivateProject(user.getGitLabId(), project.getName(), url);
        boolean isSuccess = createPreviuosJob(userName, projectName, project1.getType());
        check = check && isSuccess;
      }
      check = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * create previous job for new student.
   * 
   * @param username student name
   * @param proName  project name
   * @param fileType job type
   * @return check
   */
  public boolean createPreviuosJob(String username, String proName, String fileType) {
    boolean check = false;
    String jenkinsRootUsername = null;
    String jenkinsRootPassword = null;
    String tempDir = System.getProperty("java.io.tmpdir");
    try {
      proName = proName.toUpperCase();

      String proUrl = gitlabData.getGitlabHostUrl() + "/" + username + "/" + proName + ".git";
      proUrl = proUrl.toLowerCase();
      String filePath = tempDir + "/configs/" + proName + ".xml";
      jenkins.modifyXmlFileUrl(filePath, proUrl);
      if ("Javac".equals(fileType)) {
        jenkins.modifyXmlFileCommand(filePath, username, proName);
      }
      if ("Maven".equals(fileType)) {
        jenkins.modifyXmlFileProgEdu(filePath, username, proName);
      }
      if ("Web".equals(fileType)) {
        jenkins.modifyWebXmlFile(filePath, username, proName);
      }

      jenkinsRootUsername = jenkinsData.getJenkinsRootUsername();
      jenkinsRootPassword = jenkinsData.getJenkinsRootPassword();
      String jenkinsCrumb = jenkins.getCrumb(jenkinsRootUsername, jenkinsRootPassword);
      jenkins.postCreateJob(username, proName, proUrl, jenkinsCrumb, filePath);
      jenkins.buildJob(username, proName, jenkinsCrumb);
      check = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * Print user information
   * 
   * @param student user
   */
  public void printStudent(List<User> student) {
    String userName = "";
    String password = "";
    String email = "";
    String name = "";
    for (User user : student) {
      userName = user.getUserName();
      password = user.getPassword();
      email = user.getEmail();
      name = user.getName();

      System.out.println("userName: " + userName + ", password: " + password + ", email: " + email
          + ", name: " + name);
    }
  }

  /**
   * Get all user on GitLab
   * 
   * @return all GitLab users
   */
  public List<GitlabUser> getUsers() {
    List<GitlabUser> users = new ArrayList<>();
    users = userConn.getUsers();
    return users;
  }

  /**
   * Change user password
   * 
   * @param oldPwd   old password
   * @param newPwd   new password
   * @param checkPwd check new password
   * @return true false
   */
  @POST
  @Path("changePwd")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response changePassword(@FormDataParam("oldPwd") String oldPwd,
      @FormDataParam("newPwd") String newPwd, @FormDataParam("checkPwd") String checkPwd,
      @FormDataParam("userId") Integer userId) {

    System.out.println("oldPwd : " + oldPwd);
    System.out.println("newPwd : " + newPwd);
    System.out.println("checkPwd : " + checkPwd);
    System.out.println("userId : " + userId);

    String userName = userConn.getUserById(userId).getUsername();
    System.out.println(userName);
    boolean check = dbManager.checkPassword(userName, newPwd);
    if (check) {
      System.out.println("false");
      return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    } else {
      dbManager.modifiedUserPassword(userName, newPwd);
      userConn.updateUserPassword(userId, newPwd);
    }

    return Response.ok().build();

  }
}
