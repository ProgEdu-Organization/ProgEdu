package jenkins;

import java.util.List;

import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.service.ProjectService;

public class JenkinsApiTest {
  JenkinsApi jenkins = JenkinsApi.getInstance();
  Conn conn = Conn.getInstance();
  List<GitlabUser> users = conn.getUsers();

  public static void main(String[] args) {
    ProjectService ps2 = new ProjectService();
    String tempDir = System.getProperty("java.io.tmpdir");
    String uploadDir = tempDir + "/uploads/";
    String projectName = "OOP-HW2";
    String command = "rm -rf " + projectName;
    ps2.execLinuxCommandInFile(command, uploadDir);
  }
}