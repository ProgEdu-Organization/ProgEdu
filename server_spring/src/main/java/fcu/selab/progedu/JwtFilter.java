package fcu.selab.progedu;

import fcu.selab.progedu.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends BasicAuthenticationFilter {



  public JwtFilter(AuthenticationManager authManager) {
    super(authManager);
  }



  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws IOException, ServletException {


    String header = req.getHeader("authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String jwtToken = req.getHeader("authorization");

    JwtConfig jwtConfig = JwtConfig.getInstance();
    Claims claims = jwtConfig.decodeToken(jwtToken.replace("Bearer",""));


    String username = claims.getSubject();//获取当前登录用户名
    List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(token);
    chain.doFilter(req,res);
  }
}
