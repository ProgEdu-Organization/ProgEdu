package fcu.selab.progedu.jenkinsconfig;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;


public abstract class JenkinsProjectConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsProjectConfig.class);

  public abstract Document getXmlDocument();

  /**
   *  todo
   */
  public String getXmlConfig() {

    String result = "";
    Document xmlDocument = getXmlDocument();
    if (xmlDocument != null) {
      StringWriter strWtr = new StringWriter();
      StreamResult strResult = new StreamResult(strWtr);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      try {
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
        // text
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(xmlDocument.getDocumentElement()),
                strResult);

      } catch (Exception e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }

      result = strResult.getWriter().toString();

      try {
        strWtr.close();
      } catch (IOException e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }

    }
    return result;
  }


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
