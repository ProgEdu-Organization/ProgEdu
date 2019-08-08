package fcu.selab.progedu.project;

import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.status.StatusFactory;

public interface ProjectType {
  public ProjectTypeEnum getProjectType();

  public String getSampleTemplate();

  public void createJenkinsJob(String username, String projectName);

  public String getJenkinsJobConfigSample();

  public void createJenkinsJobConfig();

  public StatusEnum checkStatusType();

  public Status getStatus();

  public StatusFactory getStatusFactory();
}
