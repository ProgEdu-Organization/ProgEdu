package fcu.selab.progedu.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroup;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.UserDbService;

@Path("group/")
public class GroupService {

  GitlabService gitlabService = GitlabService.getInstance();
  UserService userService = new UserService();
  GroupDbService gdb = GroupDbService.getInstance();
  UserDbService udb = UserDbService.getInstance();
  AssignmentService projectService = new AssignmentService();

  /**
   * create gitlab group
   *
   * @param name    group name
   * @param leader  the username of team leader
   * @param members the members of group
   */
  @POST
  @Path("create")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createGroup(@FormParam("name") String name, @FormParam("leader") String leader,
      @FormParam("member") List<String> members) {
    GitlabGroup gitlabGroup = gitlabService.createGroup(name);
    int groupGitLabId = gitlabGroup.getId();
    members.remove(leader);
    int leaderGitlabId = udb.getGitLabId(leader);
    gitlabService.addMember(groupGitLabId, leaderGitlabId, GitlabAccessLevel.Owner);
    gdb.addGroup(groupGitLabId, name, leader);
    gdb.addMember(leader, name);

    for (String member : members) {
      int gitlabId = udb.getGitLabId(member);
      gitlabService.addMember(groupGitLabId, gitlabId, GitlabAccessLevel.Developer);
      gdb.addMember(member, name);
    }

    return Response.ok().build();
  }

}
