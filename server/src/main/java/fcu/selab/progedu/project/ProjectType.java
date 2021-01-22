package fcu.selab.progedu.project;

import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.status.StatusFactorySelector;

public abstract class ProjectType {

  /**
   * create Jenkins job
   * 
   * @param username    username
   * @param projectName project name
   */
  public void createJenkinsJob(String username, String projectName) {

    // Todo 這會改變 "/jenkins/" + getJenkinsJobConfigSample() 這個設定檔
    createJenkinsJobConfig(username, projectName);


    JenkinsService jenkinsService = JenkinsService.getInstance();

    String jobName = jenkinsService.getJobName(username, projectName);
    String jenkinsJobConfigPath = getJenkinsJobConfigPath();

    jenkinsService.createJob(jobName, jenkinsJobConfigPath);
    jenkinsService.buildJob(jobName);
  }

  // Todo 模板方法只用到這個好處
  public abstract void createJenkinsJobConfig(String username, String projectName);


  public Status getStatus(String statusType) {
    return getStatusFactory().getStatus(statusType);
  }

  public StatusFactory getStatusFactory() {
    return StatusFactorySelector.getStatusFactory(getProjectType());
  }

  public abstract ProjectTypeEnum getProjectType();

  public abstract String getSampleTemplate();

  // Todo 模板方法只用到這個好處
  public abstract String getJenkinsJobConfigPath();

  public abstract StatusEnum checkStatusType(int num, String username, String assignmentName);

}
