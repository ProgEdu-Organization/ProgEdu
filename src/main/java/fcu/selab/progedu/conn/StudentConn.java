package fcu.selab.progedu.conn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class StudentConn {
  GitlabConfig gitData = GitlabConfig.getInstance();

  String privateToken;
  private GitlabUser user = new GitlabUser();
  private String hostUrl;
  private TokenType tokenType = TokenType.PRIVATE_TOKEN;
  private AuthMethod authMethod = AuthMethod.URL_PARAMETER;
  private GitlabAPI gitlab;

  /**
   * Constructor
   * 
   * @param privateToken The student private token from gitlab
   * @throws LoadConfigFailureException on properties call error
   * @throws IOException                on gitlab get user error
   */
  public StudentConn(String privateToken) {
    this.privateToken = privateToken;
    try {
      hostUrl = gitData.getGitlabHostUrl();
      gitlab = GitlabAPI.connect(hostUrl, privateToken, tokenType, authMethod);
      getUser();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  /**
   * get gitlab user
   * 
   * @return gitlab user
   */
  public GitlabUser getUser() {
    try {
      this.user = gitlab.getUser();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return user;
  }

  public String getUsername() {
    return user.getUsername();
  }

  /**
   * Get user's projects
   * 
   * @return list of projects
   * @throws IOException on gitlab get projects error
   */
  public List<GitlabProject> getProject() {
    List<GitlabProject> project = new ArrayList<>();
    try {
      project = gitlab.getProjects();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return project;
  }

  /**
   * get gitlab project by project id
   * 
   * @param projectId gitlab project id
   * @return gitlab project
   */
  public GitlabProject getProjectById(int projectId) {
    GitlabProject project = new GitlabProject();
    try {
      project = gitlab.getProject(projectId);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return project;
  }

  /**
   * Get gitlab project commit counts
   * 
   * @param projectId The gitlab project id
   * @return commit counts
   * @throws IOException on gitlab call error
   */
  public int getAllCommitsCounts(int projectId) throws IOException {
    int count = 0;
    if (!gitlab.getAllCommits(projectId).isEmpty()) {
      count = gitlab.getAllCommits(projectId).size();
    }
    return count;
  }

  /**
   * Get all gitlab project's commits
   * 
   * @param projectId The gitlab project id
   * @return list of commits
   * @throws IOException on gitlab call error
   */
  public List<GitlabCommit> getAllCommits(int projectId) throws IOException {
    return gitlab.getAllCommits(projectId);
  }
}