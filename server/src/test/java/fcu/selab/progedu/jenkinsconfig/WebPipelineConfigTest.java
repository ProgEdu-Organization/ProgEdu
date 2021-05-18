package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebPipelineConfigTest {

  @Test
  public void createPipeline() {
    WebPipelineConfig webPipelineConfig = new WebPipelineConfig("franky", "franky"
            , "franky", "franky", "franky",
        "HTML Failure, Unit Test Failure, JavaScript Failure, CSS Failure");
    System.out.println(webPipelineConfig.getXmlConfig());
  }
}