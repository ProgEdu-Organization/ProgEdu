package fcu.selab.progedu.project;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusFactory;

public abstract class ProjectTypeImp implements ProjectType {

  /**
   * create Jenkins job
   * 
   * @param projectName group
   */
  @Override
  public void createJenkinsJob(String username, String projectName) {
    JenkinsApi jenkins = JenkinsApi.getInstance();
    GitlabConfig gitlabConfig = GitlabConfig.getInstance();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
    try {
      // String proUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" +
      // projectName + ".git";
      String jenkinsCrumb = jenkins.getCrumb(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword());
      String filePath = createJenkinsJobConfiguration(username, projectName);
      JenkinsApi.postCreateJob(username, projectName, jenkinsCrumb, filePath);
      jenkins.buildJob(username, projectName, jenkinsCrumb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Status getStatus() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public StatusFactory getStatusFactory() {
    // TODO Auto-generated method stub
    return null;
  }
}
