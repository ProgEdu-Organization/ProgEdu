package fcu.selab.progedu.service;

import fcu.selab.progedu.Greeting;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value ="/user")
public class UserService {

  private UserDbManager dbManager = UserDbManager.getInstance();

  @GetMapping("/getUsers")
  public Map<String, Object> getUsers() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    List<User> users = dbManager.getAllUsers();

    Map<String, Object> entities = new HashMap<String, Object>();
    entities.put("Users", users);
    return entities;
  }

}
