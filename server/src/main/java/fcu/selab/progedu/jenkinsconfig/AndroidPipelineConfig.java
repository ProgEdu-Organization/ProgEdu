package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.utils.JavaIoUtile;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AndroidPipelineConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidPipelineConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/pipelineConfig.xml");
  Document xmlDocument;

  /**
   * AndroidConfig
   *
   * @param projectUrl   projectUrl
   */
  public AndroidPipelineConfig(String projectUrl, String updateDbUrl,
                             String username, String projectName) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  /**
   * AndroidConfig
   *
   * @param projectUrl   projectUrl
   */
  public AndroidPipelineConfig(String projectUrl, String updateDbUrl,
                               String username, String projectName, String order) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setJenkinsPipelineWithOrder(projectUrl, updateDbUrl, username, projectName, order);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }


  private void setJenkinsPipeline(String projectUrl, String updateDbUrl,
                                  String username, String projectName) {

    String pipeline = createPipeline(projectUrl, updateDbUrl, username, projectName);
    this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);
  }

  private void setJenkinsPipelineWithOrder(String projectUrl, String updateDbUrl,
                                           String username, String projectName, String order) {

    String pipeline = createPipelineWithOrder(projectUrl, updateDbUrl, username, projectName,
        order);
    this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);
  }

  /**
   * createPipeline
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl   updateDbUrl
   * @param username   username
   * @param projectName   projectName
   */
  public String createPipeline(String projectUrl, String updateDbUrl,
                               String username, String projectName) {
    String newPipeLine = "";
    try {
      URL androidPipelineUrl = this.getClass().getResource("/jenkins/android-pipeline");
      Path androidPipelinePath = Paths.get(androidPipelineUrl.toURI());
      File androidPipelineFile = androidPipelinePath.toFile();


      String pipeLine = JavaIoUtile.readFileToString(androidPipelineFile);
      pipeLine = pipeLine.replaceFirst("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-project-name\\}", projectName);

      newPipeLine = pipeLine;

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return newPipeLine;
  }

  /**
   * createPipeline
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl   updateDbUrl
   * @param username   username
   * @param projectName   projectName
   */
  public String createPipelineWithOrder(String projectUrl, String updateDbUrl,
                                        String username, String projectName, String order) {
    String newPipeLine = "";
    ArrayList<String> ordersList = new ArrayList<>();
    String[] orderTokens = order.split(", ");
    for (int i = 0; i < orderTokens.length; i++) {
      String[] temp = orderTokens[i].split(":");
      ordersList.add(temp[0]);
    }
    try {
      URL androidPipelineUrl = this.getClass().getResource("/jenkins/android-pipeline");
      Path androidPipelinePath = Paths.get(androidPipelineUrl.toURI());
      File androidPipelineFile = androidPipelinePath.toFile();


      String pipeLine = JavaIoUtile.readFileToString(androidPipelineFile);
      pipeLine = pipeLine.replaceFirst("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceFirst("\\{ProgEdu-project-name\\}", projectName);

      //
      String insertStages = "";
      for (String temp: ordersList) {
        if (temp.equals("Compile Failure")) {
          insertStages += makeStageString("compile", getDockerCommand(temp));
        } else if (temp.equals("Unit Test Failure")) {
          insertStages += makeStageString("unit test", getDockerCommand(temp));
        } else if (temp.equals("Coding Style Failure")) {
          insertStages += makeStageString("checkstyle", getDockerCommand(temp));
        } else if (temp.equals("UI Test Failure")) {
          insertStages += makeStageString("UI test", getDockerCommand(temp));
        }
      }
      pipeLine = pipeLine.replaceFirst("\\{other stages\\}", insertStages);

      newPipeLine = pipeLine;

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return newPipeLine;
  }

  /**
   * make docker command string
   *
   * @param status     the status which will be inspected
   */
  public String getDockerCommand(String status) {
    String dockerCommand = "docker run --privileged -i -v \\$PWD:/data -v gradle-cache:/cache"
        + " yhwang8943/android-container:latest"
        + " bash -c 'chmod +x /data/gradlew; ./start.sh{android_command}'";
    String command = "; /data/gradlew {command} -p /data";
    String result = "";
    switch (status) {
      case "Compile Failure":
        command = command.replaceFirst("\\{command\\}", "compileDebugJavaWithJavac");
        result = dockerCommand.replaceFirst("\\{android_command\\}", command);
        break;
      case "Unit Test Failure":
        command = command.replaceFirst("\\{command\\}", "testDebugUnitTest");
        result = dockerCommand.replaceFirst("\\{android_command\\}", command);
        break;
      case "Coding Style Failure":
        String command1 = command.replaceFirst("\\{command\\}", "lint");
        String command2 = command.replaceFirst("\\{command\\}", "checkstyle");
        command = command1 + command2;
        result = dockerCommand.replaceFirst("\\{android_command\\}", command);
        break;
      case "UI Test Failure":
        command = command.replaceFirst("\\{command\\}", "connectedDebugAndroidTest");
        result = dockerCommand.replaceFirst("\\{android_command\\}", command);
        break;
      default:
        break;
    }
    return result;
  }

  /**
   * make stage string
   *
   * @param name stage name
   * @param commands several commands
   */
  public String makeStageString(String name, String[] commands) {
    String start = "\n\t\tstage('" + name + "') {\n\t\t\tsteps {";
    String end = "\n\t\t\t}\n\t\t}";
    String result = start;
    for (String command: commands) {
      String commandString = "\n\t\t\t\tsh '{command}'";
      commandString = commandString.replace("{command}", command);
      result = result + commandString;
    }
    result += end;
    return result;
  }

  /**
   * make stage string
   *
   * @param name stage name
   * @param command one command
   */
  public String makeStageString(String name, String command) {
    String result = "\n\t\tstage('" + name + "') {\n\t\t\tsteps {\n\t\t\t\tsh '''\n\t\t\t\t"
        + command + "\n\t\t\t\t'''" + "\n\t\t\t}\n\t\t}";
    return result;
  }
}
