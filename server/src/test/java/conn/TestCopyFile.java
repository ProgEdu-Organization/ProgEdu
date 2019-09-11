package conn;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TestCopyFile {

  public static void main(String[] args) {
    String tempDir = System.getProperty("java.io.tmpdir");
    String source = "E:/ProgEdu/src/main/resources/jenkins/config_javac.xml";
    try {
      File sourceFile = new File(source);
      File desFile = new File(tempDir + "/configs/OOP-test.xml");
      FileUtils.copyFile(sourceFile, desFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
