package fcu.selab.progedu.service;

import fcu.selab.progedu.status.Status;

public interface IGroupProject {
  public String getJenkinsConfig();

  public void createJenkinsJobConfiguration(String filePath, String updateDbUrl, String userName,
      String projectName, String tomcatUrl);

  public void createJenkinsJob(String projectName);

  public void createGitlabProject(String projectName);

  public Status getStatus(String statusType);

  public AssignmentTypeEnum getProjectType();
}
