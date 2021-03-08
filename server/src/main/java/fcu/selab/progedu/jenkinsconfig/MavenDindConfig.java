package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.utils.JavaIoUtile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MavenDindConfig extends JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenDindConfig.class);

  URL baseUrl = this.getClass().getResource("/jenkins/config_maven_dind.xml");
  Document xmlDocument;

  /**
   * MavenConfig
   *
   * @param projectUrl   projectUrl
   */
  public MavenDindConfig(String projectUrl, String updateDbUrl,
                         String username, String projectName) {

    try {
      Path basePath = Paths.get(this.baseUrl.toURI());
      File baseFile = basePath.toFile();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); // Todo 我不知道這個要不要刪掉, 先註解起來保留
      DocumentBuilder builder = factory.newDocumentBuilder();
      this.xmlDocument = builder.parse(baseFile);

      setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName);


//      setGitLabProjectUrl(projectUrl);
//      setProgEduUpdateUrl(updateDbUrl);
//      setProgEduUpdateUsername(username);
//      setProgEduUpdateProjectName(projectName);

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
    String pipeline = createPipelineString(projectUrl, updateDbUrl, username, projectName);

    pipeline = pipeline.replaceAll("'", "&apos;");
    this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);

  }

  // Todo 命名要修
  /**
   * createPipelineString
   *
   * @param projectUrl   projectUrl
   * @param updateDbUrl   updateDbUrl
   * @param username   username
   * @param projectName   projectName
   */
  public String createPipelineString(String projectUrl, String updateDbUrl,
                                     String username, String projectName) {
    String newPipeLine = "";
    try {
      URL baseUrl = this.getClass().getResource("/jenkins/maven-pipeline");
      Path basePath = Paths.get(baseUrl.toURI());
      File baseFile = basePath.toFile();


      String pipeLine = JavaIoUtile.readFileToString(baseFile);

      Pattern r1 = Pattern.compile("\\{GitLab-url\\}");
      Matcher m1 = r1.matcher(pipeLine);
      newPipeLine = m1.replaceFirst(projectUrl);

      Pattern r2 = Pattern.compile("\\{ProgEdu-server-updateDbUrl\\}");
      Matcher m2 = r2.matcher(newPipeLine);
      newPipeLine = m2.replaceFirst(updateDbUrl);

      Pattern r3 = Pattern.compile("\\{ProgEdu-user-name\\}");
      Matcher m3 = r3.matcher(newPipeLine);
      newPipeLine = m3.replaceFirst(username);

      Pattern r4 = Pattern.compile("\\{ProgEdu-project-name\\}");
      Matcher m4 = r4.matcher(newPipeLine);
      newPipeLine = m4.replaceFirst(projectName);

//      System.out.println(newPipeLine);


    } catch (Exception e) {
      e.printStackTrace();
    }

    return newPipeLine;
  }



}
