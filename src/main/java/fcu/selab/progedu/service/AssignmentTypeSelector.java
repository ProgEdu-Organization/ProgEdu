package fcu.selab.progedu.service;

import java.io.File;
import java.io.IOException;

public interface AssignmentTypeSelector {

  public String getSampleZip();

  public void unzip(String zipFilePath, String zipFolderName, String projectName)
      throws IOException;

  public void searchFile(String entryNewName);

  public void copyTestFile(File folder, String strFolder, String testFilePath);

  public void createJenkinsJob(String name, String jenkinsRootUsername, String jenkinsRootPassword)
      throws Exception;

  public String getJenkinsConfig();

  public void modifyXmlFile(String filePath, String updateDbUrl, String userName, String proName,
      String tomcatUrl, StringBuilder sb);

}
