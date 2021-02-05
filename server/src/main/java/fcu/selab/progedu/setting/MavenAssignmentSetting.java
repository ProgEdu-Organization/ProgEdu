
package fcu.selab.progedu.setting;

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

import java.io.File;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class MavenAssignmentSetting implements AssignmentSettings {

  private static final Logger LOGGER = LoggerFactory
          .getLogger(MavenAssignmentSetting.class);

  /**
   * Create pom.xml
   *
   * @param order List Order
   * @param name  AssignmentName
   */
  @Override
  public void createAssignmentSetting(List<String> order, String name, String targetPomSavePath) {
    File mavenPomXmlSetting = new File(targetPomSavePath);
    if (!mavenPomXmlSetting.exists()) {
      mavenPomXmlSetting.mkdir();
    }
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

    if (order.contains("Unit Test Failure") == false) {
      Xpp3Dom testConfigSkip = new Xpp3Dom("skipTests");
      testConfigSkip.setValue("true");
      configTest.addChild(testConfigSkip);
    }

    pluginTest.setConfiguration(configTest);
    build.addPlugin(pluginTest);
    //----------------coding style settings
    if (order.contains("Coding Style Failure") == true) {
      Plugin pluginCheckStyle = new Plugin();
      pluginCheckStyle.setGroupId(" org.apache.maven.plugins ");
      pluginCheckStyle.setArtifactId("maven-checkstyle-plugin");
      pluginCheckStyle.setVersion("2.17");
      PluginExecution checkStyleExecution = new PluginExecution();
      if (order.indexOf("Unit Test Failure") < order.indexOf("Coding Style Failure")) {
        checkStyleExecution.setId("test");
        checkStyleExecution.setPhase("test");
      } else if (order.indexOf("Unit Test Failure") > order.indexOf("Coding Style Failure")) {
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
    final List<ReportPlugin> reportingPlugins = new ArrayList<>();
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
    try {
      final MavenXpp3Writer writer = new MavenXpp3Writer();
      writer.write(new FileWriter(targetPomSavePath
          + name + "_pom.xml"), model);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}