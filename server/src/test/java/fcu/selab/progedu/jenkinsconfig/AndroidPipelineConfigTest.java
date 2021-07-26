package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

public class AndroidPipelineConfigTest {

  AndroidPipelineConfig androidPipelineConfig1 = new AndroidPipelineConfig("franky", "franky"
      , "franky", "franky",
      "Compile Failure, Unit Test Failure, Coding Style Failure");
  AndroidPipelineConfig androidPipelineConfig2 = new AndroidPipelineConfig("franky", "franky"
      , "franky", "franky");

  @Test
  public void createPipeline() {
    System.out.println(androidPipelineConfig1.getXmlConfig());
  }

  @Test
  public void createStage() {
    String name = "compile";
    String command = "./gradlew compileDebugJavaWithJavac";
    String[] commands = {"chmod +x gradlew", "./gradlew compileDebugJavaWithJavac"};
    String result1 = androidPipelineConfig1.makeStageString(name, command);
    String result2 = androidPipelineConfig1.makeStageString(name, commands);
    System.out.println(result1);
    System.out.println("=========================================================");
    System.out.println(result2);
  }
}
