package fcu.selab.progedu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.data.ZipFileInfo;
import fcu.selab.progedu.utils.ExceptionUtil;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ZipHandler {
  GitlabConfig gitData = GitlabConfig.getInstance();
  CourseConfig courseData = CourseConfig.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(ZipHandler.class);

  private String serverIp;

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
      setChecksum(cos.getChecksum().getValue());
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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

  /**
   * Unzip a zip file to specified directory
   *
   * @param targetDirectory the directory unzip to
   * @param sourceFilePath  path of zip file
   */
  public void unzipFile(String sourceFilePath, String targetDirectory) {
    File testsDir = new File(targetDirectory);
    if (!testsDir.exists()) {
      testsDir.mkdir();
    }
    try {
      ZipFile zipFileToTests = new ZipFile(sourceFilePath);
      zipFileToTests.extractAll(targetDirectory);
    } catch (ZipException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * get checksum and the file url after zip
   *
   * @param testDirectory testDirectory
   */
  public ZipFileInfo getZipInfo(String testDirectory) {
    long testZipChecksum;
    String testZipUrl;

    zipTestFolder(testDirectory);
    testZipChecksum = getChecksum();
    testZipUrl = serverIp + courseData.getBaseuri() + "/webapi/assignment/getTestFile?filePath="
            + testDirectory + ".zip";

    return new ZipFileInfo(testZipChecksum, testZipUrl);
  }

}
