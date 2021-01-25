package fcu.selab.progedu.jenkinsconfig;

import org.w3c.dom.Document;


public abstract class JenkinsConfig {

  public abstract String getXmlConfig();

  public abstract Document getXmlDocument();

  public abstract void setXmlDocument(Document document);

}
