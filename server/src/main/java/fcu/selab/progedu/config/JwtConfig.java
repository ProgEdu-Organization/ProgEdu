package fcu.selab.progedu.config;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtConfig {
  private static Key key = null;
  private static JwtConfig jwtConfig = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtConfig.class);
  
  private JwtConfig() {
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }
  
  /**
   * 
   * @return jwt class
   */
  public static JwtConfig getInstance() {
    if (jwtConfig == null) {
      jwtConfig = new JwtConfig();
    }
    return jwtConfig;
  }
  
  /**
   * create the jwt 
   * @return token
   */
  public String generateToken(String userRole, String userName, String name) {
    
    String jws = Jwts.builder().setIssuer("progedu").setSubject(userRole).setAudience(userName)
            .claim("name", name)
        .setExpiration(new Date((new Date()).getTime() +  24 * 60 * 60 * 1000))
        .setId(UUID.randomUUID().toString()).signWith(key).compact(); // just an example id
    return jws;
  }
  
  /**
   * 
   */
  public boolean validateToken(String jwsToken) {
    boolean isValidate = false;
    try {
      Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
      isValidate = true;
    } catch (JwtException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } 
    return isValidate;
  }
  
  /**
   * using the key to encode the jwt
   *  key
   */
  public Claims decodeToken(String jwsToken) {
    if (this.validateToken(jwsToken)) {
      Claims claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken).getBody();
      return claimsJws;
    }
    return null;
  }
  
}
