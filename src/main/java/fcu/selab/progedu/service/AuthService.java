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

import fcu.selab.progedu.conn.LoginAuth;
import fcu.selab.progedu.data.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("auth/")
public class AuthService {
  /**
   * @param token test
   * @return Response
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateCommitResult(@FormParam("token") String token) {
    System.out.println(token);
    Claims body = (Claims) validateToken(token, LoginAuth.key).getBody();
    JSONObject ob = new JSONObject();
    if ((boolean) body.get("admin")) {
      ob.put("isLogin", true);
      ob.put("admin", true);
    } else {
      ob.put("isLogin", true);
      ob.put("admin", false);
    }
    System.out.println("LoginAuth.key:" + LoginAuth.key);
    
    return Response.ok().entity(ob.toString()).build();
  }
  
  /**
   * jwsToken a
   *  key 
   */
  public Jws validateToken(String jwsToken, Key key) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
      return claimsJws;
    } catch (JwtException e) {

      e.printStackTrace();
    }
    System.out.println("error");
    return null;
  }
}
