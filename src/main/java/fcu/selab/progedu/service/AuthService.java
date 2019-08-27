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
      System.out.print("body" + body);
      RoleEnum roleEnum = RoleEnum.getRoleEnum((String) body.get("sub"));
      
      switch (roleEnum) {
        case TEACHER: {
          ob.put("isLogin", true);
          ob.put("isTeacher", true);
          break;
        }
        case STUDENT: {
          ob.put("isLogin", true);
          ob.put("isStudent", true);
          break;
        }
        default: {
          ob.put("isLogin", false);
        }
      }

    } else {
      ob.put("isLogin", false);
    }
    System.out.println(ob.toString());
    return Response.ok().entity(ob.toString()).build();
  }
}
