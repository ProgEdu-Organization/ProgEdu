package fcu.selab.progedu.filter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class JwtTokenUtil {

  // Token請求頭
  public static final String TOKEN_HEADER = "Authorization";
  // Token字首
  public static final String TOKEN_PREFIX = "Bearer ";

  // 簽名主題
  public static final String SUBJECT = "piconjo";
  // 過期時間
  public static final long EXPIRITION = 1000 * 24 * 60 * 60 * 7;
  // 應用金鑰
  public static final String APPSECRET_KEY = "piconjo_secret";
  // 角色許可權宣告
  private static final String ROLE_CLAIMS = "role";

  /**
   * 生成Token
   */
  public static String createToken(String username,String role) {
    Map<String,Object> map = new HashMap<>();
    map.put(ROLE_CLAIMS, role);

    String token = Jwts
            .builder()
            .setSubject(username)
            .setClaims(map)
            .claim("username",username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
            .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
    return token;
  }

  /**
   * 校驗Token
   */
  public static Claims checkJWT(String token) {
    try {
      final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
      return claims;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 從Token中獲取username
   */
  public static String getUsername(String token){
    Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
    return claims.get("username").toString();
  }

  /**
   * 從Token中獲取使用者角色
   */
  public static String getUserRole(String token){
    Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
    return claims.get("role").toString();
  }

  /**
   * 校驗Token是否過期
   */
  public static boolean isExpiration(String token){
    Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
    return claims.getExpiration().before(new Date());
  }
}
