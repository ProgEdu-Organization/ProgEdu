package fcu.selab.progedu.service;

import java.io.IOException;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.project.ProjectTypeEnum;
import fcu.selab.progedu.status.Status;

public interface IGroupProject {
  public String getJenkinsConfig();

  public void createJenkinsJobConfiguration(String filePath, String updateDbUrl, String userName,
      String projectName, String tomcatUrl);

  public void createJenkinsJob(String groupName, String projectName);

  public void createGitlabProject(Group group, String projectName)
      throws IOException, LoadConfigFailureException;

  public Status getStatus(String statusType);

  public ProjectTypeEnum getProjectType();
}
