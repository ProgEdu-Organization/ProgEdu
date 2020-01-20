package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gitlab.api.models.GitlabCommit;
import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.CommitStats;
import fcu.selab.progedu.data.Committer;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;

@Path("groups/{groupName}/projects/{projectName}/contribution")
public class GroupProjectContributionService {
  private GitlabService gitlab = GitlabService.getInstance();
  private ProjectDbService pdb = ProjectDbService.getInstance();
  private GroupDbService gdb = GroupDbService.getInstance();

  /**
   * get line of code
   * 
   * @param groupName   group name
   * @param projectName project name
   * @return JSON
   */
  @GET
  @Path("/loc")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLineOfCode(@PathParam("groupName") String groupName,
      @PathParam("projectName") String projectName) {
    int id = gitlab.getProject(groupName, projectName).getId();
    List<Committer> committers = new ArrayList<>();

    for (GitlabCommit gitlabCommit : gitlab.getAllCommits(id)) {
      boolean isMatched = false;
      String commitHash = gitlabCommit.getId();
      String committerName = gitlabCommit.getAuthorName();
      Date time = gitlabCommit.getCreatedAt();

      JSONObject stats = gitlab.getCommit(id, commitHash).getJSONObject("stats");

      int additions = stats.getInt("additions");
      int deletions = stats.getInt("deletions");
      CommitStats commitStats = new CommitStats(time, additions, deletions);

      for (Committer committer : committers) {
        if (committerName.equals(committer.getCommitter())) {
          committer.addCommitStats(commitStats);
          isMatched = true;
          break;
        }
      }
      if (!isMatched) {
        Committer committer = new Committer(committerName);
        committer.addCommitStats(commitStats);
        committers.add(committer);
      }

    }
    JSONArray array = new JSONArray();
    for (Committer commit : committers) {
      array.put(commit.toJson());
    }
    return Response.ok(array.toString()).build();
  }

  /**
   * get commit status
   * 
   * @param groupName   group name
   * @param projectName project name
   * @return JSON
   */
  @GET
  @Path("/status")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitStatus(@PathParam("groupName") String groupName,
      @PathParam("projectName") String projectName) {
    int pgid = pdb.getPgid(groupName, projectName);
    Group group = gdb.getGroup(groupName);
    List<User> members = group.getMembers();
    JSONArray array = new JSONArray();
    for (User member : members) {
      JSONObject committer = new JSONObject();
      committer.put("name", member.getName());
      committer.put("commits", new JSONArray());
      array.put(committer);
    }

    List<CommitRecord> commitRecords = pdb.getCommitRecords(pgid);
    for (CommitRecord commitRecord : commitRecords) {
      for (int index = 0; index < array.length(); index++) {
        JSONObject committer = array.getJSONObject(index);

        if (committer.getString("name").equals(commitRecord.getCommitter())) {
          JSONObject commit = new JSONObject();
          commit.put("time", commitRecord.getTime());
          commit.put("status", commitRecord.getStatus().getType());
          committer.getJSONArray("commits").put(commit);
        }

      }
    }
    return Response.ok(array.toString()).build();
  }
}
