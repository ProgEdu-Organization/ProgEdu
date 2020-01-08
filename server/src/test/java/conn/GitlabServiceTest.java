package conn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabCommit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.data.Commit;
import fcu.selab.progedu.data.CommitStats;

public class GitlabServiceTest {
  GitlabService gitlab = GitlabService.getInstance();
  GitlabAPI api = gitlab.getGitlabAPI();

  @Test
  public void m1() throws IOException {
    int id = 275;

//    JSONArray array = new JSONArray();
    List<String> committers = new ArrayList<>();
    committers.add("hoky");
    committers.add("Administrator");
    List<Commit> commits = new ArrayList<>();
    for (String committer : committers) {
      Commit commit = new Commit(committer);
      commits.add(commit);
    }

    List<GitlabCommit> gitlabCommits = api.getAllCommits(id);
    for (GitlabCommit gitlabCommit : gitlabCommits) {
      String commitHash = gitlabCommit.getId();
      String committer = gitlabCommit.getAuthorName();
      Date time = gitlabCommit.getCreatedAt();

      JSONObject stats = gitlab.getCommit(id, commitHash).getJSONObject("stats");

      int additions = stats.getInt("additions");
      int deletions = stats.getInt("deletions");
      CommitStats commitStats = new CommitStats(time, additions, deletions);

      for (Commit commit : commits) {
        if (committer.equals(commit.getCommitter())) {
          commit.addCommitStats(commitStats);
          break;
        }
      }
//      System.out.println(gitlabCommit.getAuthorName());
//      System.out.println(additions + ", " + deletions);
//      System.out.println(commitStats.toJSON() + "\n");

    }
    JSONArray array = new JSONArray();
    for (Commit commit : commits) {
      array.put(commit.toJSON());
    }
    System.out.println(array + "\n");

  }

}
