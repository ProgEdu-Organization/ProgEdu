package fcu.selab.progedu.setting;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.lifecycle.Execution;
import org.junit.Test;
import java.util.Properties;

public class MavenAssignmentSettingTest {

  @Test
  public void createAssignmentSettingTest() {
    List<String> order = new ArrayList<String>();
    order.add("compilerFailure");
    order.add("codingStyle");
    order.add("test");
    List<Dependency> dependencyList = MavenAssignmentSetting.createAssignmentSettingTest(order, "test").getDependencies();
    for (Dependency dependency : dependencyList) {
      System.out.println(dependency.toString());
    }
    List<Plugin> pluginList = MavenAssignmentSetting.createAssignmentSettingTest(order, "test").getBuild().getPlugins();
    for (Plugin plugin : pluginList) {
      System.out.println(plugin.getGroupId());
      System.out.println(plugin.getArtifactId());
      System.out.println(plugin.getVersion());
    }
  }

}