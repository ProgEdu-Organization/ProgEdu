package fcu.selab.progedu.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class JavaIoUtileTest {
  String tempDir = System.getProperty("java.io.tmpdir");
  File rootFile = new File(tempDir, "unit-test-JavaIoUtileTest");

  @Test
  public void addFile2EmptyFolder() {

    JavaIoUtile.createDirectoryIfNotExists(rootFile);
    JavaIoUtile.addFile2EmptyFolder(rootFile, "unit-test.txt");
    System.out.println("The result at : " + rootFile.getPath());
  }

  @Test
  public void deleteDirectory() {
    // The rootFile.getPath() will be deleted.
    JavaIoUtile.deleteDirectory(rootFile);
  }

  @Test
  public void deleteFileInDirectory() {
//    You need put some files in  rootFile.getPath(), then I will delete that.
    JavaIoUtile.deleteFileInDirectory(rootFile);
  }

  @Test
  public void copyDirectoryCompatibilityMode() {
    JavaIoUtile.createDirectoryIfNotExists(rootFile);
    File targetFile= new File(tempDir, "unit-test-JavaIoUtileTest-copy");
    try {
      JavaIoUtile.copyDirectoryCompatibilityMode(rootFile, targetFile);
      System.out.println("check new file at:" + targetFile.getPath());
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(System.getProperty("catalina.base"));

  }
}