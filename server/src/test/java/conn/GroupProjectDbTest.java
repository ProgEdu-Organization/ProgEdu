package conn;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.db.service.ProjectDbService;

public class GroupProjectDbTest {
  ProjectDbService pdb = ProjectDbService.getInstance();
  GroupDbService gdb = GroupDbService.getInstance();
  String groupName = "group4";
  String projectName = "project4";

  @Test
  public void m1() {

    int pgid = pdb.getPgid(groupName, projectName);
    Group group = gdb.getGroup(groupName);
    List<User> members = group.getMembers();
    JSONArray array = new JSONArray();
    for (User member : members) {
      JSONObject committer = new JSONObject();
      committer.put("name", member.getName());
      committer.put("commits", new JSONArray());
      array.put(committer);
//      System.out.println(member.getName());
    }

    System.out.println();
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
//      System.out.println(commitRecord.getCommitter());
    }
    System.out.println(array);
  }
}
