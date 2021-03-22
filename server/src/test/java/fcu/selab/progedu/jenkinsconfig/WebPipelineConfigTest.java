package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebPipelineConfigTest {

  @Test
  public void createPipeline() {
    WebPipelineConfig webPipelineConfig = new WebPipelineConfig("franky", "franky"
            , "franky", "franky", "franky");
    System.out.println(webPipelineConfig.getXmlConfig());
  }
}