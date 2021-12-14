package fcu.selab.progedu;

import com.fasterxml.jackson.databind.ObjectMapper;
import fcu.selab.progedu.config.JwtConfig;
import fcu.selab.progedu.db.UserDbManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

  protected JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
    super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
    setAuthenticationManager(authenticationManager);
  }
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException, ServletException {

    String username = req.getParameter("username");
    String password = req.getParameter("password");
    password = UserDbManager.getInstance().passwordMD5(password);

    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }
  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
    StringBuffer as = new StringBuffer();
    for (GrantedAuthority authority : authorities) {
      as.append(authority.getAuthority()).append(",");
    }

    JwtConfig jwtConfig = JwtConfig.getInstance();

    String jwt = jwtConfig.generateTokenV2(as.toString(), authResult.getName());

    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out = resp.getWriter();
    out.write(new ObjectMapper().writeValueAsString(jwt));
    out.flush();
    out.close();
  }
  protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException, ServletException {
    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out = resp.getWriter();

    String jwt = "fail!";
    out.write(new ObjectMapper().writeValueAsString(jwt));
    out.flush();
    out.close();
  }
}
