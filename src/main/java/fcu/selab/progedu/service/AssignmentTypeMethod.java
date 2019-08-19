package fcu.selab.progedu.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

  public void unzip(String zipFilePath, String zipFolderName, String projectName,
      ZipHandler unzipHandler) throws IOException {
    zipHandler = unzipHandler;
    final String tempDir = System.getProperty("java.io.tmpdir");
    final String uploadDir = tempDir + "/uploads/";
    final String testDir = tempDir + "/tests/";
    // -4 because .zip
    zipFolderName = zipFolderName.substring(0, zipFolderName.length() - 4);

    // unzip file to temp/uploads
    String destDirectory = uploadDir + projectName;
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }
    try {
      ZipFile zipFileToUploads = new ZipFile(zipFilePath);
      zipFileToUploads.extractAll(destDirectory);
    } catch (ZipException e) {
      e.printStackTrace();
    }

    // unzip file to temp/tests
    String testDirectory = testDir + projectName;
    File testsDir = new File(testDirectory);
    if (!testsDir.exists()) {
      testsDir.mkdir();
    } else {
      System.out.println(testDirectory);
    }
    try {
      ZipFile zipFileToTests = new ZipFile(zipFilePath);
      zipFileToTests.extractAll(testDirectory);
    } catch (ZipException e) {
      e.printStackTrace();
    }

    // extract main method and other process
    extractFile(zipFilePath, testDirectory, destDirectory, projectName);

    // zip assignment in temp/tests
    File testFile = new File(testDirectory);
    if (testFile.exists()) {
      zipHandler.zipTestFolder(testDirectory);

      zipHandler.setUrlForJenkinsDownloadTestFile(zipHandler.serverIp
          + "/ProgEdu/webapi/jenkins/getTestFile?filePath=" + testsDir + ".zip");
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
  public void createJenkinsJob(String name, String jenkinsRootUsername, String jenkinsRootPassword)
      throws Exception {
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
  public void createAllJenkinsJob(String userName, String proName, String jenkinsCrumb,
      StringBuilder sb) {
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
  public void createRootJob(String proName, String jenkinsCrumb, StringBuilder sb)
      throws Exception {
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
      tomcatUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi/project/checksum?proName="
          + proName;
      String progApiUrl = courseData.getTomcatServerIp() + "/ProgEdu/webapi";

      // proUrl project name toLowerCase
      proUrl = proUrl.toLowerCase();
      JenkinsApi.modifyXmlFileUrl(filePath, proUrl);
      modifyXmlFile(filePath, progApiUrl, userName, proName, tomcatUrl, sb);
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
