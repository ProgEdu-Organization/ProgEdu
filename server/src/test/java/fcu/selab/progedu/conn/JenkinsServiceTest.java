package fcu.selab.progedu.conn;

import fcu.selab.progedu.jenkinsconfig.JenkinsProjectConfig;
import fcu.selab.progedu.jenkinsconfig.MavenPipelineConfig;
import org.junit.Test;

public class JenkinsServiceTest {

    JenkinsService jenkinsService = JenkinsService.getInstance();

    @Test
    public void getConsole() {
        String out = jenkinsService.getConsole("sss2_hhh11", 1);
        System.out.print(out);
    }

    @Test
    public void getConsoleUrl() {
        String out = jenkinsService.getConsoleUrl("sss2_hhh11", 1);
        System.out.print(out);
    }

  @Test
  public void createJobV2() {

    String testString ="franky-test";
    try {
      JenkinsProjectConfig jenkinsConfig = new MavenPipelineConfig(testString, testString, testString, testString);
      jenkinsService.createJobV2("franky-test", jenkinsConfig.getXmlConfig());
    } catch (Exception e) {
      e.printStackTrace();
    }


  }
}