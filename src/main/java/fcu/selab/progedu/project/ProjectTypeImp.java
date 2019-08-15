package fcu.selab.progedu.project;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.status.StatusFactorySelector;

public abstract class ProjectTypeImp implements ProjectType {

  /**
   * create Jenkins job
   * 
   * @param username    username
   * @param projectName project name
   */
  @Override
  public void createJenkinsJob(String username, String projectName) {
    JenkinsService jenkins = JenkinsService.getInstance();
    JenkinsConfig jenkinsData = JenkinsConfig.getInstance();
    try {
      String jenkinsJobConfigPath = this.getClass()
          .getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();
      String jobName = username + "_" + projectName;
      String jenkinsCrumb = jenkins.getCrumb(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword());
      createJenkinsJobConfig(username, projectName);
      jenkins.createJob(jobName, jenkinsCrumb, jenkinsJobConfigPath);
      jenkins.buildJob(jobName, jenkinsCrumb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Status getStatus(String statusType) {
    return getStatusFactory().getStatus(statusType);
  }

  @Override
  public StatusFactory getStatusFactory() {
    return StatusFactorySelector.getStatusFactory(getProjectType());
  }
}
