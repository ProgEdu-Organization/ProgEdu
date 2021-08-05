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

public class WebGroupPipelineConfig extends JenkinsProjectConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebGroupPipelineConfig.class);

    InputStream baseConfig = this.getClass().getResourceAsStream("/jenkins/pipelineConfig.xml");

    Document xmlDocument;

    /**
     * WebPipelineConfig
     *
     * @param projectUrl   projectUrl
     */
    public WebGroupPipelineConfig(String projectUrl, String updateDbUrl,
                             String username, String projectName, String updateScreenShotDb) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.xmlDocument = builder.parse(baseConfig);

            setJenkinsPipeline(projectUrl, updateDbUrl, username, projectName, updateScreenShotDb);

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
                                    String username, String projectName, String updateScreenShotDb) {

        String pipeline = createPipeline(projectUrl, updateDbUrl, username,
                projectName, updateScreenShotDb);

        this.xmlDocument.getElementsByTagName("script").item(0).setTextContent(pipeline);
    }

    /**
     * createPipeline
     *
     * @param projectUrl   projectUrl
     * @param updateDbUrl   updateDbUrl
     * @param username   username
     * @param projectName   projectName
     * @param updateScreenShotDb   updateScreenShotDb
     */
    public String createPipeline(String projectUrl, String updateDbUrl,
                                 String username, String projectName, String updateScreenShotDb) {
        String newPipeLine = "";
        try {
            InputStream webPipeline = this.getClass().getResourceAsStream("/jenkins/group-web-pipeline");

            String pipeLine = IOUtils.toString(webPipeline, StandardCharsets.UTF_8);

            pipeLine = pipeLine.replaceAll("\\{GitLab-url\\}", projectUrl);
            pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-updateDbUrl\\}", updateDbUrl);
            pipeLine = pipeLine.replaceAll("\\{ProgEdu-user-name\\}", username);
            pipeLine = pipeLine.replaceAll("\\{ProgEdu-project-name\\}", projectName);

            pipeLine = pipeLine.replaceAll("\\{ProgEdu-server-screenshot-updateDbUrl\\}",
                    updateScreenShotDb);

            newPipeLine = pipeLine;

        } catch (Exception e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
        return newPipeLine;
    }
}
