package fcu.selab.progedu.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Committer {
  private String name;
  private List<CommitStats> commits;
  private List<String> status;

  /**
   * constructor
   * 
   * @param name committer's name
   */
  public Committer(String name) {
    this.commits = new ArrayList<CommitStats>();
    this.name = name;
  }

  /**
   * transform java object to JSON
   * 
   * @return JSON
   */
  public JSONObject toJson() {
    JSONArray array = new JSONArray();
    JSONObject object = new JSONObject();
    for (CommitStats commit : commits) {
      array.put(commit.toJson());
    }
    object.put("committer", name);
    object.put("commits", array);

    return object;
  }

  public String getCommitter() {
    return this.name;
  }

  public void addCommitStats(CommitStats commitStats) {
    this.commits.add(commitStats);
  }

  public void addStatus(String status) {
    this.status.add(status);
  }
}
