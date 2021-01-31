package fcu.selab.progedu.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class JavaIoUtileTest {

  @Test
  public void addFile2EmptyFolder() {

    String tempDir = System.getProperty("java.io.tmpdir");
    File rootFile = new File(tempDir + "/" + "franky-test");
    JavaIoUtile.addFile2EmptyFolder(rootFile, "yaya.txt");
  }

  @Test
  public void deleteDirectory() {

    String tempDir = System.getProperty("java.io.tmpdir");
    JavaIoUtile.deleteDirectory(new File(tempDir + "/" + "franky-test"));

  }

  @Test
  public void deleteFileInDirectory() {

    String tempDir = System.getProperty("java.io.tmpdir");
    JavaIoUtile.deleteFileInDirectory(new File(tempDir + "/" + "franky-test"));
  }
}