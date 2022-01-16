package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebPipelineConfigTest {

  @Test
  public void createPipeline() {
    WebPipelineConfig webPipelineConfig = new WebPipelineConfig("franky", "franky"
            , "franky", "franky", "franky",
        "HTML Failure:20, JavaScript Failure:20, Unit Test Failure:35, CSS Failure:25");
    System.out.println(webPipelineConfig.getXmlConfig());
  }
}