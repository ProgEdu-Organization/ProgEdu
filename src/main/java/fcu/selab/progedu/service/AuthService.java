package fcu.selab.progedu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import io.jsonwebtoken.Claims;

@Path("auth/")
public class AuthService {
  JwtConfig jwt = JwtConfig.getInstance();

  /**
   * @param token test
   * @return Response
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response checkAuth(@FormParam("token") String token) {
    System.out.println(token);
    JSONObject ob = new JSONObject();
    if (!token.equals("null") && jwt.validateToken(token)) {
      Claims body = jwt.decodeToken(token);
      /*
       * add the database auth
       */
      System.out.print("body" + body);
      if ((boolean) body.get("admin")) {
        ob.put("isLogin", true);
        ob.put("admin", true);
      } else {
        ob.put("isLogin", true);
        ob.put("admin", false);
      }
    } else {
      ob = new JSONObject();
      ob.put("isLogin", false);
    }
    return Response.ok().entity(ob.toString()).build();
  }

  private String checkPermission(String username, String password) {
    String role = "";
    if (checkPassword(username, password)) {
      role = getRole(username).getTypeName();
    }
    return role;
  }

  private boolean checkPassword(String username, String password) {
    return UserDbManager.getInstance().checkPassword(username, password);
  }

  private RoleEnum getRole(String username) {
    UserDbManager userDb = UserDbManager.getInstance();
    RoleUserDbManager roleUserDb = RoleUserDbManager.getInstance();
    RoleDbManager roleDb = RoleDbManager.getInstance();
    int uid = userDb.getUserIdByUsername(username);
    int rid = roleUserDb.getTopRid(uid);
    return roleDb.getRoleNameById(rid);
  }
}
