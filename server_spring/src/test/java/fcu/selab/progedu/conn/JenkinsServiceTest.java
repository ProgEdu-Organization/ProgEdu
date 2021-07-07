package fcu.selab.progedu.conn;

import fcu.selab.progedu.jenkinsconfig.WebPipelineConfig;
import fcu.selab.progedu.utils.JavaIoUtile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JenkinsServiceTest {

  @Test
  void createJobV2() throws Exception {
    JenkinsService jenkinsService = JenkinsService.getInstance();

    URL testPipelineUrl = this.getClass().getResource("/jenkins/test-pipe-config.xml");
    Path testPipelinePath = Paths.get(testPipelineUrl.toURI());
    File testPipelineFile = testPipelinePath.toFile();


    String pipeLine = JavaIoUtile.readFileToString(testPipelineFile);
    System.out.println(pipeLine);
//    jenkinsService.createJobV2("franky-test-76", pipeLine);
  }

  @Test
  void createJobTest() throws Exception {

//    WebPipelineConfig webPipelineConfig = new WebPipelineConfig("franky", "franky"
//            , "franky", "franky", "franky");

//    JenkinsService jenkinsService = JenkinsService.getInstance();
//    jenkinsService.createJobV2("franky-test-76-1", webPipelineConfig.getXmlConfig());
  }
}