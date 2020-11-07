package fcu.selab.progedu.setting;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.utils.ZipHandler;

public class MavenAssignmentSetting implements AssignmentSettings {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MavenAssignmentSetting.class);
  private static String AssignmentPath;
  private static String AssignmentZipPath;
  private ZipHandler zipHandler;
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String settingDir = tempDir + "/assignmentSetting/";
  private static String AssignmentName;

  /**
   * init maven assignment setting
   * 
   */
  public MavenAssignmentSetting(String name) {
    try {
      setAssignmentName(name);
      setZipPath();
      setAssignmentPath();
      zipHandler = new ZipHandler();
    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  public void setAssignmentName(String name) {
    this.AssignmentName = name;
  }

  public String getAssignmentName() {
    return this.AssignmentName;
  }

  @Override
  public void setZipPath() {
    this.AssignmentZipPath = "/usr/local/tomcat/webapps/ROOT/resources/MvnQuickStart.zip";
  }

  @Override
  public void setAssignmentPath() {
    this.AssignmentPath = this.settingDir + getAssignmentName();
  }

  @Override
  public String getZipPath() {
    return this.AssignmentZipPath;
  }

  @Override
  public String getAssignmentPath() {
    return this.AssignmentPath;
  }

  @Override
  public void unZipAssignmenToTmp() {
    zipHandler.unzipFile(this.getZipPath(), this.getAssignmentPath());
  }

  @Override
  public void packUpAssignment() {
    zipHandler.zipTestFolder(getAssignmentPath());
  }

  /**
   * 
   * @param order List Order
   * @param name AssignmentName
   */
  @Override
  public void createAssignmentSetting(List<String> order,String name) {
    try {
      final MavenXpp3Writer writer = new MavenXpp3Writer();
      final List<PluginExecution> checkStyleExecutions = new ArrayList<>();
      Model model = new Model();
        
      model.setModelVersion("4.0.0");
      model.setGroupId("myApp");
      model.setArtifactId(name);
      model.setPackaging("jar");
      model.setVersion("1.0-SNAPSHOT");
      model.setName(name);
      model.setUrl("http://maven.apache.org");
        
      Dependency dependency = new Dependency();
      dependency.setGroupId("junit");
      dependency.setArtifactId("junit");
      dependency.setVersion("4.12");
      dependency.setScope("test");
      model.addDependency(dependency);
        
      Build build = new Build();
      build.setFinalName("ProgEdu");
      //-------------compiler settings
      Plugin pluginCompiler = new Plugin();
      pluginCompiler.setGroupId(" org.apache.maven.plugins ");
      pluginCompiler.setArtifactId("maven-compiler-plugin");
      pluginCompiler.setVersion("3.6.1");
      Xpp3Dom configCompiler = (Xpp3Dom) pluginCompiler.getConfiguration();
      if (configCompiler == null) {
        configCompiler = new Xpp3Dom("configuration");
      }
      Xpp3Dom compilerConfigSource = new Xpp3Dom("source");
      compilerConfigSource.setValue("1.8");
      configCompiler.addChild(compilerConfigSource);

      Xpp3Dom compilerConfigTarget = new Xpp3Dom("target");
      compilerConfigTarget.setValue("1.8");
      configCompiler.addChild(compilerConfigTarget);
        
      pluginCompiler.setConfiguration(configCompiler);
      build.addPlugin(pluginCompiler);
      //---------------------unit test settings
      Plugin pluginTest = new Plugin();
      pluginTest.setGroupId(" org.apache.maven.plugins ");
      pluginTest.setArtifactId("maven-surefire-plugin");
      pluginTest.setVersion("2.12.4");
      Xpp3Dom configTest = (Xpp3Dom) pluginTest.getConfiguration();
      if (configTest == null) {
        configTest = new Xpp3Dom("configuration");
      }
      Xpp3Dom testConfigUseSystemClassLoader = new Xpp3Dom("useSystemClassLoader");
      testConfigUseSystemClassLoader.setValue("false");
      configTest.addChild(testConfigUseSystemClassLoader);
        
      if (order.contains("test") == false) {
        Xpp3Dom testConfigSkip = new Xpp3Dom("skipTests");
        testConfigSkip.setValue("true");
        configTest.addChild(testConfigSkip);
      }
  
      pluginTest.setConfiguration(configTest);
      build.addPlugin(pluginTest);
      //----------------coding style settings
      if (order.contains("codingStyle") == true) { 
        Plugin pluginCheckStyle = new Plugin();
        pluginCheckStyle.setGroupId(" org.apache.maven.plugins ");
        pluginCheckStyle.setArtifactId("maven-checkstyle-plugin");
        pluginCheckStyle.setVersion("2.17");
        PluginExecution checkStyleExecution = new PluginExecution();
        System.out.println(order.indexOf("codingStyle"));
        if (order.indexOf("test") < order.indexOf("codingStyle")) {
          checkStyleExecution.setId("test");
          checkStyleExecution.setPhase("test");
        } else if (order.indexOf("test") > order.indexOf("codingStyle")) {
          checkStyleExecution.setId("compile");
          checkStyleExecution.setPhase("compile");
        }
        
        Xpp3Dom configCheckStyle = (Xpp3Dom) pluginCheckStyle.getConfiguration();
        if (configCheckStyle == null) {
          configCheckStyle = new Xpp3Dom("configuration");
        }
        Xpp3Dom checkStyleConfigLocation = new Xpp3Dom("configLocation");
        checkStyleConfigLocation.setValue("${project.basedir}/config/checks.xml");
        configCheckStyle.addChild(checkStyleConfigLocation);
        Xpp3Dom checkStyleConfigEncoding = new Xpp3Dom("encoding");
        checkStyleConfigEncoding.setValue("UTF-8");
        configCheckStyle.addChild(checkStyleConfigEncoding);
        Xpp3Dom checkStyleConfigConsoleOutput = new Xpp3Dom("consoleOutput");
        checkStyleConfigConsoleOutput.setValue("true");
        configCheckStyle.addChild(checkStyleConfigConsoleOutput);
        Xpp3Dom checkStyleConfigFailsOnError = new Xpp3Dom("failsOnError");
        checkStyleConfigFailsOnError.setValue("true");
        configCheckStyle.addChild(checkStyleConfigFailsOnError);
        Xpp3Dom checkStyleLinkXRef = new Xpp3Dom("linkXRef");
        checkStyleLinkXRef.setValue("false");
        configCheckStyle.addChild(checkStyleLinkXRef);
        checkStyleExecution.setConfiguration(configCheckStyle);
        List<String> checkStyleExecutionGoals = new ArrayList<>();
        String checkStyleExecutionGoal = new String("check");
        
        checkStyleExecutionGoals.add(checkStyleExecutionGoal);
        checkStyleExecution.setGoals(checkStyleExecutionGoals);
        checkStyleExecutions.add(checkStyleExecution);
        pluginCheckStyle.setExecutions(checkStyleExecutions);
        build.addPlugin(pluginCheckStyle);
      }
      //-----------------update all build
      model.setBuild(build);
        
      //---------------------reporting settings
      final Reporting reporting = new Reporting();
      final List<ReportPlugin> reportingPlugins  = new ArrayList<>();;
      ReportPlugin reportingPlugin = new ReportPlugin();
      reportingPlugin.setGroupId(" org.apache.maven.plugins ");
      reportingPlugin.setArtifactId("maven-checkstyle-plugin");
      reportingPlugin.setVersion("2.17");
      List<ReportSet> reportSets = new ArrayList<>();
      ReportSet reportSet = new ReportSet();
      List<String> reports = new ArrayList<>();
      String report = new String("checkstyle");
      reports.add(report);
      reportSet.setReports(reports);
      reportSets.add(reportSet);
      reportingPlugin.setReportSets(reportSets);
      reportingPlugins.add(reportingPlugin);
        
      reporting.setPlugins(reportingPlugins);
      model.setReporting(reporting);
      writer.write(new FileWriter(getAssignmentPath()
          + "/pom.xml"), model);   
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}