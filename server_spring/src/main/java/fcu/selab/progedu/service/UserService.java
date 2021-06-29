package fcu.selab.progedu.service;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value ="/user")
public class UserService {

  private UserDbManager dbManager = UserDbManager.getInstance();

  @GetMapping("/getUsers")
  public ResponseEntity<Object> getUsers() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    List<User> users = dbManager.getAllUsers();
    List<JSONObject> userListEntities = new ArrayList<>();

    for (User user : users) {
      JSONObject entity = new JSONObject();
      entity.put("gitLabId", user.getGitLabId());
      entity.put("password", user.getPassword());


      List<JSONObject> roleList = new ArrayList<>();
      for (RoleEnum userRule : user.getRole()) {
        JSONObject typeName = new JSONObject();
        typeName.put("typeName", userRule.getTypeName());
        roleList.add(typeName);
      }

      entity.put("role", roleList);

      entity.put("gitLabToken", user.getGitLabToken());
      entity.put("display", user.getDisplay());
      entity.put("name", user.getName());
      entity.put("id", user.getId());
      entity.put("email", user.getEmail());
      entity.put("username", user.getUsername());

      userListEntities.add(entity);
    }

    JSONObject rootUserEntity = new JSONObject();
    rootUserEntity.put("User", userListEntities);


    return new ResponseEntity<Object>(rootUserEntity, headers, HttpStatus.OK);
  }

}
