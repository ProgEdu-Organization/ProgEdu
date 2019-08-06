package fcu.selab.progedu.conn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;
import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.GroupService;

public class Conn {
  private static Conn instance = new Conn();

  GitlabConfig gitData = GitlabConfig.getInstance();

  private String hostUrl;
  private String apiToken;
  private TokenType tokenType = TokenType.PRIVATE_TOKEN;
  private AuthMethod authMethod = AuthMethod.URL_PARAMETER;

  private GitlabAPI gitlab;

  private static UserDbManager dbManager = UserDbManager.getInstance();

  HttpConnect httpConn = HttpConnect.getInstance();

  private Conn() {
    try {
      hostUrl = gitData.getGitlabHostUrl();
      apiToken = gitData.getGitlabApiToken();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    gitlab = GitlabAPI.connect(hostUrl, apiToken, tokenType, authMethod);
  }

  public static Conn getInstance() {
    return instance;
  }

  /**
   * Get root session from Gitlab
   * 
   * @return root's session from Gitlab
   */
  public GitlabSession getRootSession() {
    GitlabSession rootSession = null;
    try {
      rootSession = GitlabAPI.connect(hostUrl, gitData.getGitlabRootUsername(),
          gitData.getGitlabRootPassword());
    } catch (IOException | LoadConfigFailureException e) {
      e.printStackTrace();
    }
    return rootSession;
  }

  /**
   * Get user session by username and password
   * 
   * @param userName
   *          Gitlab username
   * @param password
   *          Gitlab password
   * @return User's Session
   * @throws IOException
   *           on gitlab api call error
   */
  public GitlabSession getSession(String userName, String password) {
    GitlabSession userSession = null;
    try {
      userSession = GitlabAPI.connect(hostUrl, userName, password);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userSession;
  }

  public String getToken(GitlabSession session) {
    return session.getPrivateToken();
  }

  /**
   * Get a new GitlabApi by user token
   * 
   * @param token
   *          A token from user
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
   * @param user
   *          A user from database
   * @return The project list of user
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabProject> getProject(User user) {
    GitlabUser gitlabUser = new GitlabUser();
    List<GitlabProject> projects = new ArrayList<>();
    try {
      gitlabUser.setId(user.getGitLabId());
      projects = gitlab.getProjectsViaSudo(gitlabUser);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return projects;
  }

  /**
   * Get a list of project by GitlabUser
   * 
   * @param user
   *          of gitlab
   * @return The project list of user
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabProject> getAssignment(GitlabUser user) {
    List<GitlabProject> projects = new ArrayList<>();
    try {
      projects = gitlab.getProjectsViaSudo(user);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return projects;
  }

  /**
   * get project all commit information
   * 
   * @param projectId
   *          project id
   * @return commits list
   */
  public List<GitlabCommit> getProjectCommits(int projectId) {
    List<GitlabCommit> commits = new ArrayList<>();
    try {
      commits = gitlab.getAllCommits(projectId);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return commits;
  }

  /**
   * get project all commit information
   * 
   * @param groupName
   *          groupName
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
      e.printStackTrace();
    }
    return commits;
  }

  /**
   * Get all user's list of projects
   * 
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabProject> getAllProjects() {
    List<GitlabProject> projects = new ArrayList<>();
    try {
      projects = gitlab.getAllProjects();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return projects;
  }

  /**
   * Get a gitlab user
   * 
   * @throws IOException
   *           on gitlab api call error
   */
  public GitlabUser getUser() {
    GitlabUser gitlabUser = new GitlabUser();
    try {
      gitlabUser = gitlab.getUser();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return gitlabUser;
  }

  /**
   * Get a gitlab user by user id
   * 
   * @param userId
   *          user id
   * @return gitlab user
   */
  public GitlabUser getUserById(int userId) {
    GitlabUser gitlabUser = new GitlabUser();
    try {
      gitlabUser = gitlab.getUser(userId);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return gitlabUser;
  }

  /**
   * Get all user from Gitlab
   * 
   * @return a list of users
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabUser> getUsers() {
    List<GitlabUser> users = new ArrayList<>();
    try {
      users = gitlab.getUsers();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return users;
  }

  /**
   * get Gitlab user id via sudo
   * 
   * @param userName
   *          user name
   * @return Gitlab user
   */
  public GitlabUser getUserViaSudo(String userName) {
    GitlabUser user = new GitlabUser();
    try {
      user = gitlab.getUserViaSudo(userName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * Get a private token by GitlabUser
   * 
   * @param user
   *          A Gitlab user
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
   * @throws IOException
   *           on gitlab api call error
   */
  public GitlabUser getRoot() {
    GitlabUser root = new GitlabUser();
    try {
      root = gitlab.getUser(1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }

  /**
   * Get all groups of Gitlab
   * 
   * @return a list of groups from Gitlab
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabGroup> getGroups() {
    List<GitlabGroup> groups = new ArrayList<>();
    try {
      groups = gitlab.getGroups();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return groups;
  }

  /**
   * Get a list of project from group
   * 
   * @param group
   *          A group form Gitlab
   * @return a list of project from group
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabProject> getGroupProject(GitlabGroup group) {
    List<GitlabProject> projects = new ArrayList<>();
    try {
      projects = gitlab.getGroupProjects(group);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return projects;
  }

  /**
   * Get a list of group's member
   * 
   * @param group
   *          a group from Gitlab
   * @return a list of group's member
   * @throws IOException
   *           on gitlab api call error
   */
  public List<GitlabGroupMember> getGroupMembers(GitlabGroup group) {
    List<GitlabGroupMember> groupMembers = new ArrayList<>();
    try {
      groupMembers = gitlab.getGroupMembers(group);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return groupMembers;
  }

  /**
   * get Gitlab group id with group name
   * 
   * @param groupName
   *          group name
   * @return group id
   */
  public GitlabGroup getGitlabGroup(String groupName) {
    GitlabGroup group = new GitlabGroup();
    try {
      group = gitlab.getGroup(groupName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return group;
  }

  public String getGroupUrl(GitlabGroup group) {
    return hostUrl + "/groups/" + group.getName();
  }

  /**
   * createUserProject(Integer userId, String name, String description, String
   * defaultBranch, Boolean issuesEnabled, Boolean wallEnabled, Boolean
   * mergeRequestsEnabled, Boolean wikiEnabled, Boolean snippetsEnabled, Boolean
   * publik, Integer visibilityLevel, String importUrl)
   * 
   * @throws IOException
   *           on gitlab api call error
   */
  public GitlabProject createPrivateProject(int userId, String proName, String proUrl)
      throws IOException {
    GitlabProject project = gitlab.createUserProject(userId, proName, null, null, null, null, null,
        null, null, null, null, proUrl);
    return project;
  }

  /**
   * Create a new User
   * 
   * @param email
   *          User email
   * @param password
   *          User password
   * @param userName
   *          User name
   * @param fullName
   *          Full name
   * @return true or false
   * @throws LoadConfigFailureException
   *           on a instance call error
   * @throws IOException
   *           on gitlab api call error
   */
  public boolean createUser(String email, String password, String userName, String fullName) {
    boolean isSuccess = true;
    GitlabUser user = new GitlabUser();
    try {
      user = gitlab.createUser(email, password, userName, fullName, "", "", "", "", 10, null, null,
          "", false, true, null);
    } catch (IOException e) {
      isSuccess = false;
      e.printStackTrace();
    }
    String privateToken = instance.getSession(userName, password).getPrivateToken();
    user.setPrivateToken(privateToken);
    dbManager.addUser(user);
    return isSuccess;
  }

//  /**
//   * Creates a Group
//   *
//   * @param groupName The name of the group. The name will also be used as the
//   *                  path of the group.
//   * @param owner     the owner of the group.
//   * @return The GitLab Group
//   */
//  public GitlabGroup createGroup(String groupName, GitlabUser owner) {
//    try {
//      return gitlab.createGroupViaSudo(groupName, groupName, owner);
//    } catch (IOException e) {
//      System.out.println(e);
//    }
//    return null;
//  }

  /**
   * Creates a Group
   *
   * @param name
   *          The name of the group. The name will also be used as the path of
   *          the group.
   * @return The GitLab Group
   * @throws IOException
   *           on gitlab api call error
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
   * @param group       group
   * @param projectName project name
   * @throws IOException IOException
   */
  public GitlabProject createGroupProject(Group group, String projectName) throws IOException {
    GroupService gs = new GroupService();
    int masterId = gs.getUserIdByUsername(group.getLeaderUsername());
    GitlabProject project = createPrivateProject(masterId, projectName, null);
    httpConn.transferProjectToGroup(getGitlabGroup(group.getGroupName()).getId(), project.getId());
    return project;
  }

  /**
   * Add a member to group
   * 
   * @param groupId
   *          Group id
   * @param userId
   *          User id
   * @param level
   *          User level in group
   * @return true or false
   * @throws IOException
   *           on gitlab api call error
   */
  public boolean addMember(int groupId, int userId, int level) {
    GitlabAccessLevel accessLevel = null;
    if (level == 40) {
      accessLevel = GitlabAccessLevel.Master;
    } else if (level == 30) {
      accessLevel = GitlabAccessLevel.Developer;
    } else {
      accessLevel = GitlabAccessLevel.Guest;
    }

    try {
      gitlab.addGroupMember(groupId, userId, accessLevel);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * Create a root project
   * 
   * @param proName
   *          Project name
   * @return true or false
   * @throws IOException
   *           on gitlab api call error
   */
  public boolean createRootAssignment(String proName) {
    boolean isSuccess = false;
    try {
      gitlab.createUserProject(1, proName);
      isSuccess = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return isSuccess;
  }

  /**
   * Get commit counts from project
   * 
   * @param projectId
   *          Project id
   * @return a count of commit
   * @throws IOException
   *           on gitlab api call error
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
      e.printStackTrace();
    }
    return count;
  }

  /**
   * Get project commit
   * 
   * @param projectId
   *          project id
   * @return list of commit
   */
  public List<GitlabCommit> getAllCommits(int projectId) {
    List<GitlabCommit> lsCommits = new ArrayList<>();
    try {
      lsCommits = gitlab.getAllCommits(projectId);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lsCommits;
  }

  /**
   * Replace the project url
   * 
   * @param oldUrl
   *          The old url of project
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
   * @param userId
   *          user id
   */
  public void deleteUser(int userId) {
    try {
      gitlab.deleteUser(userId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * delete all gitlab projects
   */
  public void deleteAssignments(String name) {
    List<GitlabProject> projects = getAllProjects();
    for (GitlabProject project : projects) {
      try {
        if (project.getName().equals(name)) {
          gitlab.deleteProject(project.getId());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Update user password
   * 
   * @param password
   *          user new password
   */
  public void updateUserPassword(int userId, String password) {
    GitlabUser stuUser = new GitlabUser();
    try {
      stuUser = gitlab.getUser(userId);
      gitlab.updateUser(stuUser.getId(), stuUser.getEmail(), password, stuUser.getUsername(),
          stuUser.getName(), null, null, null, null, 20, null, null, null, false, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * get commits from gitlab project.
   * 
   * @param name
   *          project's name
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
      e.printStackTrace();
    }
    return committers;
  }

}
