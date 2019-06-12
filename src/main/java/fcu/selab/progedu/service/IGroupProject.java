package fcu.selab.progedu.service;

import fcu.selab.progedu.data.Group;
import fcu.selab.progedu.status.Status;

public interface IGroupProject {
  public String getJenkinsConfig();

  public void modifyJenkinsJobConfiguration(String filePath, String updateDbUrl, String userName,
      String proName, String tomcatUrl);

  public void createJenkinsJob(Group group);

  public void createGitlabProject(Group group);

  public Status getStatus(String statusType);
}
