package fcu.selab.progedu.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import com.csvreader.CsvReader;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.User;
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

  /**
   * Upload a csv file for student batch registration
   * 
   * @param uploadedInputStream file of student list
   * @return Response
   */
  @POST
  @Path("upload")
  public Response createAccounts(@FormDataParam("file") InputStream uploadedInputStream) {
    Response response = null;
    List<User> users = new ArrayList<>();
    String errorMessage = "";
    System.out.println("test1");
    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream, "UTF-8"));
      csvReader.readHeaders();
      System.out.println("test2");
      while (csvReader.readRecord()) {
        User newUser = new User();
        String username = csvReader.get("Username");
        String password = csvReader.get("Password");
        String name = csvReader.get("Name");
        String email = csvReader.get("Email");

        newUser.setDisplay(true);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setEmail(email);
        System.out.println("test3");
        errorMessage = getErrorMessage(users, newUser);
        if (errorMessage.isEmpty()) {
          users.add(newUser);
        } else {
          break;
        }
      }
      csvReader.close();
      System.out.println("test4");
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
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response createAccount(@FormParam("name") String name,
      @FormParam("username") String username, @FormParam("email") String email,
      @FormParam("password") String password, @FormParam("isDisplayed") boolean isDisplayed) {
    Response response = null;
    System.out.println(name + "  " + username);
    User user = new User(username, name, email, password, isDisplayed);
    String errorMessage = getErrorMessage(user);
    System.out.println(errorMessage);
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
   * (to do )
   * 
   * @param id              (to do )
   * @param currentPassword (to do )
   * @param newPassword     (to do )
   * @return response (to do)
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

  /**
   * Get all user which role is student
   * 
   * @return all GitLab users
   */
  @GET
  @Path("getUsers")
  public Response getUsers() {
    List<User> users = dbManager.listAllUsers();

    JSONObject ob = new JSONObject();
    ob.put("Users", users);
    return Response.ok().entity(ob.toString()).build();
  }

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
   */
  private void register(User user) throws IOException {
    GitlabUser gitlabUser = gitlabService.createUser(user.getEmail(), user.getPassword(),
        user.getUsername(), user.getName());
    user.setGitLabToken(gitlabUser.getPrivateToken());
    user.setGitLabId(gitlabUser.getId());

    dbManager.addUser(user);
    
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

  private boolean isDuplicateEmail(String email) {
    boolean isDuplicateEmail = false;
    if (dbManager.checkEmail(email)) {
      isDuplicateEmail = true;
    }
    return isDuplicateEmail;
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
