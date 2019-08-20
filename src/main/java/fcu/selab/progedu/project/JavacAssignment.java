package fcu.selab.progedu.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.JenkinsService;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.service.StatusService;
import fcu.selab.progedu.status.StatusEnum;

public class JavacAssignment extends AssignmentType {

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
      String progEduApiUrl = courseConfig.getTomcatServerIp() + "/ProgEdu/webapi";
      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
          + ".git";
      String updateDbUrl = progEduApiUrl + "/commits/update";
      // to-do : command
      String assignmentPath = System.getProperty("java.io.tmpdir") + "/tests/" + projectName;
      String command = searchJavaFile(assignmentPath);
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      Document doc = docBuilder.parse(jenkinsJobConfigPath);
      doc.getElementsByTagName("command").item(0).setTextContent(command);
      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);
      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
      doc.getElementsByTagName("user").item(0).setTextContent(username);
      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
      transformer.transform(source, result);
    } catch (LoadConfigFailureException | ParserConfigurationException | SAXException | IOException
        | TransformerException e) {
      e.printStackTrace();
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
  public void createTestCase(String testDirectory, String assignmentName) {
  }

  /**
   * Search all Java file in this assignment
   * 
   * @param assignmentPath assignmentPath
   */
  public String searchJavaFile(String assignmentPath) {
    File testDir = new File(assignmentPath);
    List<String> result = new ArrayList<>();
    String assignmentName = new File(assignmentPath).getName();
    String command = "";

    search(testDir, result, assignmentName);
    for (String s : result) {
      command += s;
    }

    return command;
  }

  /**
   * 
   * @param folder folder
   * @param result result
   */
  private void search(File folder, List<String> result, String assignmentName) {
    String pattern = ".*\\.java";

    for (final File f : folder.listFiles()) {

      if (f.isDirectory()) {
        search(f, result, assignmentName);
      }

      if (f.isFile()) {
        if (f.getName().matches(pattern)) {
          String filePath = f.getAbsolutePath()
              .substring(f.getAbsolutePath().indexOf(assignmentName) + assignmentName.length() + 1);
          String shell = "javac " + filePath + "\n";
          result.add(shell);
        }
      }
    }
  }

//  public String searchFile(String entryNewName) {
//    StringBuilder sb = new StringBuilder();
//    String last = "";
//    if (entryNewName.endsWith(".java")) {
//      last = entryNewName.substring(entryNewName.length() - 5, entryNewName.length());
//    }
//    String fileName = null;
//    for (int i = 0; i < entryNewName.length() - 3; i++) {
//      if (entryNewName.substring(i, i + 3).equals("src")) {
//        fileName = entryNewName.substring(i);
//        System.out.println("Search java file fileName : " + fileName);
//        if (last.equals(".java")) {
//          sb.append("javac " + fileName + "\n");
//          sb.append("echo \"BUILD SUCCESS\"");
//          zipHandler.setStringBuilder(sb);
//        }
//      }
//    }
//  }
//
//  public JavacAssignment() {
//    super(new JavacStatusFactory());
//  }
//
//  public String getSampleZip() {
//    return "JavacQuickStart.zip";
//  }
//
//  /**
//   * searchFile
//   * 
//   * @param entryNewName entryNewName
//   */
//  public void searchFile(String entryNewName) {
//    StringBuilder sb = new StringBuilder();
//    String last = "";
//    if (entryNewName.endsWith(".java")) {
//      last = entryNewName.substring(entryNewName.length() - 5, entryNewName.length());
//    }
//    String fileName = null;
//    for (int i = 0; i < entryNewName.length() - 3; i++) {
//      if (entryNewName.substring(i, i + 3).equals("src")) {
//        fileName = entryNewName.substring(i);
//        System.out.println("Search java file fileName : " + fileName);
//        if (last.equals(".java")) {
//          sb.append("javac " + fileName + "\n");
//          sb.append("echo \"BUILD SUCCESS\"");
//          zipHandler.setStringBuilder(sb);
//        }
//      }
//    }
//  }
//
//  /**
//   * extract main method and modify pom.xml
//   * 
//   * @param testDirectory testDirectory
//   * @param projectName   projectName
//   */
//  public void extractFile(String zipFilePath, String testDirectory, String destDirectory,
//      String projectName) {
//    int parDirLength = 0;
//    String parentDir = null;
//
//    try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
//
//      ZipEntry entry = zipIn.getNextEntry();
//      while (entry != null) {
//        String filePath = destDirectory + File.separator + entry.getName();
//        ;
//
//        if (filePath.substring(filePath.length() - 4).equals("src/") && parDirLength == 0) {
//          parentDir = zipHandler.getParentDir(filePath);
//          parDirLength = parentDir.length() + 1;
//        }
//        String entryNewName = filePath.substring(parDirLength);
//
//        if (!entry.isDirectory()) {
//          searchFile(entryNewName);
//        }
//        zipIn.closeEntry();
//        entry = zipIn.getNextEntry();
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public String getJenkinsConfig() {
//    return "config_javac.xml";
//  }
//
//  /**
//   * modifyXmlFile
//   * 
//   * @param filePath   filePath
//   * @param progApiUrl progApiUrl
//   * @param userName   userName
//   * @param proName    proName
//   * @param tomcatUrl  tomcatUrl
//   * @param sb         sb
//   */
//  public void modifyXmlFile(String filePath, String progApiUrl, String userName, String proName,
//      String tomcatUrl, StringBuilder sb) {
//    try {
//      String filepath = filePath;
//      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//      Document doc = docBuilder.parse(filepath);
//
//      Node ndUrl = doc.getElementsByTagName("command").item(0);
//      ndUrl.setTextContent(sb.toString());
//
//      String updateDbUrl = progApiUrl + "/commits/update";
//      Node progeduDbUrl = doc.getElementsByTagName("progeduDbUrl").item(0);
//      progeduDbUrl.setTextContent(updateDbUrl);
//
//      Node user = doc.getElementsByTagName("user").item(0);
//      user.setTextContent(userName);
//
//      Node ndProName = doc.getElementsByTagName("proName").item(0);
//      ndProName.setTextContent(proName);
//
//      // write the content into xml file
//      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//      Transformer transformer = transformerFactory.newTransformer();
//      DOMSource source = new DOMSource(doc);
//      StreamResult result = new StreamResult(new File(filepath));
//      transformer.transform(source, result);
//    } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
//      e.printStackTrace();
//    }
//
//  }
}
