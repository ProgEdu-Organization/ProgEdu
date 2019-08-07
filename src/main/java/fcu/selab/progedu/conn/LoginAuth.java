package fcu.selab.progedu.conn;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gitlab.api.models.GitlabSession;
import org.json.JSONObject;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.json.JSONObject;

/**
 * Servlet implementation class AfterEnter
 */
public class LoginAuth extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String USER_NAME = "username";
  private static final String PRIVATE_TOKEN = "private_token";
  GitlabConfig gitData = GitlabConfig.getInstance();
  CourseConfig courseData = CourseConfig.getInstance();
  private Conn conn = Conn.getInstance();
  boolean isEnter = true;
  public static Key key = null;
  
  /**
   * @throws LoadConfigFailureException .
   * @see HttpServlet#HttpServlet()
   */
  public LoginAuth() throws LoadConfigFailureException {
    super();
    if (key == null) {
      key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
      System.out.println("key:" + key);
    }
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
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String token = request.getParameter("password");
    JSONObject ob = new JSONObject();
    System.out.println("receive:" + username + ":" + password);
    /////////////////////////////////////////////////
    System.out.println("key" + key);

    ///////////////////////////////////////////////
    if (username.equals("root") && password.equals("zxcv1234")) {
      ob.put("isLogin", true);
      ob.put("user", "admin");
      token = generateToken("teacher", "M0707350", true);
      ob.put("token", token);
    } else if (username.equals("root") && password.equals("zxcv12345")) {
      ob.put("isLogin", true);
      ob.put("user", "user");
      token = generateToken("student","M0707350", false);
      ob.put("token", token);
    } else {
      ob.put("isLogin", false);
      ob.put("token", "FFFFF");
    }
    System.out.println(ob.toString());
    response.setStatus(200);
    PrintWriter pw = response.getWriter();
    pw.print(ob);
    pw.flush();
    pw.close();
  }

  /**
   * 利用JWT生成token
   *
   * @return token
   */
  public String generateToken(String userRole, String userName, boolean isAdmin) {
    String jws = Jwts.builder().setIssuer("progedu").setSubject(userRole).setAudience(userName)
        .claim("admin", isAdmin)
        .setExpiration(new Date((new Date()).getTime() + 60 * 60 * 1000))
        .setId(UUID.randomUUID().toString()).signWith(key).compact(); // just an example id

    return jws;
  }

  /**
   * @return token
   */
  

}
