package fcu.selab.progedu.service;

import com.csvreader.CsvReader;
import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.Group;
import org.gitlab.api.models.GitlabUser;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import fcu.selab.progedu.service.GroupCommitRecordService;
import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.db.service.UserDbService;
import fcu.selab.progedu.service.GroupService;


@Path("user")
public class UserService {
  private static UserService instance = new UserService();

  public static UserService getInstance() {
    return instance;
  }

  private GitlabService gitlabService = GitlabService.getInstance();

  private CourseConfig course = CourseConfig.getInstance();
  private UserDbManager dbManager = UserDbManager.getInstance();
  private RoleUserDbManager rudb = RoleUserDbManager.getInstance();
  private RoleDbManager rdb = RoleDbManager.getInstance();
  private AssignmentService as = AssignmentService.getInstance();
  private GroupDbService gdb = GroupDbService.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  /**
   * Upload a csv file for student batch registration
   *
   * @param uploadedInputStream file of student list
   * @return Response
   */
  @POST
  @Path("/upload")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAccounts(@FormDataParam("file") InputStream uploadedInputStream) {
    Response response = null;
    List<User> users = new ArrayList<>();
    String errorMessage = "";
    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream, "BIG5"));
      csvReader.readHeaders();
      while (csvReader.readRecord()) {
        User newUser = new User();
        String role = csvReader.get("Role");
        RoleEnum roleEnum = RoleEnum.getRoleEnum(role);
        List<RoleEnum> roleList = new ArrayList<>();
        roleList.add(roleEnum);
        String username = csvReader.get("Username");
        String password = csvReader.get("Password");
        String name = csvReader.get("Name");
        String email = csvReader.get("Email");

        newUser.setDisplay(true);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setRole(roleList);
        errorMessage = getErrorMessage(users, newUser);
        if (errorMessage.isEmpty()) {
          users.add(newUser);
        } else {
          break;
        }
      }
      csvReader.close();
      System.out.println("errorMessage: " + errorMessage);
      if (errorMessage == null || errorMessage.isEmpty()) {
        for (User user : users) {
          register(user);
          createPreviousAssginment(user.getUsername(), user.getRole());
        }
        response = Response.ok().build();
      } else {
        response = Response.serverError().entity(errorMessage).build();
      }
    } catch (IOException e) {
      response = Response.serverError().entity("failed !").build();
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return response;
  }

  /**
   * @param name name
   * @param username id
   * @param email email
   * @param password password
   * @return response
   */
  @POST
  @Path("/new")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAccount(
      @FormParam("name") String name,
      @FormParam("username") String username,
      @FormParam("email") String email,
      @FormParam("password") String password,
      @FormParam("role") String role,
      @FormParam("isDisplayed") boolean isDisplayed) {

    Response response = null;
    List<RoleEnum> roleList = new ArrayList<>();

    RoleEnum roleEnum = RoleEnum.getRoleEnum(role);
    roleList.add(roleEnum);

    User user = new User(username, name, email, password, roleList, isDisplayed);
    String errorMessage = getErrorMessage(user);
    if (errorMessage.isEmpty()) {
      try {
        register(user);
        createPreviousAssginment(username, roleList);
        response = Response.ok().build();
      } catch (IOException e) {
        response = Response.serverError().entity("Failed !").build();
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }
    } else {
      response = Response.serverError().entity(errorMessage).build();
    }

    return response;
  }

  /**
   * (to do )
   *
   * @param username (to do )
   * @param currentPassword (to do )
   * @param newPassword (to do )
   * @return response (to do)
   */
  @POST
  @Path("updatePassword")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updatePassword(
      @FormDataParam("username") String username,
      @FormDataParam("currentPassword") String currentPassword,
      @FormDataParam("newPassword") String newPassword) {
    Response response = null;
    boolean isSame = dbManager.checkPassword(username, currentPassword);
    if (isSame) {
      int gitLabId = dbManager.getGitLabIdByUsername(username);
      gitlabService.updateUserPassword(gitLabId, newPassword);
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
  @Path("/getUsers")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUsers() {
    List<User> users = dbManager.getAllUsers();

    JSONObject ob = new JSONObject();
    ob.put("Users", users);
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * Display
   *
   * @param username username
   */
  @POST
  @Path("/display")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateStatus(@FormParam("username") String username) {
    boolean isDisplay = !dbManager.getUserStatus(username);
    dbManager.updateUserStatus(username, isDisplay);
    return Response.ok().build();
  }

  /**
   * Display
   *
   * @param username username
   */
  @GET
  @Path("/{username}/groups")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGroup(@PathParam("username") String username) {
    GroupService gs = new GroupService();
    int uid = dbManager.getUserIdByUsername(username);
    List<String> groupNames = gdb.getGroupNames(uid);
    JSONArray array = new JSONArray();
    for (String groupName : groupNames) {
      String group = gs.getGroup(groupName).getEntity().toString();
      JSONObject ob = new JSONObject(group);
      array.put(ob);
    }

    return Response.ok().entity(array.toString()).build();
  }

  /**
   * Get all user which role is student
   *
   * @return all GitLab users
   */
  public List<User> getStudents() {
    List<User> studentUsers = new ArrayList<>();
    List<User> users = dbManager.getAllUsers();

    for (User user : users) {
      if (user.getRole().contains(RoleEnum.STUDENT)) {
        studentUsers.add(user);
      }
    }
    return studentUsers;
  }

  /**
   * Delete user but gitlab repository not delete so you need manual delete
   *
   * @param username user name
   */
  @DELETE
  @Path("/{username}")
  public Response deleteUser(@PathParam("username") String username) {
    UserDbService userDbService = UserDbService.getInstance();
    int userId = userDbService.getId(username);
    return deleteUser(userId);
  }

  /**
   * Delete user
   *
   * @param userId user id
   */
  public Response deleteUser(int userId) {
    UserDbService userDbService = UserDbService.getInstance();


    ////delete Gitlab
    gitlabService.deleteUser( userDbService.getGitLabId(userId) );

    // if user's group has one user delete group
    GroupService groupService = GroupService.getInstance();
    List<Group> groups = gdb.getGroups(userId);
    for (Group group : groups) {

      if ( group.isNotMoreThanOneUser() ) { // delete Group
        groupService.removeGroup( group.getGroupName() );
        
      } else if (group.getLeader() == userId) { // change Group Leader and update DB
        List<User> groupUsers = group.getMembers();
        for (User groupUser:groupUsers) {
          if (groupUser.getId() != userId) {
            group.setLeader( groupUser.getId() );
            gdb.updateLeader( group );
            break;
          }
        }

      }

    }

    ////remove jenkins
    JenkinsService jenkinsService = JenkinsService.getInstance();
    List<String> assignmentNames = userDbService.getUserAssignmentNames(userId);
    String username = userDbService.getName(userId);
    for (String assignmentName : assignmentNames) {
      String jobName = jenkinsService.getJobName(username, assignmentName);
      jenkinsService.deleteJob(jobName);
    }


    //remove db
    userDbService.deleteUser(userId);

    return Response.ok().build();
  }

  /**
   * Register user
   *
   * @param user user of ProgEdu
   */
  private void register(User user) throws IOException {
    GitlabUser gitlabUser =
        gitlabService.createUser(
            user.getEmail(), user.getPassword(), user.getUsername(), user.getName());
    user.setGitLabToken(gitlabUser.getPrivateToken());
    user.setGitLabId(gitlabUser.getId());

    dbManager.addUser(user);
    rudb.addRoleUser(user);
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
    } else if (user.getRole() == null) {
      errorMessage = "Role is empty";
    }
    return errorMessage;
  }

  private void createPreviousAssginment(String username, List<RoleEnum> roles) {
    for (RoleEnum role : roles) {
      if (role == RoleEnum.STUDENT) {
        as.createPreviousAssignment(username);
        break;
      }
    }
  }
}
