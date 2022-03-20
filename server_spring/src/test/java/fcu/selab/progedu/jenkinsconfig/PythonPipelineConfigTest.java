package fcu.selab.progedu.jenkinsconfig;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.apache.commons.lang3.StringEscapeUtils;

public class PythonPipelineConfigTest {
  PythonPipelineConfig pythonPipelineConfig1 = new PythonPipelineConfig("franky", "franky",
      "franky", "franky","Compile Failure:10, Unit Test Failure:10, Coding Style Failure:30");

  PythonPipelineConfig pythonPipelineConfig2 = new PythonPipelineConfig("franky", "franky",
      "franky", "franky");

  @Test
  public void createPipeline() {
    System.out.println(pythonPipelineConfig1.getXmlConfig());
  }

  @Test
  public void createCommand() {
    String result = pythonPipelineConfig1.getDockerCommand("Compile Failure");
    System.out.println(result);
  }

  @Test
  public void createStage() {
    String name = "Build";
    String command = "python -m compileall \\$(ls -d */)";
    String result1 = pythonPipelineConfig1.makeStageString(name, command);
    System.out.println(result1);
    System.out.println("=========================================================");
  }

  @Test
  public void test() {
    String newPipeLine = "";
    String order = "Compile Failure:10, Unit Test Failure:20, Coding Style Failure:30";
    ArrayList<String> ordersList = new ArrayList<>();
    String[] orderTokens = order.split(", ");
    for (int i = 0; i < orderTokens.length; i++) {
      String[] temp = orderTokens[i].split(":");
      ordersList.add(temp[0]);
    }
    System.out.println(ordersList);
  }
}
