package fcu.selab.progedu.service;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.UserDbService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/groups")
public class GroupService {
  private static GroupService instance = new GroupService();

  public static GroupService getInstance() {
    return instance;
  }

  private GitlabService gitlabService = GitlabService.getInstance();
  private UserService userService = new UserService();
  private GroupDbService gdb = GroupDbService.getInstance();
  private ProjectDbService pdb = ProjectDbService.getInstance();
  private UserDbService udb = UserDbService.getInstance();
  private GroupUserDbManager gudb = GroupUserDbManager.getInstance();
  private AssignmentService projectService = new AssignmentService();

  /**
   * get group info
   *
   * @param name group name
   * @return response
   */
  @GetMapping("/{name}")
  public ResponseEntity<Object> getGroup(@PathVariable("name") String name) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    Group group = gdb.getGroup(name);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", group.getGroupName());
    jsonObject.put("leader", group.getLeader());
    jsonObject.put("members", group.getMembers());
    jsonObject.put("project", group.getProjects());
    return new ResponseEntity<Object>(jsonObject, headers, HttpStatus.OK);
  }
}
