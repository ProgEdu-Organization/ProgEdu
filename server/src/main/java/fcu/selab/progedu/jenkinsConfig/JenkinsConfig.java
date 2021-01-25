package fcu.selab.progedu.jenkinsConfig;
import org.w3c.dom.Document;

public abstract class JenkinsConfig {
    public abstract String getXMLConfig();
    public abstract Document getXMLDocument();
    public abstract void setXMLDocument(Document document);
}
