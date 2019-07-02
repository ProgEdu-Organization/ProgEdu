package fcu.selab.progedu.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.HttpConnect;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class ZipHandler {
  HttpConnect httpConn = new HttpConnect();
  GitlabConfig gitData = GitlabConfig.getInstance();
  CourseConfig courseData = CourseConfig.getInstance();

  public String serverIp;

  StringBuilder sb = new StringBuilder();

  List<String> filesListInDir = new ArrayList<>();

  long checksum = 0;

  String urlForJenkinsDownloadTestFile = "";

  /**
   * Constructor.
   */
  public ZipHandler() throws LoadConfigFailureException {
    serverIp = courseData.getTomcatServerIp();
  }

  /**
   * Size of the buffer to read/write data
   */
  private static final int BUFFER_SIZE = 4096;

  // /**
  //  * Extracts a zip entry (file entry)
  //  * 
  //  * @param zipIn The zip inputstream
  //  * @param filePath The file path
  //  * @throws IOException on fileoutputstream call error
  //  */
  // public void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
  //   try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));) {
  //     byte[] bytesIn = new byte[BUFFER_SIZE];
  //     int read = 0;
  //     while ((read = zipIn.read(bytesIn)) != -1) {
  //       bos.write(bytesIn, 0, read);
  //     }
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //   }
  // }

  public void setStringBuilder(StringBuilder sb) {
    this.sb = sb;
  }

  public StringBuilder getStringBuilder() {
    return sb;
  }

  /**
   * 
   * @param filePath a
   * @return aa
   */
  public String getParentDir(String filePath) {
    String dir = null;
    File file = new File(filePath);
    dir = file.getParent();
    return dir;
  }

  /**
   * modifyPomXml
   * 
   * @param filePath The file path
   * @throws projectName projectName
   */
  public void modifyPomXml(String filePath, String projectName) {
    try {
      System.out.println("modify filePath : " + filePath);
      System.out.println("projectName : " + projectName);
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(filePath);

      Node ndId = doc.getElementsByTagName("artifactId").item(0);
      ndId.setTextContent(projectName);
      System.out.println("doc : " + ndId.getTextContent());

      Node ndName = doc.getElementsByTagName("name").item(0);
      ndName.setTextContent(projectName);
      System.out.println("doc : " + doc.getElementsByTagName("name").item(0));

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filePath));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | SAXException | TransformerException | IOException e) {
      e.printStackTrace();
    }
  }

  public void zipTestFolder(String testFilePath) {
    File testFile = new File(testFilePath);
    zipDirectory(testFile, testFilePath + ".zip");
  }

  private void zipDirectory(File dir, String zipDirName) {
    try (FileOutputStream fos = new FileOutputStream(zipDirName);
        CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);) {
      populateFilesList(dir);
      // now zip files one by one
      // create ZipOutputStream to write to the zip file

      for (String filePath : filesListInDir) {
        System.out.println("Zipping " + filePath);
        // for ZipEntry we need to keep only relative file path, so we used
        // substring on absolute path
        ZipEntry ze = new ZipEntry(
            filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
        zos.putNextEntry(ze);
        // read the file and write to ZipOutputStream
        try (FileInputStream fis = new FileInputStream(filePath);) {
          byte[] buffer = new byte[1024];
          int len;
          while ((len = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
          }
          zos.closeEntry();
        }
      }
      System.out.println("Checksum : " + cos.getChecksum().getValue());
      setChecksum(cos.getChecksum().getValue());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void populateFilesList(File dir) throws IOException {
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        filesListInDir.add(file.getAbsolutePath());
      } else {
        populateFilesList(file);
      }

    }
  }

  public void setChecksum(long checksum) {
    this.checksum = checksum;
  }

  public long getChecksum() {
    return checksum;
  }

  public void setUrlForJenkinsDownloadTestFile(String urlForJenkinsDownloadTestFile) {
    this.urlForJenkinsDownloadTestFile = urlForJenkinsDownloadTestFile;
  }

  public String getUrlForJenkinsDownloadTestFile() {
    return urlForJenkinsDownloadTestFile;
  }

}