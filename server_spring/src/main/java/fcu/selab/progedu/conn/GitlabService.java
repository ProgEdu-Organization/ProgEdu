package fcu.selab.progedu.conn;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.service.GroupDbService;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.utils.Linux;

public class GitlabService {
  private static GitlabService instance = new GitlabService();

  GitlabConfig gitlabConfig = GitlabConfig.getInstance();
  GroupDbService gdb = GroupDbService.getInstance();
  private String hostUrl;
  private String rootUrl;
  private String apiToken;
  private TokenType tokenType = TokenType.PRIVATE_TOKEN;
  private AuthMethod authMethod = AuthMethod.URL_PARAMETER;
  private static final String API_NAMESPACE = "/api/v4";
  private GitlabAPI gitlab;
  private static final Logger LOGGER = LoggerFactory.getLogger(GitlabConfig.class);

  private GitlabService() {
    try {
      hostUrl = gitlabConfig.getGitlabHostUrl();
      rootUrl = gitlabConfig.getGitlabRootUrl();
      apiToken = gitlabConfig.getGitlabApiToken();
    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    gitlab = GitlabAPI.connect(hostUrl, apiToken, tokenType, authMethod);
  }

  public static GitlabService getInstance() {
    return instance;
  }

  public GitlabAPI getGitlab() {
    return gitlab;
  }

  /**
   * Get root session from Gitlab
   *
   * @param userGitlabId user Gitlab Id
   * @return token
   */
  public String getToken(int userGitlabId) {
    return getUserById(userGitlabId).getPrivateToken();
  }

  /**
   * Get a new GitlabApi by user token
   *
   * @param token A token from user
   * @return a new GitlabApi
   */
  public GitlabAPI getUserApi(String token) {
    GitlabAPI newUser;
    newUser = GitlabAPI.connect(hostUrl, token, tokenType, authMethod);
    return newUser;
  }

  /**
   * Get a list of project by user
   *
   * @param user A user from database
   * @return The project list of user
   */
  public List<GitlabProject> getProject(User user) {
    GitlabUser gitlabUser = new GitlabUser();
    List<GitlabProject> projects = new ArrayList<>();
    try {
      gitlabUser.setId(user.getGitLabId());
      projects = gitlab.getProjectsViaSudo(gitlabUser);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return projects;
  }

  /**
   * Get a list of project by GitlabUser
   *
   * @param user of gitlab
   * @return The project list of user
   */
  public List<GitlabProject> getProject(GitlabUser user) {
    List<GitlabProject> projects = new ArrayList<>();
    try {
      projects = gitlab.getProjectsViaSudo(user);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return projects;
  }

  /**
   * get gitlab project by id
   *
   * @param id gitlab project id
   * @return gitlab project
   */
  public GitlabProject getProject(int id) {
    GitlabProject project = null;
    try {
      project = gitlab.getProject(id);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return project;
  }

  /**
   * get gitlab project by username and projectName
   *
   * @param username username
   * @param proName  proName
   * @return gitlabProject
   */
  public GitlabProject getProject(String username, String proName) {
    GitlabProject gitlabProject = null;
    try {
      gitlabProject = gitlab.getProject(username, proName);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return gitlabProject;
  }

  /**
   * get project all commit information
   *
   * @param projectId project id
   * @return commits list
   */
  public List<GitlabCommit> getProjectCommits(int projectId) {
    List<GitlabCommit> commits = new ArrayList<>();
    try {
      commits = gitlab.getAllCommits(projectId);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return commits;
  }

  /**
   * Todo 這預設限定了一個Group 只有一個 project
   * get project all commit information
   *
   * @param groupName groupName
   * @return commits list
   */
  public List<GitlabCommit> getProjectCommits(String groupName) {
    List<GitlabCommit> commits = new ArrayList<>();
    GitlabGroup group = getGitlabGroup(groupName);
    // get the first project of group
    GitlabProject project = getGroupProject(group).get(0);
    try {
      commits = gitlab.getAllCommits(project.getId());
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return commits;
  }

  /**
   * Get all user's list of projects
   *
   * @return projects
   */
  public List<GitlabProject> getAllProjects() {
    return gitlab.getAllProjects();
  }

  /**
   * Get a gitlab user
   *
   * @return gitlabUser
   */
  public GitlabUser getUser() {
    GitlabUser gitlabUser = new GitlabUser();
    try {
      gitlabUser = gitlab.getUser();
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return gitlabUser;
  }

  /**
   * Get a gitlab user by user id
   *
   * @param userId user id
   * @return gitlab user
   */
  public GitlabUser getUserById(int userId) {
    GitlabUser gitlabUser = new GitlabUser();
    try {
      gitlabUser = gitlab.getUser(userId);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return gitlabUser;
  }

  /**
   * Get all user from Gitlab
   *
   * @return a list of users
   */
  public List<GitlabUser> getUsers() {
    return gitlab.getUsers();
  }

  /**
   * get Gitlab user id via sudo
   *
   * @param userName user name
   * @return Gitlab user
   */
  public GitlabUser getUserViaSudo(String userName) {
    GitlabUser user = new GitlabUser();
    try {
      user = gitlab.getUserViaSudo(userName);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return user;
  }

  /**
   * Get a private token by GitlabUser
   *
   * @param user A Gitlab user
   * @return a private token of user
   */
  public String getPrivateToken(GitlabUser user) {
    String privateToken;
    privateToken = user.getPrivateToken();
    return privateToken;
  }

  public int getProjectsLength(List<GitlabProject> projects) {
    return projects.size();
  }

  /**
   * Get GitlabUser of Root
   *
   * @return GitlabUser of Root
   */
  public GitlabUser getRoot() {
    GitlabUser root = new GitlabUser();
    try {
      root = gitlab.getUser(1);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return root;
  }

  /**
   * Get all groups of Gitlab
   *
   * @return a list of groups from Gitlab
   */
  public List<GitlabGroup> getGroups() {
    List<GitlabGroup> groups = new ArrayList<>();
    try {
      groups = gitlab.getGroups();
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return groups;
  }

  /**ProjectCommitRecordDbManager
   * Get a list of project from group
   *
   * @param group A group form Gitlab
   * @return a list of project from group
   */
  public List<GitlabProject> getGroupProject(GitlabGroup group) {
    return gitlab.getGroupProjects(group);
  }

  /**
   * Get a list of group's member
   *
   * @param group a group from Gitlab
   * @return a list of group's member
   */
  public List<GitlabGroupMember> getGroupMembers(GitlabGroup group) {
    return gitlab.getGroupMembers(group);
  }

  /**
   * get Gitlab group id with group name
   *
   * @param groupName group name
   * @return group id
   */
  public GitlabGroup getGitlabGroup(String groupName) {
    GitlabGroup group = new GitlabGroup();
    try {
      group = gitlab.getGroup(groupName);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return group;
  }

  public String getGroupUrl(GitlabGroup group) {
    return hostUrl + API_NAMESPACE + "/groups/" + group.getName();
  }

  /**
   * @param username     username
   * @param proName      proName
   * @param projectOwner project owner name
   * @return project
   * @throws IOException on gitlab api call error
   */
  public GitlabProject createPrivateProject(String username, String proName, String projectOwner) {
    GitlabProject gitlabProject = new GitlabProject();
    try {
      gitlabProject = gitlab.createFork(username, getProject(projectOwner, proName));
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return gitlabProject;
  }

  /**
   * Create a new User
   *
   * @param email    User email
   * @param password User password
   * @param username User name
   * @param name     Full name
   * @return true or false
   * @throws IOException on gitlab api call error
   */
  public GitlabUser createUser(String email, String password, String username, String name)
      throws IOException {
    GitlabUser user = new GitlabUser();
    user = gitlab.createUser(email, password, username, name, "", "", "", "", 10, null, null, "",
        false, true, true, false);

    String privateToken = getToken(user.getId());
    user.setPrivateToken(privateToken);
    return user;
  }

  /**
   * Creates a Group
   *
   * @param groupName The name of the group. The name will also be used as the
   *                  path of the group.
   * @return The GitLab Group
   */
  public GitlabGroup createGroup(String groupName) {
    try {
      return gitlab.createGroup(groupName);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }

  /**
   * transfer project into group
   *
   * @param groupName   group name
   * @param projectName project name
   * @return projectId
   * @throws IOException IOException
   */
  public int createGroupProject(String groupName, String projectName) throws IOException {
    int groupGitlabId = gdb.getGitlabId(groupName);
    GitlabProject project = createRootProject(projectName);
    int projectId = project.getId();
    transferProjectToGroupProject(groupGitlabId, projectId);
    return projectId;
  }

  /**
   * Add a member to group
   *
   * @param groupId     gitlab group id
   * @param userId      gitlab user id
   * @param accessLevel access level in group
   */
  public void addMember(int groupId, int userId, GitlabAccessLevel accessLevel) {
    try {
      gitlab.addGroupMember(groupId, userId, accessLevel);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * update member access level
   *
   * @param groupId     Group id
   * @param userId      User id
   * @param accessLevel access level in group
   */
  public void updateMemberAccessLevel(int groupId, int userId, GitlabAccessLevel accessLevel) {
    // PUT /groups/:id/members/:user_id
    // for example,
    // http://localhost:80/api/v4/groups/38/members/2?access_level=50
    String api = hostUrl + API_NAMESPACE + "/groups/" + groupId + "/members/" + userId;

    HttpPut put = new HttpPut(api);
    put.addHeader("PRIVATE-TOKEN", apiToken);
    // Request parameters
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("access_level", Integer.toString(accessLevel.accessValue)));
    try {
      put.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    try {
      HttpClients.createDefault().execute(put);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Create a root project
   *
   * @param proName Project name
   * @return GitlabProject
   */
  public GitlabProject createRootProject(String proName) {
    GitlabProject project = null;
    try {
      project = gitlab.createUserProject(1, proName);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return project;
  }

  /**
   * Delete a root project
   *
   * @param proName Project name
   * @return true or false
   */
  public Boolean deleteRootProject(String proName) {
    try {
      GitlabProject gitlabProject = gitlab.getProject("root", proName);
      gitlab.deleteProject(gitlabProject.getId());
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return false;
    }
    return true;
  }


  /**
   * Get commit counts from project
   *
   * @param projectId Project id
   * @return a count of commit
   */
  public int getAllCommitsCounts(int projectId) {
    int count = 0;
    List<GitlabCommit> lsCommits = new ArrayList<>();
    try {
      if (!gitlab.getAllCommits(projectId).isEmpty()) {
        lsCommits = gitlab.getAllCommits(projectId);
        count = lsCommits.size();
      } else {
        count = 0;
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return count;
  }

  /**
   * Get project commit
   *
   * @param projectId project id
   * @return list of commit
   */
  public List<GitlabCommit> getAllCommits(int projectId) {
    List<GitlabCommit> lsCommits = new ArrayList<>();
    try {
      lsCommits = gitlab.getAllCommits(projectId);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return lsCommits;
  }

  /**
   * Replace the project url
   *
   * @param oldUrl The old url of project
   * @return the new url
   */
  public String getReplaceUrl(String oldUrl) {
    String oldStr = oldUrl.substring(0, 19);
    oldUrl = oldUrl.replace(oldStr, hostUrl);
    return oldUrl;
  }

  /**
   * Delete the target user
   *
   * @param userId user id
   */
  public void deleteUser(int userId) {
    try {
      gitlab.deleteUser(userId);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * delete all gitlab projects
   *
   * @param name name
   */
  public void deleteProjects(String name) {
    List<GitlabProject> projects = getAllProjects();
    for (GitlabProject project : projects) {
      try {
        if (project.getName().equals(name)) {
          gitlab.deleteProject(project.getId());
        }
      } catch (IOException e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }
    }
  }

  /**
   * Update user password
   *
   * @param userId   user id
   * @param password user new password
   */
  public void updateUserPassword(int userId, String password) {
    GitlabUser user = new GitlabUser();
    try {
      user = gitlab.getUser(userId);
      gitlab.updateUser(user.getId(), user.getEmail(), password, user.getUsername(),
          user.getName(), null, null, null, null, 20, null, null,
          null, false, true, false);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get commits from gitlab project.
   *
   * @param name project's name
   * @return committers
   */
  public List<String> getAllCommitters(String name) {
    List<String> committers = new ArrayList<>();
    try {
      List<GitlabCommit> commits = gitlab
          .getAllCommits(gitlab.getGroupProjects(gitlab.getGroup(name)).get(0).getId());
      for (GitlabCommit commit : commits) {
        committers.add(commit.getAuthorName());
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return committers;
  }

  /**
   * Clone GitLab project
   * if not success clone, return ""
   *
   * @param username    project's (to do)
   * @param projectName project name
   * @return target path
   */
  public String cloneProject(String username, String projectName) {

    String targetPathString = System.getProperty("java.io.tmpdir") + "/uploads/" + projectName;
    Path targetPath = Paths.get(targetPathString);

    boolean isSuccess = cloneProject(username, projectName, targetPath);

    if (isSuccess) {
      return targetPath.toString();
    } else {
      return "";
    }
  }

  /**
   * clone project to not exist folder
   *
   * @param username    project's (to do)
   * @param projectName project name
   * @return boolean
   */
  public boolean cloneProject(String username, String projectName, Path targetPath) {

    if (Files.exists(targetPath)) { // is exist
      LOGGER.error("In cloneProject(), " + targetPath.toString() + " folder is exist.");
      return false;
    }

    GitlabProject gitlabProject = getProject(username, projectName);
    if (gitlabProject == null) {
      LOGGER.error("In cloneProject(), username: " + username + " projectName: "
              + projectName + " is not exist.");
      return false;
    }

    String repositoryUrl = rootUrl + "/" + username + "/" + projectName + ".git";
    String cloneCommand = "git clone " + repositoryUrl + " " + targetPath.toString();
    Linux linux = new Linux();
    linux.execLinuxCommand(cloneCommand);

    return true;
  }

  /**
   * (to do)
   *
   * @param cloneDirectoryPath (to do)
   */
  public void pushProject(String cloneDirectoryPath) {
    Linux linux = new Linux();
    String[] addCommand = {"git", "add", "."};
    linux.execLinuxCommandInFile(addCommand, cloneDirectoryPath);

    String[] commitCommand = {"git", "commit", "-m", "Instructor Commit"};
    linux.execLinuxCommandInFile(commitCommand, cloneDirectoryPath);

    String[] pushCommand = {"git", "push"};
    linux.execLinuxCommandInFile(pushCommand, cloneDirectoryPath);
  }

  /**
   * (to do)
   *
   * @param project (to do)
   * @throws IOException                (to do)
   * @throws LoadConfigFailureException (to do)
   */
  public void setGitlabWebhook(GitlabProject project, String jobName)
          throws IOException, LoadConfigFailureException {

    JenkinsConfig jenkinsConfig = JenkinsConfig.getInstance();
    String jenkinsJobUrl = jenkinsConfig.getJenkinsHostUrl() + "/project/" + jobName;

    // for example,
    // http://localhost:80/api/v4/projects/3149/hooks?url=http://localhost:8888/project/{jobName}
    String gitlabWebhookApi = hostUrl + API_NAMESPACE + "/projects/" + project.getId() + "/hooks";

    HttpPost post = new HttpPost(gitlabWebhookApi);
    post.addHeader("PRIVATE-TOKEN", apiToken);

    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("url", jenkinsJobUrl));
    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    HttpClients.createDefault().execute(post);
  }

  /**
   * transfer project from root to group
   *
   * @param groupId   group id
   * @param projectId project id
   */
  public void transferProjectToGroupProject(int groupId, int projectId) {
    HttpClient client = new DefaultHttpClient();
    String url = "";
    try {
      url = hostUrl + API_NAMESPACE + "/groups/" + groupId + "/projects/"
          + projectId + "?private_token=" + apiToken;
      HttpPost post = new HttpPost(url);

      HttpResponse response = client.execute(post);
      HttpEntity resEntity = response.getEntity();

      if (resEntity != null) {
        String result = resEntity.toString();
        System.out.println("httppost build " + groupId + " , result : " + result);
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get gitlab project url
   *
   * @param username    username
   * @param projectName project name
   * @return gitlab project url
   */
  public String getProjectUrl(String username, String projectName) {
    return hostUrl + "/" + username + "/" + projectName + ".git";
  }

  /**
   * remove gitlab group
   *
   * @param id gitlab group id
   */
  public void removeGroup(int id) {
    try {
      gitlab.deleteGroup(id);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  /**
   * remove member from gitlab group
   *
   * @param groupId group gitlab id
   * @param userId  user gitlab id
   */
  public void removeGroupMember(int groupId, int userId) {
    try {
      gitlab.deleteGroupMember(groupId, userId);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

}
