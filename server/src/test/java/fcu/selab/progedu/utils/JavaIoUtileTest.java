package fcu.selab.progedu.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaIoUtileTest {

  @Test
  public void addFile2EmptyFolder() {

    String tempDir = System.getProperty("java.io.tmpdir");
    JavaIoUtile.addFile2EmptyFolder(tempDir + "/" + "franky-test", "yaya.txt");
  }
}