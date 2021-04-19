package fcu.selab.progedu.service;

import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import javax.annotation.Priority;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class JwtFilter implements ContainerRequestFilter {

  JwtConfig jwt = JwtConfig.getInstance();

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    System.out.println("franky-test filter");
    System.out.println(containerRequestContext.getHeaders());

    MultivaluedMap<String, String> queryParameters = containerRequestContext
            .getUriInfo().getQueryParameters();

    String jwtToken = queryParameters.getFirst("token");

    if (jwtToken != null && jwt.validateToken(jwtToken)) {
      Claims body = jwt.decodeToken(jwtToken);
      System.out.print("body" + body);
      RoleEnum roleEnum = RoleEnum.getRoleEnum((String) body.get("sub"));

      Set<String> roles = new HashSet<>();


      switch (roleEnum) {
        case TEACHER: {
          roles.add("TEACHER");
          break;
        }
        case STUDENT: {
          roles.add("STUDENT");
          break;
        }
        default: {
          break;
        }
      }
      Authorizer authorizer = new Authorizer(roles, "admin",true);
      containerRequestContext.setSecurityContext(authorizer);

    } else {
      containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
              .entity("Cannot access")
              .build());

//      Authorizer authorizer = new Authorizer(null, null,false);
//      containerRequestContext.setSecurityContext(authorizer);
    }

  }

  public static class Authorizer implements SecurityContext {

    Set<String> roles;
    String username;
    boolean isSecure;

    /**
     * Todo
     */
    public Authorizer(Set<String> roles, final String username,
                      boolean isSecure) {
      this.roles = roles;
      this.username = username;
      this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
      return null;
    }

    @Override
    public boolean isUserInRole(String role) {
      return roles.contains(role);
    }

    @Override
    public boolean isSecure() {
      return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
      return "Your Scheme";
    }


  }
}
