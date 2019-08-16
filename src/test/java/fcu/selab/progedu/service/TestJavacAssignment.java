package fcu.selab.progedu.service;

import org.junit.Test;

public class TestJavacAssignment {
  JavacAssignment javaAssignment;
  String zipFilePath = "";
  String destDirectory = "";

  String testDirectory = "";
  String projectName = "";

  @Test
  public void testJavac() {
    javaAssignment.extractFile(zipFilePath, testDirectory, destDirectory, projectName);
  }
}
