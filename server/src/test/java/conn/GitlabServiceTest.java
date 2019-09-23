package conn;

import java.io.IOException;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class GitlabServiceTest {

  public static void main(String[] args) throws LoadConfigFailureException, IOException {
    // TODO Auto-generated method stub
    GitlabConfig gitData = GitlabConfig.getInstance();
    String hostUrl = gitData.getGitlabHostUrl();
    String apiToken = gitData.getGitlabApiToken();
    GitlabAPI api = GitlabAPI.connect(hostUrl, apiToken);

    GitlabService gs = GitlabService.getInstance();
    GitlabProject project = api.getProject(20);
    gs.setGitlabWebhook(project);
  }

}
