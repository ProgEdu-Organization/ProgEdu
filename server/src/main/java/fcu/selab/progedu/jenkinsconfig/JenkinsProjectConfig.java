package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;


public abstract class JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsProjectConfig.class);

  public abstract String getXmlConfig();

  public abstract Document getXmlDocument();


  /**
   * writeToFile
   *
   * @param filePath   filePath
   */
  public void writeToFile(Path filePath) {
    // write the content into xml file
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(this.getXmlDocument());
      StreamResult result = new StreamResult(filePath.toFile());
      transformer.transform(source, result);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

}
