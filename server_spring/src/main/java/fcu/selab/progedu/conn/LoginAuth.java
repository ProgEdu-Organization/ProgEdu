package fcu.selab.progedu.conn;



import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minidev.json.JSONObject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/LoginAuth")
public class LoginAuth {
  private static final long serialVersionUID = 1L;
  private static final String USERNAME = "username";
  private static final String USER_TOKEN = "password";
  private GitlabConfig gitlabConfig = GitlabConfig.getInstance();
  JwtConfig jwt = JwtConfig.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuth.class);

  @PostMapping("")
  public ResponseEntity<Object> doPost(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {

    HttpHeaders headers = new HttpHeaders();

    headers.add("Access-Control-Allow-Origin", "*");

    String token;
    JSONObject ob = new JSONObject();
    try {
      String role = checkPermission(username, password);
      if (!role.equals("")) {
        ob.put("isLogin", true);
        ob.put("role", role);
        String name = getNameByUsername(username);
        token = jwt.generateToken(role, username, name);
        ob.put("token", token);
      } else {
        ob.put("isLogin", false);
      }
    } catch (LoadConfigFailureException | JSONException e) {
      try {
        ob.put("isLogin", false);
      } catch (JSONException ex) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(ex));
        LOGGER.error(ex.getMessage());
      }
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);

  }

  // Todo 命名不好, 還有不要用回傳空字串 來判斷有沒有效
  private String checkPermission(String username, String password)
      throws LoadConfigFailureException {
    String role = "";

    //Todo 這耦合了GitLab, 用此依據判斷是不是teacher不好
    if (username.equals(gitlabConfig.getGitlabRootUsername())
        && password.equals(gitlabConfig.getGitlabRootPassword())) {
      role = "teacher";
    } else {
      if (checkPassword(username, password)) {
        role = getRole(username).getTypeName();
      }
    }
    return role;
  }

  private boolean checkPassword(String username, String password) {
    return UserDbManager.getInstance().checkPassword(username, password);
  }

  private RoleEnum getRole(String username) {
    UserDbManager userDb = UserDbManager.getInstance();
    RoleUserDbManager roleUserDb = RoleUserDbManager.getInstance();
    int uid = userDb.getUserIdByUsername(username);
    int rid = roleUserDb.getTopRid(uid);
    RoleDbManager roleDb = RoleDbManager.getInstance();
    return roleDb.getRoleNameById(rid);
  }

  private String getNameByUsername(String username) {
    String name;
    try {
      if (!username.equals(gitlabConfig.getGitlabRootUsername())) {
        name = UserDbManager.getInstance().getUser(username).getName();
      } else {
        name = username;
      }
    } catch ( LoadConfigFailureException e) {
      name = username;
    }
    return name;
  }

}
