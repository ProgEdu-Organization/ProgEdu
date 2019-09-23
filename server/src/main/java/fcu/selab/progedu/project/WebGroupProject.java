package fcu.selab.progedu.project;

public class WebGroupProject extends GroupProjectType {

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.WEB;
  }

  @Override
  public String getSampleTemplate() {
    return "WebQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigSample() {
    return "group_web_config.xml";
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    // TODO Auto-generated method stub

  }

}
