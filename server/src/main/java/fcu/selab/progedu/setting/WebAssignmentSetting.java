package fcu.selab.progedu.setting;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebAssignmentSetting implements AssignmentSettings {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(WebAssignmentSetting.class);
  private SettingZipHandler settingZipHandler;
  private String configWebXmlPath =
      "/usr/local/tomcat/webapps/ROOT/WEB-INF/classes/jenkins/config_web.xml";
  private Document doc;
  private String assignmentName;

  public WebAssignmentSetting(String assignmentName) {
    this.assignmentName = assignmentName;
    settingZipHandler = new SettingZipHandler();
  }

  @Override
  public void unZipAssignmentToTmp() {
    settingZipHandler.unZipAssignmentToTmp("web", this.assignmentName);
  }

  @Override
  public void packUpAssignment() {
    settingZipHandler.packUpAssignment(this.assignmentName);

  }

  @Override
  public void createAssignmentSetting(List<String> order, String name) {
    List <String> commands = getCommand(order);

    try {
      File inputFile = new File(configWebXmlPath);
      DocumentBuilderFactory docFactory =
          DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder =
          docFactory.newDocumentBuilder();
      this.doc = docBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      NodeList builders = doc.getElementsByTagName("builders");
      NodeList value = (NodeList) builders.item(0);
      Element value2 = (Element) builders.item(0);

      int BuildStepWithTimeoutCount = value.getLength();
      while (BuildStepWithTimeoutCount > 5 ) {
        Node wannaDel = (Node) value2.getElementsByTagName
            ("hudson.plugins.build__timeout.BuildStepWithTimeout").item(1);
        value2.removeChild(wannaDel);
        BuildStepWithTimeoutCount = BuildStepWithTimeoutCount-2;
      }

      for (String command: commands) {
        Element TimeOut = doc.createElement("timeoutMinutes");
        TimeOut.appendChild(doc.createTextNode("15"));
        Element Strategy = doc.createElement("strategy");
        Strategy.setAttribute("class",
            "hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy");
        Strategy.appendChild(TimeOut);

        Element NodeCommand = doc.createElement("command");
        NodeCommand.appendChild(doc.createTextNode(command));
        Element BuildStep = doc.createElement("buildStep");
        BuildStep.setAttribute("class", "hudson.tasks.Shell");
        BuildStep.appendChild(NodeCommand);

        Element FailOperation = doc.createElement
            ("hudson.plugins.build__timeout.operations.FailOperation");
        Element Operation = doc.createElement("operationList");
        Operation.appendChild(FailOperation);

        Element BuildStepWithTimeout = doc.createElement
            ("hudson.plugins.build__timeout.BuildStepWithTimeout");
        BuildStepWithTimeout.setAttribute("plugin", "build-timeout@1.19");
        BuildStepWithTimeout.appendChild(Strategy);
        BuildStepWithTimeout.appendChild(BuildStep);
        BuildStepWithTimeout.appendChild(Operation);

        builders.item(builders.getLength()-1).appendChild(BuildStepWithTimeout);
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
  }

  @Override
  public void writeAssignmentSettingFile() {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT,"yes");
      transformer.transform(new DOMSource(doc),
              new StreamResult(new File(configWebXmlPath)));
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  private List<String> getCommand(List<String> orders) {
    List <String> commands = new ArrayList<String>();
    commands.add("npm i");

    for (String order : orders) {
      if (order.equals("HTML Failure")) {
        commands.add("npm run htmlhint");
      } else if (order.equals("CSS Failure")) {
        commands.add("npm run stylelint");
      } else if(order.equals("JavaScript Failure")) {
        commands.add("npm run eslint");
      } else if(order.equals("Unit Test Failure")) {
        commands.add("npm run test");
      }
    }
    return commands;
  }
}
