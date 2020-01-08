package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Commit {
  private String committer;
  private List<CommitStats> commits;

  public Commit(String committer) {
    this.commits = new ArrayList<CommitStats>();
    this.committer = committer;
  }

  public JSONObject toJSON() {
    JSONArray array = new JSONArray();
    JSONObject object = new JSONObject();
    for (CommitStats commit : commits) {
      array.put(commit.toJSON());
    }
    object.put("committer", committer);
    object.put("commits", array);

    return object;
  }

  public String getCommitter() {
    return this.committer;
  }

  public void addCommitStats(CommitStats commitStats) {
    this.commits.add(commitStats);
  }
}
