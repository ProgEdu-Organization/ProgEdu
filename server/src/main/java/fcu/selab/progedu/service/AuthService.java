package fcu.selab.progedu.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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
   * @return Response
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response checkAuth( @Context SecurityContext securityContext ) {

    JSONObject ob = new JSONObject();

    if (securityContext.isUserInRole("TEACHER")) {
      ob.put("isLogin", true);
      ob.put("isTeacher", true);
    } else if (securityContext.isUserInRole("STUDENT")) {
      ob.put("isLogin", true);
      ob.put("isStudent", true);
    } else {
      ob.put("isLogin", false);
    }
    return Response.ok().entity(ob.toString()).build();
  }
}
