package fcu.selab.progedu.jenkins;

import fcu.selab.progedu.config.JenkinsConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class JobStatus {
  private String name;
  private String url;
  private String color;
  private String lastCheckStyleResultUrl;
  private String jobApiJson;
  private JenkinsApi jenkinsApi;
  private JenkinsConfig jenkinsData = JenkinsConfig.getInstance();

  public JobStatus() {
    jenkinsApi = new JenkinsApi();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getLastCheckStyleResultUrl() {
    return lastCheckStyleResultUrl;
  }

  public void setLastCheckStyleResultUrl(String lastCheckStyleResultUrl) {
    this.lastCheckStyleResultUrl = lastCheckStyleResultUrl;
  }

  /**
   * Set Job Api Json
   */
  public void setJobApiJson() {
    try {
      jobApiJson = jenkinsApi.getJobApiJson(jenkinsData.getJenkinsRootUsername(),
          jenkinsData.getJenkinsRootPassword(), url);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public String getJobApiJson() {
    return jobApiJson;
  }

}
