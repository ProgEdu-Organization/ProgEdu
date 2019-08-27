package fcu.selab.progedu.config;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtConfig {
  private static Key key = null;
  private static JwtConfig jwtConfig = null;
  
  private JwtConfig() {
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    System.out.println("key:" + key.getEncoded());
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
  public String generateToken(String userRole, String userName) {
    
    String jws = Jwts.builder().setIssuer("progedu").setSubject(userRole).setAudience(userName)
        .setExpiration(new Date((new Date()).getTime() +  3 * 60 * 60 * 1000))
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
      e.printStackTrace();
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
