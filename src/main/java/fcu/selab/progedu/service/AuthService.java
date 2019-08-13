package fcu.selab.progedu.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import com.csvreader.CsvReader;

import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.conn.LoginAuth;
import fcu.selab.progedu.data.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

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
}
