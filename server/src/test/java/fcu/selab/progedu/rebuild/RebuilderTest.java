package fcu.selab.progedu.rebuild;

import org.junit.Test;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.utils.Linux;

public class RebuilderTest {
  GitlabService gs = GitlabService.getInstance();
  Linux linux = Linux.getInstance();
  JenkinsService js = JenkinsService.getInstance();
  ProjectDbService pdb = ProjectDbService.getInstance();
  Rebuilder rebuilder = Rebuilder.getInstance();
  String groupName = "group5";
  String projectName = "project5";

  @Test
  public void test() throws InterruptedException {
    String gitlabUrl = "http://140.134.26.65:47777";
    String jenkinsUrl = "http://140.134.26.65:48888";
    String dbUrl = "140.134.26.65:41003";
    String jobName = js.getJobName(groupName, projectName);
    CommitRecord cr = pdb.getCommitResult(pdb.getPgid(groupName, projectName, dbUrl), dbUrl);
    String filePath = gs.cloneProject(groupName, projectName, gitlabUrl);
//    deleteBranch(filePath);
//    rebuilder.createGroupProject(groupName, projectName);
//    Thread.sleep(10000);

    for (int num = 2; num <= cr.getNumber(); num++) {
      String consoleText = js.getConsole(jobName, num, jenkinsUrl);
      String revision = rebuilder.extractRevision(consoleText);
      System.out.println(num + "\t: " + revision);
      pushProject(groupName, projectName, filePath, revision);
      Thread.sleep(20000);
    }

  }

  public void pushProject(String groupName, String projectName, String filePath, String revision) {
    Linux linux = new Linux();
    String url = gs.getProjectUrl(groupName, projectName);
//    String branch = " master";
    String checkoutCommand = "git checkout -B rebuild " + revision;
    linux.execLinuxCommandInFile(checkoutCommand, filePath);

    String pushCommand = "git push " + url;
    linux.execLinuxCommandInFile(pushCommand, filePath);
  }
}
