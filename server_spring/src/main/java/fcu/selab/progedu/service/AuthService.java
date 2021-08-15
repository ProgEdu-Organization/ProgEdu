package fcu.selab.progedu.service;

import fcu.selab.progedu.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping(value ="/auth")
public class AuthService {

  JwtConfig jwt = JwtConfig.getInstance();

  /**
   * @return Response
   */
  @PostMapping("login")
  public ResponseEntity<Object> checkAuth(@RequestParam("token") String token) {

    HttpHeaders headers = new HttpHeaders();

    headers.add("Access-Control-Allow-Origin", "*");

    JSONObject ob = new JSONObject();

    Claims userInfo = jwt.decodeToken(token);


    if (userInfo.getSubject().equals("teacher")) {
      ob.put("isLogin", true);
      ob.put("isTeacher", true);
    } else if (userInfo.getSubject().equals("student")) {
      ob.put("isLogin", true);
      ob.put("isStudent", true);
    } else {
      ob.put("isLogin", false);
    }
    return new ResponseEntity<Object>(ob, headers, HttpStatus.OK);
  }

}
