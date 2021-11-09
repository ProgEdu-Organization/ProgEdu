package fcu.selab.progedu.service;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.gitlab.api.models.GitlabUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.csvreader.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value ="/user")
public class UserService {

  private static UserService instance = new UserService();
  public static UserService getInstance() {
    return instance;
  }

  private UserDbManager dbManager = UserDbManager.getInstance();
  private GitlabService gitlabService = GitlabService.getInstance();
  private RoleUserDbManager rudb = RoleUserDbManager.getInstance();
  private AssignmentService as = AssignmentService.getInstance();
  private GroupDbService gdb = GroupDbService.getInstance();



  @GetMapping("/getUsers")
  public ResponseEntity<Object> getUsers() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    List<User> users = dbManager.getAllUsers();
    List<JSONObject> userListEntities = new ArrayList<>();

    for (User user : users) {
      JSONObject entity = new JSONObject();
      entity.put("gitLabId", user.getGitLabId());
      entity.put("password", user.getPassword());


      List<JSONObject> roleList = new ArrayList<>();
      for (RoleEnum userRule : user.getRole()) {
        JSONObject typeName = new JSONObject();
        typeName.put("typeName", userRule.getTypeName());
        roleList.add(typeName);
      }

      entity.put("role", roleList);

      entity.put("gitLabToken", user.getGitLabToken());
      entity.put("display", user.getDisplay());
      entity.put("name", user.getName());
      entity.put("id", user.getId());
      entity.put("email", user.getEmail());
      entity.put("username", user.getUsername());

      userListEntities.add(entity);
    }

    JSONObject rootUserEntity = new JSONObject();
    rootUserEntity.put("Users", userListEntities);


    return new ResponseEntity<Object>(rootUserEntity, headers, HttpStatus.OK);
  }

  @PostMapping("/new")
  public ResponseEntity<Object> createAccount(
          @RequestParam("name") String name,
          @RequestParam("username") String username,
          @RequestParam("email") String email,
          @RequestParam("password") String password,
          @RequestParam("role") String role,
          @RequestParam("isDisplayed") boolean isDisplayed) {

    HttpHeaders headers = new HttpHeaders();
    //

    List<RoleEnum> roleList = new ArrayList<>();

    RoleEnum roleEnum = RoleEnum.getRoleEnum(role);
    roleList.add(roleEnum);

    User user = new User(username, name, email, password, roleList, isDisplayed);
    String errorMessage = getErrorMessage(user);
    if (errorMessage.isEmpty()) {
      try {
        register(user);
        createPreviousAssginment(username, roleList);
        return new ResponseEntity<>(headers, HttpStatus.OK);
      } catch (IOException e) {
        return new ResponseEntity<>("Failed !", headers, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return new ResponseEntity<>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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


  @PostMapping("/upload")
  public ResponseEntity<Object> createAccounts(@RequestParam("file") MultipartFile uploadedInputStream) {


    HttpHeaders headers = new HttpHeaders();
    //

    List<User> users = new ArrayList<>();
    String errorMessage = "";
    try {
      CsvReader csvReader = new CsvReader(new InputStreamReader(uploadedInputStream.getInputStream(), "BIG5"));
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
        return new ResponseEntity<>(headers, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (IOException e) {
      return new ResponseEntity<>("failed !", headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }




  @PostMapping("/updatePassword")
  public ResponseEntity<Object> updatePassword(
          @RequestParam("username") String username,
          @RequestParam("currentPassword") String currentPassword,
          @RequestParam("newPassword") String newPassword) {

    HttpHeaders headers = new HttpHeaders();
    //

    boolean isSame = dbManager.checkPassword(username, currentPassword);
    if (isSame) {
      int gitLabId = dbManager.getGitLabIdByUsername(username);
      gitlabService.updateUserPassword(gitLabId, newPassword);
      dbManager.modifiedUserPassword(username, newPassword);
      return new ResponseEntity<>(headers, HttpStatus.OK);
    } else {
      return new ResponseEntity<>("The current password is wrong", headers, HttpStatus.OK);
    }
  }

  @GetMapping("/{username}/groups")
  public ResponseEntity<Object> getGroup(@PathVariable("username") String username) {

    HttpHeaders headers = new HttpHeaders();
    //

    GroupService gs = new GroupService();
    int uid = dbManager.getUserIdByUsername(username);

    List<String> groupNames = gdb.getGroupNames(uid);
    List<JSONObject> array = new ArrayList<>();
    for (String groupName : groupNames) {
      JSONObject ob = gs.getGroupInfo(groupName);
      array.add(ob);
    }

    return new ResponseEntity<>(array, headers, HttpStatus.OK);
  }

  @GetMapping(
          value ="getUserCsvFile",
          produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
  )
  public ResponseEntity<Object>  getUserCsvFile() throws Exception{

    HttpHeaders headers = new HttpHeaders();

    //
    headers.add("Content-Disposition", "attachment;filename=" + "StudentTemplate.csv");

    InputStream targetStream = this.getClass().getResourceAsStream("/sample/StudentTemplate.csv");


    byte[] file = IOUtils.toByteArray(targetStream);
    return new ResponseEntity<Object>(file, headers, HttpStatus.OK);

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

  private boolean isPasswordTooShort(String password) {
    boolean isPasswordTooShort = false;
    if (password.length() < 8) {
      isPasswordTooShort = true;
    }
    return isPasswordTooShort;
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

  private void register(User user) throws IOException {
    GitlabUser gitlabUser =
            gitlabService.createUser(
                    user.getEmail(), user.getPassword(), user.getUsername(), user.getName());
    user.setGitLabToken(gitlabUser.getPrivateToken());
    user.setGitLabId(gitlabUser.getId());

    dbManager.addUser(user);
    rudb.addRoleUser(user);
  }

  private void createPreviousAssginment(String username, List<RoleEnum> roles) {
    for (RoleEnum role : roles) {
      if (role == RoleEnum.STUDENT) {
        as.createPreviousAssignment(username); // Todo 這裡會去依賴作業的API, 要創一個獨立的class 去處理比較好
        break;
      }
    }
  }

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

}
