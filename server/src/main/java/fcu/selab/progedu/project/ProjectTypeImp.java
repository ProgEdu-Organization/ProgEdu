package fcu.selab.progedu.project;

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

    String jenkinsJobConfigPath = this.getClass()
        .getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();
    String jobName = jenkins.getJobName(username, projectName);
    createJenkinsJobConfig(username, projectName);
    jenkins.createJob(jobName, jenkinsJobConfigPath);
    jenkins.buildJob(jobName);

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
