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

import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.csvreader.CsvReader;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.data.Assignment;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.jenkins.JenkinsApi;

@Path("user/")
public class UserService {

  Conn userConn = Conn.getInstance();

  CourseConfig course = CourseConfig.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private AssignmentDbManager projectDbManager = AssignmentDbManager.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
  private JenkinsApi jenkins = JenkinsApi.getInstance();
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

  /**
   * Upload a csv file for student batch registration
   * 
   * @param uploadedInputStream
   *          file of student list
   * @param fileDetail
   *          file information
   * @return Response
   */
  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    Response response = null;
    List<User> userList = new ArrayList<>();

    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream, "UTF-8"));
      csvReader.readHeaders();
      while (csvReader.readRecord()) {
        User user = new User(csvReader.get("StudentId"), csvReader.get("Name"),
            csvReader.get("Email"), csvReader.get("Password"), true);
        userList.add(user);
        response = checkErrorType(user);
        if (response != null) {
          break;
        }
      }

      if (response == null) {
        response = checkForDuplicates(userList);
        if (response == null) {
          register(userList);
          response = Response.ok().build();
        }

      }
      csvReader.close();
    } catch (IOException e) {
      response = Response.serverError().entity("failed !").build();
      e.printStackTrace();
    }

    return response;
  }

  /**
   * check for duplicates.
   * 
   * @param userList
   *          user list
   * @return response
   */
  public Response checkForDuplicates(List<User> userList) {
    Response response = null;
    for (int index = 0; index < userList.size() - 1; index++) {
      for (int index2 = index + 1; index2 < userList.size(); index2++) {
        if (userList.get(index).getStufentId().equals(userList.get(index2).getStufentId())) {
          response = Response.serverError().entity("username : "
              + userList.get(index).getStufentId() + " is duplicated in student list.").build();
          break;
        } else if (userList.get(index).getEmail().equals(userList.get(index2).getEmail())) {
          response = Response.serverError()
              .entity(
                  "Email : " + userList.get(index).getEmail() + " is duplicated in student list.")
              .build();
          break;
        }
      }
    }
    return response;
  }

  /**
   * check error type.
   * 
   * @param user
   *          user's information
   * @return response
   */
  public Response checkErrorType(User user) {
    Response response = null;
    if (user.getPassword().length() < 8) {
      response = Response.serverError().entity("Password must be at least 8 characters.").build();
    } else if (dbManager.checkStudentId(user.getStufentId())) {
      response = Response.serverError()
          .entity("username : " + user.getStufentId() + " already exists.").build();
    } else if (dbManager.checkEmail(user.getEmail())) {
      response = Response.serverError().entity("Email : " + user.getEmail() + " already exists.")
          .build();
    }
    return response;
  }

  /**
   * Translate uploaded file content to string and parse to register
   * 
   * @param userList
   *          file content to string
   */
  public void register(List<User> userList) {
    for (User user : userList) {
      userConn.createUser(user.getEmail(), user.getPassword(), user.getStufentId(), user.getName());
    }
    printStudent(userList);
  }

  /**
   *
   * @param name
   *          name
   * @param id
   *          id
   * @param email
   *          email
   * @param password
   *          password
   * @return response
   */
  @POST
  @Path("new")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createAStudentAccount(@FormDataParam("studentName") String name,
      @FormDataParam("studentId") String id, @FormDataParam("studentEmail") String email,
      @FormDataParam("password") String password) {
    Response response = null;
    boolean isSave = false;
    try {
      response = checkErrorType(new User(id, name, email, password, true));
      if (response == null) {
        isSave = userConn.createUser(email, password, id, name);
        User user = dbManager.getUser(id);
        boolean isSuccess = importPreviousProject(user);
        isSave = isSave && isSuccess;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    if (response == null) {
      if (isSave) {
        response = Response.ok().build();
      } else {
        response = Response.serverError().entity("failed !").build();
      }
    }

    return response;
  }

  /**
   * create previous project for new student.
   * 
   * @param user
   *          student
   * @return check
   */
  public boolean importPreviousProject(User user) {
    boolean check = false;
    List<Assignment> projects = projectDbManager.listAllAssignments();
    String url = "";
    String gitlabUrl = "";
    String userName = user.getStufentId();
    try {
      gitlabUrl = gitlabData.getGitlabRootUrl();
      for (Assignment project : projects) {
        String projectName = project.getName();
        Assignment project1 = projectDbManager.getAssignmentByName(projectName);
        url = gitlabUrl + "/root/" + projectName;
        userConn.createPrivateAssignment(user.getGitLabId(), project.getName(), url);
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
   * @param username
   *          student name
   * @param proName
   *          project name
   * @param fileType
   *          job type
   * @return check
   */
  public boolean createPreviuosJob(String username, String proName, int fileType) {
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
   * @param student
   *          user
   */
  public void printStudent(List<User> student) {
    String userName = "";
    String password = "";
    String email = "";
    String name = "";
    boolean display = true;
    for (User user : student) {
      userName = user.getStufentId();
      password = user.getPassword();
      email = user.getEmail();
      name = user.getName();
      display = user.getDisplay();

      System.out.println("userName: " + userName + ", password: " + password + ", email: " + email
          + ", name: " + name + ", display: " + display);
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
   * @param oldPwd
   *          old password
   * @param newPwd
   *          new password
   * @param checkPwd
   *          check new password
   * @return true false
   */
  @POST
  @Path("changePwd")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response changePassword(@FormDataParam("oldPwd") String oldPwd,
      @FormDataParam("newPwd") String newPwd, @FormDataParam("checkPwd") String checkPwd,
      @FormDataParam("userId") Integer userId) {
    String userName = userConn.getUserById(userId).getUsername();
    boolean check = dbManager.checkPassword(userName, oldPwd);
    if (check) {
      dbManager.modifiedUserPassword(userName, newPwd);
      userConn.updateUserPassword(userId, newPwd);
    } else {
      return Response.serverError().entity("The current password is wrong").build();

    }

    return Response.ok().build();

  }
}
