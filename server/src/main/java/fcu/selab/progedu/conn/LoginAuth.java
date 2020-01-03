package fcu.selab.progedu.conn;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.RoleEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

/**
 * Servlet implementation class AfterEnter
 */
public class LoginAuth extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String USERNAME = "username";
  private static final String USER_TOKEN = "password";
  private GitlabService gitlabService = GitlabService.getInstance();
  private GitlabConfig gitlabConfig = GitlabConfig.getInstance();
  JwtConfig jwt = JwtConfig.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuth.class);

  /**
   * @throws LoadConfigFailureException .
   * @see HttpServlet#HttpServlet()
   */
  public LoginAuth() throws LoadConfigFailureException {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    final HttpSession session = request.getSession();
    String username = request.getParameter(USERNAME);
    String password = request.getParameter(USER_TOKEN);
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

    response.setStatus(200);
    
    try {
      PrintWriter pw = response.getWriter();
      pw.print(ob);
      pw.flush();
      pw.close();
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  private String checkPermission(String username, String password)
      throws LoadConfigFailureException {
    String role = "";
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
