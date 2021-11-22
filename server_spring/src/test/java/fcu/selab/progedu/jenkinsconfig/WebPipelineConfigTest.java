package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebPipelineConfigTest {

  @Test
  void getXmlDocument() throws LoadConfigFailureException {
    WebPipelineConfig webPipelineConfig = new WebPipelineConfig("franky", "franky"
            , "franky", "franky", "franky",
        "HTML Failure:20, JavaScript Failure:20, Unit Test Failure:35, CSS Failure:25");
    System.out.println(webPipelineConfig.getXmlConfig());

//    System.out.println(this.getClass().getClassLoader().getResource(""));
//    String path = this.getClass().getResource("/").getPath();
//    System.out.println(path);
  }
}