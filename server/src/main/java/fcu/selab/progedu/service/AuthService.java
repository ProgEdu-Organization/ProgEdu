package fcu.selab.progedu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.RoleDbManager;
import fcu.selab.progedu.db.RoleUserDbManager;
import fcu.selab.progedu.db.UserDbManager;
import io.jsonwebtoken.Claims;

@Path("auth/")
public class AuthService {
  JwtConfig jwt = JwtConfig.getInstance();
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

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
          LOGGER.info("AuthService Teacher Login Successed");
          break;
        }
        case STUDENT: {
          ob.put("isLogin", true);
          ob.put("isStudent", true);
          LOGGER.info("AuthService Student Login Successed");
          break;
        }
        default: {
          ob.put("isLogin", false);
          LOGGER.info("AuthService Default Login Failed");
        }
      }

    } else {
      ob.put("isLogin", false);
      LOGGER.info("AuthService Else Login Failed");
    }
    return Response.ok().entity(ob.toString()).build();
  }
}
