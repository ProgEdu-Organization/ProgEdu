package fcu.selab.progedu.conn;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.RoleEnum;

/**
 * Servlet implementation class AfterEnter
 */
public class LoginAuth extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String USERNAME = "username";
  private static final String USER_PASSWORD = "password";
  private GitlabService gitlabService = GitlabService.getInstance();
  private GitlabConfig gitlabConfig = GitlabConfig.getInstance();
  JwtConfig jwt = JwtConfig.getInstance();

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final HttpSession session = request.getSession();
    String username = request.getParameter(USERNAME);
    String password = request.getParameter(USER_PASSWORD);
    String token;
    JSONObject ob = new JSONObject();

    try {
      String user = checkPermission(username, password);
      if (!user.equals("")) {
        ob.put("isLogin", true);
        ob.put("user", user);
        token = jwt.generateToken(user, username);
        ob.put("token", token);
      } else {
        ob.put("isLogin", false);
      }
    } catch (LoadConfigFailureException e) {
      ob.put("isLogin", false);
      e.printStackTrace();
    }

    System.out.println(ob.toString());
    response.setStatus(200);
    PrintWriter pw = response.getWriter();
    pw.print(ob);
    pw.flush();
    pw.close();
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

    System.out.println("test1");
    int uid = userDb.getUserIdByUsername(username);
    System.out.println("test2");
    int rid = roleUserDb.getTopRid(uid);
    RoleDbManager roleDb = RoleDbManager.getInstance();
    System.out.println("rid:" + rid);
    return roleDb.getRoleNameById(rid);
  }

}
