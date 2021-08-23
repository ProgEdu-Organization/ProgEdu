package fcu.selab.progedu.filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import com.alibaba.fastjson.JSON;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

  private AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager)
  {
    this.authenticationManager = authenticationManager;
  }

  /**
   * 驗證操作 接收並解析使用者憑證
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 從輸入流中獲取到登入的資訊
    // 建立一個token並呼叫authenticationManager.authenticate() 讓Spring security進行驗證
    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getParameter("username"),request.getParameter("password")));
  }

  /**
   * 驗證【成功】後呼叫的方法
   * 若驗證成功 生成token並返回
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
    User user= (User) authResult.getPrincipal();

    // 從User中獲取許可權資訊
    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
    // 建立Token
    String token = JwtTokenUtil.createToken(user.getUsername(), authorities.toString());

    // 設定編碼 防止亂碼問題
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    // 在請求頭裡返回建立成功的token
    // 設定請求頭為帶有"Bearer "字首的token字串
    response.setHeader("token", JwtTokenUtil.TOKEN_PREFIX + token);

    // 處理編碼方式 防止中文亂碼
    response.setContentType("text/json;charset=utf-8");
    // 將反饋塞到HttpServletResponse中返回給前臺
    response.getWriter().write(JSON.toJSONString("登入成功"));




  }

//  /**
//   * 驗證【失敗】呼叫的方法
//   */
//  @Override
//  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//



//    String returnData="";
//    // 賬號過期
//    if (failed instanceof AccountExpiredException) {
//      returnData="賬號過期";
//    }
//    // 密碼錯誤
//    else if (failed instanceof BadCredentialsException) {
//      returnData="密碼錯誤";
//    }
//    // 密碼過期
//    else if (failed instanceof CredentialsExpiredException) {
//      returnData="密碼過期";
//    }
//    // 賬號不可用
//    else if (failed instanceof DisabledException) {
//      returnData="賬號不可用";
//    }
//    //賬號鎖定
//    else if (failed instanceof LockedException) {
//      returnData="賬號鎖定";
//    }
//    // 使用者不存在
//    else if (failed instanceof InternalAuthenticationServiceException) {
//      returnData="使用者不存在";
//    }
//    // 其他錯誤
//    else{
//      returnData="未知異常";
//    }
//
//    // 處理編碼方式 防止中文亂碼
//    response.setContentType("text/json;charset=utf-8");
//    // 將反饋塞到HttpServletResponse中返回給前臺
//    response.getWriter().write(JSON.toJSONString(returnData));
//  }


}
