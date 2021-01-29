package fcu.selab.progedu.project;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebGroupProject extends GroupProjectType {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebGroupProject.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.WEB;
  }

  @Override
  public String getSampleTemplate() {
    return "WebQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigPath() {
    URL url = this.getClass().getResource("/jenkins/group_web_config.xml");
    return url.getPath();
  }

}
