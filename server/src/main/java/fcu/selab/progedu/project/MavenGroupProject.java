package fcu.selab.progedu.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenGroupProject extends GroupProjectType {
  private static final Logger LOGGER = LoggerFactory.getLogger(MavenGroupProject.class);

  @Override
  public ProjectTypeEnum getProjectType() {
    return ProjectTypeEnum.MAVEN;
  }

  @Override
  public String getSampleTemplate() {
    return "MavenQuickStart.zip";
  }

  // Todo 這因為還未遷移, 所以先不刪,
//  public void createJenkinsJobConfig(String username, String projectName) {
//    try {
//      GitlabConfig gitlabConfig = GitlabConfig.getInstance();
//      String jenkinsJobConfigPath = getJenkinsJobConfigPath();
//
//      CourseConfig courseConfig = CourseConfig.getInstance();
//      String progEduApiUrl = courseConfig.getTomcatServerIp() + courseConfig.getBaseuri()
//          + "/webapi/groups";
//      String projectUrl = gitlabConfig.getGitlabHostUrl() + "/" + username + "/" + projectName
//          + ".git";
//      String updateDbUrl = progEduApiUrl + "/commits/update";
//      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//      docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
//      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//      Document doc = docBuilder.parse(jenkinsJobConfigPath);
//
//      doc.getElementsByTagName("url").item(0).setTextContent(projectUrl);
//      doc.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
//      doc.getElementsByTagName("user").item(0).setTextContent(username);
//      doc.getElementsByTagName("proName").item(0).setTextContent(projectName);
//
//      // write the content into xml file
//      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//      Transformer transformer = transformerFactory.newTransformer();
//      DOMSource source = new DOMSource(doc);
//      StreamResult result = new StreamResult(new File(jenkinsJobConfigPath));
//      transformer.transform(source, result);
//    } catch (LoadConfigFailureException | ParserConfigurationException | SAXException | IOException
//        | TransformerException e) {
//      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
//      LOGGER.error(e.getMessage());
//    }
//
//  }

}
