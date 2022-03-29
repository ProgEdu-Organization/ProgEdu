package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Matcher.*;

public class PythonPipelineConfig extends JenkinsProjectConfig{
  private static final Logger LOGGER = LoggerFactory.getLogger(PythonPipelineConfig.class);

  InputStream baseConfig = this.getClass().getResourceAsStream("/jenkins/pipelineConfig.xml");
  Document xmlDocument;

  /**
   * PythonConfig
   *
   * @param projectUrl   projectUrl
   */
  public PythonPipelineConfig(String projectUrl, String updateDbUrl,
                               String username, String projectName) {

    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseConfig);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  /**
   * PythonConfig with order
   *
   * @param projectUrl   projectUrl
   */
  public PythonPipelineConfig(String projectUrl, String updateDbUrl,
                              String username, String projectName, String order) {

    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseConfig);

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

    String pipeline = createPipelineWithOrder(projectUrl, updateDbUrl, username, projectName, order);
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
      InputStream androidPipeline = this.getClass().getResourceAsStream("/jenkins/python-pipeline");

      String pipeLine = IOUtils.toString(androidPipeline, StandardCharsets.UTF_8);
      pipeLine = pipeLine.replaceAll("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-project-name\\}", projectName);

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
      InputStream pythonPipeline = this.getClass().getResourceAsStream("/jenkins/python-pipeline");

      String pipeLine = IOUtils.toString(pythonPipeline, StandardCharsets.UTF_8);
      pipeLine = pipeLine.replaceAll("\\{GitLab-url\\}", projectUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-user-name\\}", username);
      pipeLine = pipeLine.replaceAll("\\{ProgEdu-project-name\\}", projectName);

      String insertStages = "";
      for (String temp: ordersList) {
        if (temp.equals("Compile Failure")) {
          insertStages += makeStageString("Build", getDockerCommand(temp));
        } else if (temp.equals("Unit Test Failure")) {
          insertStages += makeStageString("Pytest", getDockerCommand(temp));
        } else if (temp.equals("Coding Style Failure")) {
          insertStages += makeStageString("Coding Style", getDockerCommand(temp));
        }
      }
      insertStages = Matcher.quoteReplacement(insertStages);
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
    String dockerCommand = "echo \"python {stage_name} start\""
        + "\n\t\t\t\t'''\n\t\t\t\tsh'''\n\t\t\t\t"
        + "docker run --privileged -i -v $PWD:/tmp yachi77/progedu-python:v1 "
        + "bash -c 'cd tmp/; {python_command}'"
        + "\n\t\t\t\t'''\n\t\t\t\tsh'''\n\t\t\t\t"
        + "echo \"python {stage_name} end\"";
    String command = "";
    String result = "";
    switch (status) {
      case "Compile Failure":
        command = "python -m compileall \\$(ls -d */)";
        dockerCommand = dockerCommand.replaceAll("\\{stage_name\\}", "build");
        result = dockerCommand.replaceFirst("\\{python_command\\}", command);
        break;
      case "Coding Style Failure":
        command = "flake8 --filename=*.py";
        dockerCommand = dockerCommand.replaceAll("\\{stage_name\\}", "check style");
        result = dockerCommand.replaceFirst("\\{python_command\\}", command);
        break;
      case "Unit Test Failure":
        command = "python -m  pytest \\$(ls -d */)";
        dockerCommand = dockerCommand.replaceAll("\\{stage_name\\}", "test");
        result = dockerCommand.replaceFirst("\\{python_command\\}", command);
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
   * @param command one command
   */
  public String makeStageString(String name, String command) {
    String result = "\n\t\tstage('" + name + "') {\n\t\t\tsteps {\n\t\t\t\tsh '''\n\t\t\t\t"
        + command + "\n\t\t\t\t'''" + "\n\t\t\t}\n\t\t}";
    return result;
  }

}
