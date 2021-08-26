package fcu.selab.progedu.service;

import java.text.SimpleDateFormat;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.GroupProject;
import fcu.selab.progedu.db.GroupUserDbManager;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.db.service.UserDbService;


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

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

  /**
   * create gitlab group
   *
   * @param name group name
   * @param leader the username of team leader
   * @param members the members of group
   * @param projectType project type
   * @param projectName project name
   * @return response
   */
  @PostMapping("")
  public ResponseEntity<Object> createGroup(
          @RequestParam("name") String name,
          @RequestParam("leader") String leader,
          @RequestParam("member") List<String> members,
          @RequestParam("projectType") String projectType,
          @RequestParam("projectName") String projectName) {

    HttpHeaders headers = new HttpHeaders();
    //

    GitlabGroup gitlabGroup = gitlabService.createGroup(name);
    int groupGitLabId = gitlabGroup.getId();
    members.remove(leader);
    int leaderGitlabId = udb.getGitLabId(leader);
    gitlabService.addMember(groupGitLabId, leaderGitlabId, GitlabAccessLevel.Owner);
    gdb.addGroup(groupGitLabId, name, leader);
    gdb.addMember(leader, name);

    addMembers(name, members);

    GroupProjectService groupProjectService = new GroupProjectService();
    groupProjectService.createGroupProjectV2(name, projectName, projectType);

    return new ResponseEntity<Object>(headers, HttpStatus.OK);
  }

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
    //

    JSONObject jsonObject = getGroupInfo(name);
    return new ResponseEntity<Object>(jsonObject, headers, HttpStatus.OK);
  }

  public JSONObject getGroupInfo(String name){

    Group group = gdb.getGroup(name);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    JSONArray projectList = new JSONArray();
    List<GroupProject> groupProjects = group.getProjects();
    for(GroupProject temp: groupProjects) {
      JSONObject project = new JSONObject();
      project.put("id", temp.getId());
      project.put("name", temp.getName());

      if (temp.getReleaseTime() != null) {
        String releaseTime = dateFormat.format(temp.getReleaseTime());
        project.put("releaseTime", releaseTime);
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

    return jsonObject;
  }

  @CrossOrigin(origins = "*")
  @DeleteMapping(path = "/{name}")
  public ResponseEntity<Object> removeGroup(@PathVariable("name") String name) {


    // remove gitlab
    int gitlabId = gdb.getGitlabId(name);
    gitlabService.removeGroup(gitlabId);

    // remove Jenkins
    JenkinsService js = JenkinsService.getInstance();
    List<String> projectNames = pdb.getProjectNames(name);
    for (String projectName : projectNames) {
      String jobName = js.getJobName(name, projectName);
      js.deleteJob(jobName);
    }

    // remove db
    gdb.removeGroup(name);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{name}/members")
  public ResponseEntity<Object> addMembers(
          @PathVariable("name") String name, @RequestParam("members") List<String> members) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

    int groupGitLabId = gdb.getGitlabId(name);
    for (String member : members) {
      int gitlabId = udb.getGitLabId(member);
      gitlabService.addMember(groupGitLabId, gitlabId, GitlabAccessLevel.Master);
      gdb.addMember(member, name);
    }
    return new ResponseEntity<Object>(headers, HttpStatus.OK);
  }

  /**
   * get all commit result.
   *
   * @return hw, color, commit
   */
  @GetMapping("")
  public ResponseEntity<Object> getAllGroup() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    //

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

  /**
   * update team leader
   *
   * @param name group name
   * @param leader leader username
   * @return response
   */
  @CrossOrigin(origins = "*")
  @PutMapping("/{name}/members/{username}")
  public ResponseEntity<Object> updateLeader(
      @PathVariable("name") String name, @PathVariable("username") String leader) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");


    int groupGitLabId = gdb.getGitlabId(name);
    // update newLeader's AccessLevel to owner
    int leaderGitlabId = udb.getGitLabId(leader);
    gitlabService.updateMemberAccessLevel(groupGitLabId, leaderGitlabId, GitlabAccessLevel.Owner);

    String currentLeader = gdb.getLeader(name);
    int currentLeaderGitlabId = udb.getGitLabId(currentLeader);
    // update currentLeader's AccessLevel to master
    gitlabService.updateMemberAccessLevel(groupGitLabId, currentLeaderGitlabId, GitlabAccessLevel.Master);
    // update db.group leader
    gdb.updateLeader(name, leader);

    JSONObject result = new JSONObject();
    result.put("status", "success");

    return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
  }

  /**
   * remove members
   *
   * @param name group name
   * @param member member
   * @return response
   */
  @CrossOrigin(origins = "*")
  @DeleteMapping("/{name}/members/{username}")
  public ResponseEntity<Object> removeMember(
      @PathVariable("name") String name,
      @PathVariable("username") String member) {
    try{
        int groupGitLabId = gdb.getGitlabId(name);
        int gitlabId = udb.getGitLabId(member);
        gitlabService.removeGroupMember(groupGitLabId, gitlabId);
        gdb.removeMember(name, member);

        return new ResponseEntity<Object>(HttpStatus.OK);
      } catch (Exception e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

  }

}
