package fcu.selab.progedu.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.data.ZipFileInfo;
import fcu.selab.progedu.utils.ExceptionUtil;

public class JavacAssignment extends AssignmentType {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavacAssignment.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.JAVAC;
  }

  @Override
  public String getSampleTemplate() {
    return "JavacQuickStart.zip";
  }

  @Override
  public String getJenkinsJobConfigSample() {
    return "config_javac.xml";
  }

  @Override
  public void createJenkinsJobConfig(String username, String projectName) {
    try {
      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
      String jenkinsJobConfigPath = this.getClass()
          .getResource("/jenkins/" + getJenkinsJobConfigSample()).getPath();

      CourseConfig courseConfig = CourseConfig.getInstance();
      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
          + "/webapi";
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
          + ".git";

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      String updateDbUrl = progEduApiUrl + "/commits/update";
      // to-do : command
      String assignmentPath = System.getProperty("java.io.tmpdir") + "/tests/" + projectName;
      String command = getCommandFromFile(assignmentPath);

      Document doc = docBuilder.parse(jenkinsJobConfigPath);
      doc.getElementsByTagName("command").item(0).setTextContent(command);
      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("user").item(0).setTextContent(username);
      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
      transformer.transform(source, result);
    } catch (LoadConfigFailureException | ParserConfigurationException | SAXException | IOException
        | TransformerException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  @Override
  public StatusEnum checkStatusType(int num, String username, String assignmentName) {
    StatusEnum status;
    StatusService statusService = StatusService.getInstance();
    if (statusService.isInitialization(num)) {
      status = StatusEnum.INITIALIZATION;
    } else {
      JenkinsService jenkinsService = JenkinsService.getInstance();
      String jobName = username + "_" + assignmentName;
      String console = jenkinsService.getConsole(jobName, num);

      if (statusService.isBuildSuccess(console)) {
        status = StatusEnum.BUILD_SUCCESS;
      } else {
        status = StatusEnum.COMPILE_FAILURE;
      }
    }
    return status;
  }

  @Override
  public void createTemplate(String uploadDirectory) {
  }

  @Override
  public ZipFileInfo createTestCase(String testDirectory) {
    long testZipChecksum = 0;
    String testZipUrl = "";

    createCommandFile(testDirectory);
    ZipFileInfo zipFileInfo = new ZipFileInfo(testZipChecksum, testZipUrl);

    return zipFileInfo;
  }

  /**
   * Create a file with compile command in test directory
   * @param assignmentPath assignmentPath
   */
  private void createCommandFile(String assignmentPath) {
    String command = searchJavaFile(assignmentPath);
    List<String> lines = Arrays.asList(command);
    Path file = Paths.get(assignmentPath + "-command");
    try {
      Files.write(file, lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Raed the command file in test directory and return command string
   *
   * @param assignmentPath assignmentPath
   */
  private String getCommandFromFile(String assignmentPath) {
    StringBuilder sb = new StringBuilder();

    try (BufferedReader br = Files.newBufferedReader(Paths.get(assignmentPath + "-command"))) {
      String line;

      while ((line = br.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return sb.toString();
  }

  /**
   * Search all Java file in this assignment
   *
   * @param assignmentPath assignmentPath
   */
  public String searchJavaFile(String assignmentPath) {
    List<String> fileList = null;
    String assignmentName = new File(assignmentPath).getName();

    try (Stream<Path> walk = Files.walk(Paths.get(assignmentPath))) {

      fileList = walk.filter(Files::isRegularFile).map(x -> x.toString())
          .filter(f -> f.endsWith(".java")).collect(Collectors.toList());

    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    System.out.print(getCommand(fileList, assignmentName));
    return getCommand(fileList, assignmentName);
  }

  /**
   *
   * @param fileList fileList
   * @param assignmentName assignmentName
   */
  private String getCommand(List<String> fileList, String assignmentName) {
    String command = "";

    for (String absolutePath : fileList) {
      String subPath = absolutePath
          .substring(absolutePath.indexOf(assignmentName) + assignmentName.length() + 1);
      command += "javac " + subPath + "\n";
    }
    command += "echo \"BUILD SUCCESS\"";

    return command;
  }
}
