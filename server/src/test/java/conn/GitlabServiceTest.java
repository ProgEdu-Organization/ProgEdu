package conn;

import javax.ws.rs.core.Response;

import org.junit.Test;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.service.GroupProjectContributionService;

public class GitlabServiceTest {
  GitlabService gitlab = GitlabService.getInstance();
  String groupName = "group5";
  String projectName = "project5";

  @Test
  public void m1() {
    GroupProjectContributionService gpcs = new GroupProjectContributionService();
    Response r = gpcs.getLineOfCode(groupName, projectName);
    System.out.println(r.getEntity());

  }

}
