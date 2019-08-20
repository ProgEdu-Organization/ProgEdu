package jenkins;

import java.util.List;

import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.utils.Linux;

public class JenkinsApiTest {
  JenkinsApi jenkins = JenkinsApi.getInstance();
  GitlabService conn = GitlabService.getInstance();
  List<GitlabUser> users = conn.getUsers();

  public static void main(String[] args) {
    Linux linuxApi = new Linux();
    String tempDir = System.getProperty("java.io.tmpdir");
    String uploadDir = tempDir + "/uploads/";
    String projectName = "OOP-HW2";
    String command = "rm -rf " + projectName;
    linuxApi.execLinuxCommandInFile(command, uploadDir);
  }
}