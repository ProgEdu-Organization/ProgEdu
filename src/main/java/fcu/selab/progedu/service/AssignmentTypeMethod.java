package fcu.selab.progedu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.config.CourseConfig;
import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.conn.Conn;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.jenkins.JenkinsApi;
import fcu.selab.progedu.status.Status;
import fcu.selab.progedu.status.StatusFactory;
import fcu.selab.progedu.utils.ZipHandler;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public abstract class AssignmentTypeMethod implements AssignmentTypeSelector {

  StatusFactory statusFactory = null;
  ZipHandler zipHandler = null;
  JenkinsApi jenkinsApi = null;

  public AssignmentTypeMethod(StatusFactory statusFactory) {
    this.statusFactory = statusFactory;
  }

  public abstract String getSampleZip();

  /**
   * 
   * @param zipFilePath   zipFilePath
   * @param zipFolderName zipFolderName
   * @param projectName   projectName
   */

  public void unzip(String zipFilePath, String zipFolderName, String projectName, ZipHandler unzipHandler)
      throws IOException {
    zipHandler = unzipHandler;
    final String tempDir = System.getProperty("java.io.tmpdir");
    final String uploadDir = tempDir + "/uploads/";
    final String testDir = tempDir + "/tests/";
    String parentDir = null;
    int parDirLength = 0;
    // -4 because .zip
    zipFolderName = zipFolderName.substring(0, zipFolderName.length() - 4);

    File fileUploadDir = new File(uploadDir);
    if (!fileUploadDir.exists()) {
      fileUploadDir.mkdir();
    }

    String destDirectory = uploadDir + projectName;
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }

    File fileTestDir = new File(testDir);
    if (!fileTestDir.exists()) {
      fileTestDir.mkdir();
    }

    String testDirectory = testDir + projectName;
    File testsDir = new File(testDirectory);
    if (!testsDir.exists()) {
      testsDir.mkdir();
    } else {
      System.out.println(testDirectory);
    }

    String targetDirectory = testDir + projectName;
    File targetDir = new File(targetDirectory);
    if (!targetDir.exists()) {
      targetDir.mkdir();
    } else {
      System.out.println(targetDir);
    }

    try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
      ZipFile zipFile = new ZipFile(zipFilePath);
      zipFile.extractAll(targetDirectory);
      zipHandler.modifyPomXml(targetDirectory + "/pom.xml", projectName);
      // Zip HW in temp/tests
      zipHandler.zipFolder(targetDirectory);

      ZipEntry entry = zipIn.getNextEntry();
      while (entry != null) {
        String filePath = destDirectory + File.separator + entry.getName();
        File newFile = new File(filePath);

        // create all non exists folders
        // else you will hit FileNotFoundException for compressed folder
        new File(newFile.getParent()).mkdirs();

        if (filePath.substring(filePath.length() - 4).equals("src/") && parDirLength == 0) {
          parentDir = zipHandler.getParentDir(filePath);
          parDirLength = parentDir.length() + 1;
        }
        String entryNewName = filePath.substring(parDirLength);

        if (!entry.isDirectory()) {
          // if the entry is a file, extracts it
          zipHandler.extractFile(zipIn, filePath);

          // if filePath equals pom.xml, modify the project name
          if (filePath.substring(filePath.length() - 7, filePath.length()).equals("pom.xml")) {
            zipHandler.modifyPomXml(filePath, projectName);
          }
          searchFile(entryNewName);
        } else {
          // if the entry is a directory, make the directory
          File dir = new File(filePath);
          dir.mkdir();
        }
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      }
    } catch (ZipException e) {
      e.printStackTrace();
    }
    // iterates over entries in the zip file
    copyTestFile(destDir, destDirectory, testDirectory);

    File testFile = new File(testDirectory);
    if (testFile.exists()) {
      // zipHandler.zipTestFolder(testDirectory);

      zipHandler.setUrlForJenkinsDownloadTestFile(
          zipHandler.serverIp + "/ProgEdu/webapi/jenkins/getTestFile?filePath=" + testsDir + ".zip");
    } else {
      System.out.println("test file not exists");
    }
  }

  /**
   * 
   * @param name                name
   * @param jenkinsRootUsername jenkinsRootUsername
   * @param jenkinsRootPassword jenkinsRootPassword
   */
  public void createJenkinsJob(String name, String jenkinsRootUsername, String jenkinsRootPassword) throws Exception {
    Conn conn = Conn.getInstance();
    jenkinsApi = new JenkinsApi();
    JenkinsApi jenkins = JenkinsApi.getInstance();
    String jenkinsCrumb = jenkinsApi.getCrumb(jenkinsRootUsername, jenkinsRootPassword);
    StringBuilder sb = zipHandler.getStringBuilder();
    createRootJob(name, jenkinsCrumb, sb);
    List<GitlabUser> users = conn.getUsers();
    Collections.reverse(users);
    for (GitlabUser user : users) {
      if (user.getId() == 1) {
        jenkins.buildJob(user.getUsername(), name, jenkinsCrumb);
        continue;
      }
      createAllJenkinsJob(user.getUsername(), name, jenkinsCrumb, sb);
      jenkins.buildJob(user.getUsername(), name, jenkinsCrumb);
    }
  }

  /**
   * 
   * @param userName     userName
   * @param proName      proName
   * @param jenkinsCrumb jenkinsCrumb
   * @param sb           sb
   */
  public void createAllJenkinsJob(String userName, String proName, String jenkinsCrumb, StringBuilder sb) {
    // ---Create Jenkins Job---
    GitlabConfig gitlabConfig = GitlabConfig.getInstance();
    String proUrl = null;
    try {
      proUrl = gitlabConfig.getGitlabHostUrl() + "/" + userName + "/" + proName + ".git";
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
    String filePath = modifyXml(userName, proName, proUrl, sb);
    JenkinsApi.postCreateJob(userName, proName, proUrl, jenkinsCrumb, filePath);
    // ------------------------
  }

  /**
   * 
   * @param proName      proName
   * @param jenkinsCrumb jenkinsCrumb
   * @param sb           sb
   */
  public void createRootJob(String proName, String jenkinsCrumb, StringBuilder sb) throws Exception {
    // ---Create Jenkins Job---
    GitlabConfig gitlabConfig = GitlabConfig.getInstance();
    String proUrl = gitlabConfig.getGitlabHostUrl() + "/root/" + proName + ".git";
    System.out.println("proUrl : " + proUrl);
    String filePath = modifyXml("root", proName, proUrl, sb);
    JenkinsApi.postCreateJob("root", proName, proUrl, jenkinsCrumb, filePath);
    JenkinsApi.backupConfig(filePath, proName);
    // ------------------------
  }

  /**
   * 
   * @param userName userName
   * @param proName  proName
   * @param proUrl   proUrl
   * @param sb       sb
   */
  public String modifyXml(String userName, String proName, String proUrl, StringBuilder sb) {
    String filePath = null;
    String configType = getJenkinsConfig();
    filePath = this.getClass().getResource("/jenkins/" + configType).getPath();

    try {
      String tomcatUrl;
      CourseConfig courseData = CourseConfig.getInstance();
      tomcatUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/project/checksum?proName=" + proName;
      String updateDbUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/commits/update";
      // proUrl project name toLowerCase
      proUrl = proUrl.toLowerCase();
      JenkinsApi.modifyXmlFileUrl(filePath, proUrl);
      modifyXmlFile(filePath, updateDbUrl, userName, proName, tomcatUrl, sb);
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }

    return filePath;
  }

  /**
   * @param statusType status.
   */
  public Status getStatus(String statusType) {
    return statusFactory.getStatus(statusType);
  }

}
