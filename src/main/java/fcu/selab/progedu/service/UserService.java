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
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.UserDbManager;

@Path("user/")
public class UserService {
  private static UserService instance = new UserService();

  public static UserService getInstance() {
    return instance;
  }

  GitlabService gitlabService = GitlabService.getInstance();

  CourseConfig course = CourseConfig.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private AssignmentDbManager projectDbManager = AssignmentDbManager.getInstance();
  private GitlabConfig gitlabData = GitlabConfig.getInstance();
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
  public Response createAccounts(@FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    Response response = null;
    List<User> users = new ArrayList<>();
    String errorMessage = "";
    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream, "UTF-8"));
      csvReader.readHeaders();
      while (csvReader.readRecord()) {
        User newUser = new User(csvReader.get("StudentId"), csvReader.get("Name"),
            csvReader.get("Email"), csvReader.get("Password"), true);
        errorMessage = getErrorMessage(users, newUser);
        if (errorMessage.isEmpty()) {
          users.add(newUser);
        } else {
          break;
        }
      }
      csvReader.close();

      if (errorMessage == null || errorMessage.isEmpty()) {
        for (User user : users) {
          register(user);
        }
        response = Response.ok().build();
      } else {
        response = Response.serverError().entity(errorMessage).build();
      }
    } catch (IOException e) {
      response = Response.serverError().entity("failed !").build();
      e.printStackTrace();
    }
    return response;
  }

  /**
   *
   * @param name     name
   * @param username id
   * @param email    email
   * @param password password
   * @return response
   */
  @POST
  @Path("new")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createAccount(@FormDataParam("name") String name,
      @FormDataParam("username") String username, @FormDataParam("email") String email,
      @FormDataParam("password") String password) {
    Response response = null;
    User user = new User(username, name, email, password, true);
    String errorMessage = getErrorMessage(user);

    if (errorMessage.isEmpty()) {
      try {
        register(user);
        response = Response.ok().build();
      } catch (IOException e) {
        response = Response.serverError().entity("Failed !").build();
        e.printStackTrace();
      }
    } else {
      response = Response.serverError().entity(errorMessage).build();
    }

    return response;
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
  @Path("updatePassword")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response updatePassword(@FormDataParam("id") Integer id,
      @FormDataParam("currentPassword") String currentPassword,
      @FormDataParam("newPassword") String newPassword) {
    Response response = null;
    String username = gitlabService.getUserById(id).getUsername();
    boolean isSame = dbManager.checkPassword(username, currentPassword);
    if (isSame) {
      gitlabService.updateUserPassword(id, newPassword);
      dbManager.modifiedUserPassword(username, newPassword);
      response = Response.ok().build();
    } else {
      response = Response.ok().entity("The current password is wrong").build();
    }

    return response;

  }

//  /**
//   * create previous project for new student.
//   * 
//   * @param user student
//   * @return check
//   */
//  public boolean importPreviousProject(User user) {
//    boolean check = false;
//    List<Project> projects = projectDbManager.listAllProjects();
//    String url = "";
//    String gitlabUrl = "";
//    String userName = user.getUsername();
//    try {
//      gitlabUrl = gitlabData.getGitlabRootUrl();
//      for (Project project : projects) {
//        String projectName = project.getName();
//        Project project1 = projectDbManager.getProjectByName(projectName);
//        url = gitlabUrl + "/root/" + projectName;
//        GitlabProject gitlabProject = gitlabService.createPrivateProject(user.getGitLabId(),
//            project.getName(), url);
//        HttpConnect.getInstance().setGitlabWebhook(gitlabProject.getOwner().getName(),
//            gitlabProject);
//        boolean isSuccess = createPreviuosJob(userName, projectName, project1.getType());
//        check = check && isSuccess;
//      }
//      check = true;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return check;
//  }
//
//  /**
//   * create previous job for new student.
//   * 
//   * @param username student name
//   * @param proName  project name
//   * @param fileType job type
//   * @return check
//   */
//  public boolean createPreviuosJob(String username, String proName, String fileType) {
//    boolean check = false;
//    String jenkinsRootUsername = null;
//    String jenkinsRootPassword = null;
//    String tempDir = System.getProperty("java.io.tmpdir");
//    try {
//      proName = proName.toUpperCase();
//
//      String proUrl = gitlabData.getGitlabHostUrl() + "/" + username + "/" + proName + ".git";
//      proUrl = proUrl.toLowerCase();
//      String filePath = tempDir + "/configs/" + proName + ".xml";
//      jenkins.modifyXmlFileUrl(filePath, proUrl);
//      if ("Javac".equals(fileType)) {
//        jenkins.modifyXmlFileCommand(filePath, username, proName);
//      }
//      if ("Maven".equals(fileType)) {
//        jenkins.modifyXmlFileProgEdu(filePath, username, proName);
//      }
//      if ("Web".equals(fileType)) {
//        jenkins.modifyWebXmlFile(filePath, username, proName);
//      }
//
//      jenkinsRootUsername = jenkinsData.getJenkinsRootUsername();
//      jenkinsRootPassword = jenkinsData.getJenkinsRootPassword();
//      String jenkinsCrumb = jenkins.getCrumb(jenkinsRootUsername, jenkinsRootPassword);
//      jenkins.postCreateJob(username, proName, proUrl, jenkinsCrumb, filePath);
//      jenkins.buildJob(username, proName, jenkinsCrumb);
//      check = true;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return check;
//  }
//
//  /**
//   * Print user information
//   * 
//   * @param student user
//   */

//

  /**
   * Get all user which role is student
   * 
   * @return all GitLab users
   */
  public List<User> getStudents() {
    List<User> studentUsers = new ArrayList<>();
    List<User> users = dbManager.listAllUsers();

    for (User user : users) {
      if (user.getRole() == RoleEnum.STUDENT) {
        studentUsers.add(user);
      }
    }
    return studentUsers;
  }

  /**
   * Register user
   * 
   * @param user user of ProgEdu
   * @throws IOException
   */
  private void register(User user) throws IOException {
    GitlabUser gitlabUser = gitlabService.createUser(user.getEmail(), user.getPassword(),
        user.getUsername(), user.getName());
    dbManager.addUser(gitlabUser);
  }

  private boolean isPasswordTooShort(String password) {
    boolean isPasswordTooShort = false;
    if (password.length() < 8) {
      isPasswordTooShort = true;
    }
    return isPasswordTooShort;
  }

  private boolean isDuplicateUsername(String username) {
    boolean isDuplicateUsername = false;
    if (dbManager.checkUsername(username)) {
      isDuplicateUsername = true;
    }
    return isDuplicateUsername;
  }

  private boolean isDuplicateEmail(String email) {
    boolean isDuplicateEmail = false;
    if (dbManager.checkEmail(email)) {
      isDuplicateEmail = true;
    }
    return isDuplicateEmail;
  }

  private boolean isDuplicateUsername(List<User> users, String username) {
    boolean isDuplicateUsername = false;
    for (User user : users) {
      if (username.equals(user.getUsername())) {
        isDuplicateUsername = true;
        break;
      }
    }
    return isDuplicateUsername;
  }

  private boolean isDuplicateEmail(List<User> users, String email) {
    boolean isDuplicateEmail = false;
    for (User user : users) {
      if (email.equals(user.getEmail())) {
        isDuplicateEmail = true;
        break;
      }
    }
    return isDuplicateEmail;
  }

  private String getErrorMessage(List<User> users, User user) {
    String errorMessage = getErrorMessage(user);
    if (errorMessage.isEmpty()) {
      if (isDuplicateUsername(users, user.getUsername())) {
        errorMessage = "username : " + user.getUsername() + " is duplicated in student list.";
      } else if (isDuplicateEmail(users, user.getEmail())) {
        errorMessage = "Email : " + user.getEmail() + " is duplicated in student list.";
      }
    }
    return errorMessage;
  }

  private String getErrorMessage(User user) {
    String errorMessage = "";
    if (isPasswordTooShort(user.getPassword())) {
      errorMessage = user.getName() + " : Password must be at least 8 characters.";
    } else if (isDuplicateUsername(user.getUsername())) {
      errorMessage = "username : " + user.getUsername() + " already exists.";
    } else if (isDuplicateEmail(user.getEmail())) {
      errorMessage = "Email : " + user.getEmail() + " already exists.";
    }
    return errorMessage;
  }

}
