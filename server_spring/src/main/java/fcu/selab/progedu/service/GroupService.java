package fcu.selab.progedu.service;

import java.text.SimpleDateFormat;
import java.util.List;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.UserDbService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss.S");

    JSONArray projectList = new JSONArray();
    List<GroupProject> groupProjects = group.getProjects();
    for(GroupProject temp: groupProjects) {
      JSONObject project = new JSONObject();
      project.put("id", temp.getId());
      project.put("name", temp.getName());

      String releaseTime;
      try {
        releaseTime = dateFormat.format(temp.getReleaseTime());
        project.put("releaseTime", releaseTime);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String createTime = dateFormat.format(temp.getCreateTime());
      String deadline = dateFormat.format(temp.getDeadline());
      project.put("createTime", createTime);
      project.put("deadline", deadline);

      project.put("description", temp.getDescription());

      JSONObject type = new JSONObject();
      type.put("typeName", temp.getType().getTypeName());
      project.put("type", type);
      projectList.add(project);
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", group.getGroupName());
    jsonObject.put("leader", group.getLeader());
    jsonObject.put("members", group.getMembers());
    jsonObject.put("project", projectList);
    return new ResponseEntity<Object>(jsonObject, headers, HttpStatus.OK);
  }

  @GetMapping("")
  public ResponseEntity<Object> getAllGroup() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");

    JSONArray jsonArray = new JSONArray();
    List<Group> groups = gdb.getGroups();
    for(Group group : groups) {
      ResponseEntity<Object> responseEntity = getGroup(group.getGroupName());
      JSONObject jsonObject = (JSONObject) JSONValue.parse(
          responseEntity.getBody().toString().replaceAll("\"gitLabToken\":null,", ""));
      jsonArray.add(jsonObject);
    }
    return new ResponseEntity<Object>(jsonArray, headers, HttpStatus.OK); 
  }
}
